package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.convention.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
class SQLBuilder {

    private SQLBuilder() {
    }

    private static final String SINGLE_PARAM = "param";

    /**
     * MySQL 分页，参数1 ：第几条开始( offset ); 参数2：查询多少条(pageSize)
     */
    private static final String MYSQL_PAGE_SQL = "%s LIMIT %d, %d ";

    /**
     * DB2 分页，参数1 ：第几条开始( offset ); 参数2：第几条为止(maxResult)
     */
    private static final String DB2_PAGE_SQL = "SELECT * FROM ( SELECT t.*, rownumber() over() rowid FROM ( %s ) t ) WHERE rowid > %d ) AND rowid <= %d ";

    /**
     * Oracle 分页，参数1：第几条为止(maxResult); 参数2 ：第几条开始( offset )
     */
    private static final String ORACLE_PAGE_SQL = "SELECT * FROM (SELECT t.*, rownum rowno FROM ( %s ) t WHERE rownum <= %d ) WHERE rowno > %d ";


    public static SQL insertSQL(Object entity) {
        Asserts.notNull(entity, "insert with entity can not be null");
        Class<?> eClass = entity.getClass();
        TableMetadata tableMetadata = TableMetadata.forClass(eClass);
        Map<String, String> fieldColumnMap = tableMetadata.getFieldColumnMap();
        SQL sql = new SQL().INSERT_INTO(tableMetadata.getTableName());
        for (Map.Entry<String, String> entry : fieldColumnMap.entrySet()) {
            String k = entry.getKey();
            // 忽略主键
            PropertyDescriptor ps = BeanUtils.getPropertyDescriptor(eClass, k);
            if (Objects.equals(k, tableMetadata.getIdField())
                    || ps == null || ps.getReadMethod() == null) {
                continue;
            }
            Object value = ReflectionUtils.invokeMethod(ps.getReadMethod(), entity);
            if (!StringUtils.isEmpty(value)) {
                sql.VALUES(entry.getValue(), tokenParam(k));
            }
        }
        return sql;
    }

    public static SQL updateSQL(Object entity, boolean ignoreNull) {
        Asserts.notNull(entity, "update with entity can not be null");
        Class<?> entityClass = entity.getClass();
        TableMetadata tableMetadata = TableMetadata.forClass(entityClass);
        Map<String, String> fieldColumnMap = tableMetadata.getFieldColumnMap();
        SQL sql = new SQL().UPDATE(tableMetadata.getTableName());
        for (Map.Entry<String, String> entry : fieldColumnMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            // 忽略主键
            PropertyDescriptor ps = BeanUtils.getPropertyDescriptor(entityClass, k);
            if (Objects.equals(k, tableMetadata.getIdField())
                    || ps == null || ps.getReadMethod() == null) {
                continue;
            }
            if (ignoreNull) {
                Object value = ReflectionUtils.invokeMethod(ps.getReadMethod(), entity);
                if (value != null) {
                    sql.SET(eq(v, k));
                }
            } else {
                sql.SET(eq(v, k));
            }
        }
        sql.WHERE(eq(tableMetadata.getIdColumn(), tableMetadata.getIdField()));
        return sql;
    }

    public static SQL deleteSQL(Class<?> entityClass) {
        TableMetadata tableMetadata = TableMetadata.forClass(entityClass);
        return new SQL().DELETE_FROM(tableMetadata.getTableName())
                .WHERE(eq(tableMetadata.getIdColumn(), SINGLE_PARAM));
    }

    public static SQL selectByIdSQL(Class<?> entityClass) {
        TableMetadata tableMetadata = TableMetadata.forClass(entityClass);
        return new SQL().SELECT(tableMetadata.getColumns()).FROM(tableMetadata.getTableName())
                .WHERE(eq(tableMetadata.getIdColumn(), SINGLE_PARAM));
    }

    public static SQL selectByIdsSQL(Class<?> entityClass) {
        TableMetadata tableMetadata = TableMetadata.forClass(entityClass);
        return new SQL().SELECT(tableMetadata.getColumns()).FROM(tableMetadata.getTableName())
                .WHERE(tableMetadata.getIdColumn() + " in (:param)");
    }

    public static SqlParameterSource sqlParams(Object params) {
        if (params == null) {
            return EmptySqlParameterSource.INSTANCE;
        }
        if (params instanceof Collection
                || BeanUtils.isSimpleProperty(params.getClass())) {
            return new MapSqlParameterSource(SINGLE_PARAM, params);
        }
        if (params instanceof Map) {
            //noinspection unchecked
            Map<String, ?> paramsMap = (Map<String, ?>) params;
            if (paramsMap.isEmpty()) {
                return EmptySqlParameterSource.INSTANCE;
            }
            return new MapSqlParameterSource(paramsMap);
        }
        if (params instanceof SqlParameterSource) {
            return (SqlParameterSource) params;
        }
        return new BeanPropertySqlParameterSource(params);
    }

    public static String pageSQL(SQL sql, String dbName, int pageNum, int pageSize) {
        if (StringUtils.isEmpty(dbName)) {
            throw new UnsupportedOperationException("unsupported operation build page sql unknow database");
        }
        dbName = dbName.toUpperCase();
        if (dbName.contains("DB2")) {
            return String.format(DB2_PAGE_SQL, sql.toString(), pageSize * (pageNum - 1), pageSize * pageNum);
        }
        if (dbName.contains("MYSQL")) {
            return String.format(MYSQL_PAGE_SQL, sql.toString(), pageSize * (pageNum - 1), pageSize);
        }
        if (dbName.contains("ORACLE")) {
            return String.format(ORACLE_PAGE_SQL, sql.toString(), pageSize * pageNum, pageSize * (pageNum - 1));
        }

        throw new UnsupportedOperationException("unsupported operation build page sql for database: " + dbName);
    }


    private static String eq(String column, String param) {
        return column + " = " + tokenParam(param);
    }


    private static String tokenParam(String k) {
        return ":" + k;
    }


}
