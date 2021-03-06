package com.github.liulus.yurt.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public class JdbcRepositoryFactoryBean<T> implements ApplicationContextAware, MethodInterceptor, FactoryBean<T> {
    private static final Map<String, SQLExecutor> EXECUTOR_MAP = new ConcurrentHashMap<>(4, 1);
    private final Class<T> repositoryInterface;
    private Class<?> entityClass;
    private String dataSourceBeanName;
    private SQLExecutor sqlExecutor;

    public JdbcRepositoryFactoryBean(Class<T> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
        for (Type parent : repositoryInterface.getGenericInterfaces()) {
            ResolvableType parentType = ResolvableType.forType(parent);
            if (parentType.getRawClass() == JdbcRepository.class) {
                entityClass = parentType.getGeneric(0).getRawClass();
                break;
            }
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = invocation.getMethod().getName();
        Object argument = Optional.ofNullable(invocation.getArguments())
                .filter(args -> args.length > 0)
                .map(args -> args[0])
                .orElse(null);
        switch (methodName) {
            case "toString":
                return repositoryInterface.getName() + "<" + entityClass.getSimpleName() + ">";
            case "insert":
                return sqlExecutor.insert(argument);
            case "batchInsert":
                return sqlExecutor.batchInsert((Collection<?>) argument);
            case "updateIgnoreNull":
                return sqlExecutor.updateIgnoreNull(argument);
            case "deleteById":
                return sqlExecutor.deleteById(entityClass, (Long) argument);
            case "deleteLogicalById":
                return sqlExecutor.deleteLogicalById(entityClass, (Long) argument);
            case "selectById":
                return sqlExecutor.selectById(entityClass, (Long) argument);
            case "selectByIds":
                //noinspection unchecked
                return sqlExecutor.selectByIds(entityClass, (Collection<Long>) argument);
            default:
                return SQLContext.getContext(repositoryInterface, invocation.getMethod())
                        .execute(sqlExecutor, invocation.getArguments());
        }
    }

    @Override
    public T getObject() throws Exception {
        return ProxyFactory.getProxy(repositoryInterface, this);
    }

    @Override
    public Class<T> getObjectType() {
        return repositoryInterface;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);
        Map<String, SQLExecutor> sqlExecutorMap = applicationContext.getBeansOfType(SQLExecutor.class);
        if (StringUtils.hasText(dataSourceBeanName)) {
            this.sqlExecutor = EXECUTOR_MAP.containsKey(dataSourceBeanName) ? EXECUTOR_MAP.get(dataSourceBeanName)
                    : initSQLExecutor(dataSourceBeanName, dataSourceMap, sqlExecutorMap.values());
            return;
        }
        Assert.isTrue(dataSourceMap.size() == 1, "to many dataSource bean fund, please config your dataSource bean id");
        if (EXECUTOR_MAP.size() == 1) {
            this.sqlExecutor = EXECUTOR_MAP.values().iterator().next();
            return;
        }
        this.sqlExecutor = new SQLExecutorImpl(dataSourceMap.values().iterator().next());
        EXECUTOR_MAP.put("one", this.sqlExecutor);
    }

    private SQLExecutor initSQLExecutor(String dataSourceBeanName, Map<String, DataSource> dataSourceMap,
                                        Collection<SQLExecutor> sqlExecutors) {
        DataSource dataSource = Optional.of(dataSourceBeanName)
                .map(dataSourceMap::get)
                .orElseThrow(() -> new IllegalArgumentException("no dataSource bean with name " + dataSourceBeanName));

        for (SQLExecutor executor : sqlExecutors) {
            SQLExecutorImpl repositoryImpl = (SQLExecutorImpl) executor;
            JdbcTemplate jdbcTemplate = (JdbcTemplate) repositoryImpl.getJdbcOperations().getJdbcOperations();
            DataSource jdbcTemplateDataSource = jdbcTemplate.getDataSource();
            if (Objects.equals(dataSource, jdbcTemplateDataSource)) {
                EXECUTOR_MAP.put(dataSourceBeanName, executor);
                return executor;
            }
        }

        SQLExecutorImpl executor = new SQLExecutorImpl(dataSource);
        EXECUTOR_MAP.put(dataSourceBeanName, executor);
        return executor;
    }

}
