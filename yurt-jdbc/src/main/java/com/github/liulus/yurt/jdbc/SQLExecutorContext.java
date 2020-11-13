package com.github.liulus.yurt.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/13
 */
class SQLExecutorContext {

    private static final Map<String, SQLExecutor> REPOSITORY_MAP = new ConcurrentHashMap<>(4, 1);

    @Resource
    private Map<String, DataSource> dataSourceMap;

    @Resource
    private List<SQLExecutor> jdbcExecutors;

    public SQLExecutor getSQLExecutor(String dataSourceId) {
        SQLExecutor cacheRepository = REPOSITORY_MAP.get(dataSourceId);
        if (cacheRepository != null) {
            return cacheRepository;
        }
        DataSource dataSource = getDataSource(dataSourceId);
        for (SQLExecutor executor : jdbcExecutors) {
            SQLExecutorImpl repositoryImpl = (SQLExecutorImpl) executor;
            JdbcTemplate jdbcTemplate = (JdbcTemplate) repositoryImpl.getJdbcOperations().getJdbcOperations();
            DataSource jdbcTemplateDataSource = jdbcTemplate.getDataSource();
            if (Objects.equals(dataSource, jdbcTemplateDataSource)) {
                REPOSITORY_MAP.put(dataSourceId, executor);
                return executor;
            }
        }
        //
        SQLExecutorImpl executor = new SQLExecutorImpl(dataSource);
        REPOSITORY_MAP.put(dataSourceId, executor);
        return executor;
    }

    private DataSource getDataSource(String dataSourceId) {
        if (StringUtils.hasText(dataSourceId)) {
            return Optional.of(dataSourceId)
                    .map(dataSourceMap::get)
                    .orElseThrow(() -> new IllegalArgumentException("no dataSource bean with name " + dataSourceId));
        } else if (dataSourceMap.size() == 1) {
            return dataSourceMap.values().iterator().next();
        }
        throw new IllegalArgumentException("to many dataSource bean fund, please config your dataSource bean id");
    }


    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public void setJdbcExecutors(List<SQLExecutor> jdbcExecutors) {
        this.jdbcExecutors = jdbcExecutors;
    }
}
