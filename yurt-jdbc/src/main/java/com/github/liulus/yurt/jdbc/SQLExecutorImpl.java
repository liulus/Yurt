package com.github.liulus.yurt.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.liulus.yurt.jdbc.SQLBuilder.sqlParams;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public class SQLExecutorImpl implements SQLExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SQLExecutorImpl.class);

    private String dbName;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public SQLExecutorImpl(DataSource dataSource) {
        this.namedParameterJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
    }

    public SQLExecutorImpl(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }


    @Override
    public <E> long insert(E entity) {
        Assert.notNull(entity, "insert with entity can not be null");
        Class<?> entityClass = entity.getClass();
        String sql = SQLBuilder.insertSQL(entity).toString();
        TableMetadata tableMetadata = TableMetadata.forClass(entityClass);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParams = sqlParams(entity);
        logger.debug("insert sql: {} \n params: {}", sql, sqlParams);
        namedParameterJdbcOperations.update(sql, sqlParams, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        PropertyDescriptor keyPs = BeanUtils.getPropertyDescriptor(entityClass, tableMetadata.getIdField());
        // set key property
        if (keyPs != null) {
            ReflectionUtils.invokeMethod(keyPs.getWriteMethod(), entity, id);
        }
        return id;
    }

    @Override
    public <E> int batchInsert(Collection<E> eList) {
        if (CollectionUtils.isEmpty(eList)) {
            return 0;
        }
        E entity = eList.iterator().next();
        SQL sql = SQLBuilder.insertSQL(entity);

        SqlParameterSource[] parameterSources = eList.stream()
                .map(SQLBuilder::sqlParams).toArray(SqlParameterSource[]::new);
        logger.debug("batchInsert sql: {} \n params: {}", sql, parameterSources);
        int[] updateResult = namedParameterJdbcOperations.batchUpdate(sql.toString(), parameterSources);

        int row = 0;
        for (int res : updateResult) {
            row += res;
        }
        return row;
    }

    @Override
    public <E> int updateIgnoreNull(E entity) {
        Assert.notNull(entity, "update with entity can not be null");
        String sql = SQLBuilder.updateSQL(entity, true).toString();
        SqlParameterSource sqlParams = sqlParams(entity);
        logger.debug("update sql: {} \n params: {}", sql, sqlParams);
        return namedParameterJdbcOperations.update(sql, sqlParams);
    }

    @Override
    public <E> int deleteById(Class<E> eClass, Long id) {
        Assert.notNull(id, "delete id can not be null");
        String sql = SQLBuilder.deleteSQL(eClass).toString();
        SqlParameterSource sqlParams = sqlParams(id);
        logger.debug("delete sql: {} \n params: {}", sql, sqlParams);
        return namedParameterJdbcOperations.update(sql, sqlParams);
    }

    @Override
    public <E> int deleteLogicalById(Class<E> eClass, Long id) {
        Assert.notNull(id, "delete logical id can not be null");
        String sql = SQLBuilder.deleteLogicalSQL(eClass, getDbName()).toString();
        SqlParameterSource sqlParams = sqlParams(id);
        logger.debug("delete sql: {} \n params: {}", sql, sqlParams);
        return namedParameterJdbcOperations.update(sql, sqlParams);
    }

    @Override
    public <E> E selectById(Class<E> eClass, Long id) {
        Assert.notNull(id, "id can not be null");
        SQL sql = SQLBuilder.selectByIdSQL(eClass);
        return selectForObject(sql, id, eClass);
    }

    @Override
    public <E> List<E> selectByIds(Class<E> eClass, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        SQL sql = SQLBuilder.selectByIdsSQL(eClass);
        return selectForList(sql, ids, eClass);
    }

    @Override
    public <E> E selectForObject(SQL sql, Object params, Class<E> requiredType) {
        List<E> rs = selectForList(sql, params, requiredType);
        if (CollectionUtils.isEmpty(rs)) {
            return null;
        }
        if (rs.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, rs.size());
        }
        return rs.iterator().next();
    }

    @Override
    public <E> List<E> selectForList(SQL sql, Object params, Class<E> requiredType) {
        String sqlStr = sql.toString();
        SqlParameterSource sqlParams = sqlParams(params);
        logger.debug("select sql: {} \n params: {}", sql, sqlParams);
        if (BeanUtils.isSimpleProperty(requiredType)) {
            return namedParameterJdbcOperations.queryForList(sqlStr, sqlParams, requiredType);
        }
        AnnotationRowMapper<E> rowMapper = new AnnotationRowMapper<>(requiredType);
        return namedParameterJdbcOperations.query(sqlStr, sqlParams, rowMapper);
    }

    @Override
    public <E> List<E> selectForPage(SQL sql, Object params, int pageNum, int pageSize, Class<E> requiredType) {
        String pageSQL = SQLBuilder.pageSQL(sql, getDbName(), pageNum, pageSize);
        SqlParameterSource sqlParams = sqlParams(params);
        logger.debug("page sql: {} \n params: {}", sql, sqlParams);
        if (BeanUtils.isSimpleProperty(requiredType)) {
            return namedParameterJdbcOperations.queryForList(pageSQL, sqlParams, requiredType);
        }
        AnnotationRowMapper<E> rowMapper = new AnnotationRowMapper<>(requiredType);
        return namedParameterJdbcOperations.query(pageSQL, sqlParams, rowMapper);
    }

    @Override
    public long count(SQL sql, Object params) {
        String countSql = sql.toCountSql();
        SqlParameterSource sqlParams = sqlParams(params);
        logger.debug("count sql: {} \n params: {}", countSql, sqlParams);
        Long count = namedParameterJdbcOperations.queryForObject(countSql, sqlParams, Long.class);
        return count == null ? 0L : count;
    }

    @Override
    public int update(SQL sql, Object params) {
        String updateSql = sql.toString();
        SqlParameterSource sqlParams = sqlParams(params);
        logger.debug("update sql: {} \n params: {}", updateSql, sqlParams);
        return namedParameterJdbcOperations.update(updateSql, sqlParams);
    }


    public String getDbName() {
        if (StringUtils.isEmpty(dbName)) {
            dbName = namedParameterJdbcOperations.getJdbcOperations()
                    .execute((ConnectionCallback<String>) con -> con.getMetaData().getDatabaseProductName());
        }
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }


    public NamedParameterJdbcOperations getJdbcOperations() {
        return namedParameterJdbcOperations;
    }
}
