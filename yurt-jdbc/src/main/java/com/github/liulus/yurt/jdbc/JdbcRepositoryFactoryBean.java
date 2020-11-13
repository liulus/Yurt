package com.github.liulus.yurt.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.ResolvableType;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public class JdbcRepositoryFactoryBean<T> implements MethodInterceptor, FactoryBean<T> {

    private final Class<T> repositoryInterface;
    private Class<?> entityClass;
    private String dataSourceBeanName;
    @Resource
    private SQLExecutorContext sqlExecutorContext;

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
        SQLExecutor sqlExecutor = sqlExecutorContext.getSQLExecutor(dataSourceBeanName);
        String methodName = invocation.getMethod().getName();
        switch (methodName) {
            case "insert":
                return sqlExecutor.insert(invocation.getArguments()[0]);
            case "updateIgnoreNull":
                return sqlExecutor.updateIgnoreNull(invocation.getArguments()[0]);
            case "deleteById":
                return sqlExecutor.deleteById(entityClass, (Long) invocation.getArguments()[0]);
            case "selectById":
                return sqlExecutor.selectById(entityClass, (Long) invocation.getArguments()[0]);
            case "selectByIds":
                return sqlExecutor.selectByIds(entityClass, (Collection<Long>) invocation.getArguments()[0]);
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

}
