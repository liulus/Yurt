package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.convention.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        Asserts.notNull(entity, "insert with entity can not be null");
        Class<?> entityClass = entity.getClass();
        SQL sql = SQLBuilder.insertSQL(entity);
        TableMetadata tableMetadata = TableMetadata.forClass(entityClass);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParams = sqlParams(entity);
        namedParameterJdbcOperations.update(sql.toString(), sqlParams, keyHolder);
        PropertyDescriptor keyPs = BeanUtils.getPropertyDescriptor(entityClass, tableMetadata.getIdField());
        // set key property
        if (keyPs != null) {
            ReflectionUtils.invokeMethod(keyPs.getWriteMethod(), entity,
                    Objects.requireNonNull(keyHolder.getKey()));
        }
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public <E> int updateIgnoreNull(E entity) {
        Asserts.notNull(entity, "update with entity can not be null");
        SQL sql = SQLBuilder.updateSQL(entity, true);
        return namedParameterJdbcOperations.update(sql.toString(), sqlParams(entity));
    }

    @Override
    public <E> int deleteById(Class<E> eClass, Long id) {
        Asserts.notNull(id, "id can not be null");
        SQL sql = SQLBuilder.deleteSQL(eClass);
        return namedParameterJdbcOperations.update(sql.toString(), sqlParams(id));
    }

    @Override
    public <E> E selectById(Class<E> eClass, Long id) {
        Asserts.notNull(id, "id can not be null");
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
        if (BeanUtils.isSimpleProperty(requiredType)) {
            return namedParameterJdbcOperations.queryForList(sql.toString(), sqlParams(params), requiredType);
        }
        AnnotationRowMapper<E> rowMapper = new AnnotationRowMapper<>(requiredType);
        return namedParameterJdbcOperations.query(sql.toString(), sqlParams(params), rowMapper);
    }

    @Override
    public <E> List<E> selectForPage(SQL sql, Object params, int pageNum, int pageSize, Class<E> requiredType) {
        String pageSQL = SQLBuilder.pageSQL(sql, getDbName(), pageNum, pageSize);
        if (BeanUtils.isSimpleProperty(requiredType)) {
            return namedParameterJdbcOperations.queryForList(pageSQL, sqlParams(params), requiredType);
        }
        AnnotationRowMapper<E> rowMapper = new AnnotationRowMapper<>(requiredType);
        return namedParameterJdbcOperations.query(pageSQL, sqlParams(params), rowMapper);
    }

    @Override
    public long count(SQL sql, Object params) {
        Long count = namedParameterJdbcOperations.queryForObject(sql.toCountSql(), sqlParams(params), Long.class);
        return count == null ? 0L : count;
    }

    @Override
    public int update(SQL sql, Object params) {
        return namedParameterJdbcOperations.update(sql.toString(), sqlParams(params));
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


}
