package com.github.liulus.yurt.jdbc

import org.springframework.beans.BeanUtils
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.util.Assert
import org.springframework.util.CollectionUtils
import org.springframework.util.ReflectionUtils
import org.springframework.util.StringUtils
import java.util.Objects
import javax.sql.DataSource

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/5
 */
class SQLExecutorImpl(
        var namedParameterJdbcOperations: NamedParameterJdbcOperations
) : SQLExecutor {

    var dbName: String = ""
        get() {
            if (StringUtils.isEmpty(field)) {
                val jdbcOperations = namedParameterJdbcOperations.jdbcOperations
                if (jdbcOperations is JdbcTemplate) {
                    dbName = jdbcOperations.dataSource.connection.metaData.databaseProductName
                }
            }
            return field
        }

    constructor(dataSource: DataSource) : this(NamedParameterJdbcTemplate(dataSource))

    override fun <E> insert(entity: E): Long {
        requireNotNull(entity) { "insert with entity can not be null" }
        val entityClass = entity.javaClass
        val sql = SQLBuilder.insertSQL(entity)

        val tableMetadata = TableMetadata.forClass(entityClass)
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        val sqlParams = sqlParams(entity)
        val insert: Int = namedParameterJdbcOperations.update(sql.toString(), sqlParams, keyHolder)
        val keyPs = BeanUtils.getPropertyDescriptor(entityClass, tableMetadata.idField)
        // set key property
        if (keyPs != null) {
            ReflectionUtils.invokeMethod(keyPs.writeMethod, entity,
                    Objects.requireNonNull(keyHolder.key).toLong())
        }
        return insert.toLong()
    }

    override fun <E> updateIgnoreNull(entity: E): Int {
        requireNotNull(entity) { "update with entity can not be null" }
        val sql = SQLBuilder.updateSQL(entity, true).toString()
        return namedParameterJdbcOperations.update(sql, sqlParams(entity))
    }

    override fun <E> deleteById(eClass: Class<E>, id: Long): Int {
        Assert.notNull(id, "id can not be null")
        val sql = SQLBuilder.deleteSQL(eClass).toString()
        return namedParameterJdbcOperations.update(sql, sqlParams(id))
    }

    override fun <E> selectById(eCLass: Class<E>, id: Long): E? {
        Assert.notNull(id, "id can not be null")
        val tableMetadata = TableMetadata.forClass(eCLass)
        val sql = SQL().SELECT(tableMetadata.columns).FROM(tableMetadata.tableName)
                .WHERE("${tableMetadata.idColumn} = :param")
        return selectForObject(sql, id, eCLass)
    }

    override fun <E> selectByIds(eClass: Class<E>, ids: Collection<Long>): List<E> {
        if (CollectionUtils.isEmpty(ids)) {
            return emptyList()
        }
        val tableMetadata = TableMetadata.forClass(eClass)
        val sql = SQL().SELECT(tableMetadata.columns).FROM(tableMetadata.tableName)
                .WHERE("${tableMetadata.idColumn} in :param")
        return selectForList(sql, ids, eClass)
    }


    override fun <E> selectForObject(sql: SQL, params: Any?, requiredType: Class<E>): E? {
        val rs = selectForList(sql, params, requiredType)
        if (CollectionUtils.isEmpty(rs)) {
            return null
        }
        if (rs.size > 1) {
            throw IncorrectResultSizeDataAccessException(1, rs.size)
        }
        return rs.iterator().next()
    }

    override fun <E> selectForList(sql: SQL, params: Any?, requiredType: Class<E>): List<E> {
        if (BeanUtils.isSimpleProperty(requiredType)) {
            return namedParameterJdbcOperations.queryForList(sql.toString(), sqlParams(params), requiredType)
        }
        val rowMapper = AnnotationRowMapper(requiredType)
        return namedParameterJdbcOperations.query(sql.toString(), sqlParams(params), rowMapper)

    }

    override fun count(sql: SQL, params: Any?): Long {
        return namedParameterJdbcOperations.queryForObject(sql.toCountSql(), sqlParams(params), Long::class.java) ?: 0L
    }

    override fun update(sql: SQL, params: Any?): Int {
        return namedParameterJdbcOperations.update(sql.toString(), sqlParams(params))
    }

    private fun <E> sqlParams(params: E): SqlParameterSource {
        if (params == null) {
            return EmptySqlParameterSource.INSTANCE
        }
        if (params is Collection<*>
                || BeanUtils.isSimpleProperty(params.javaClass)) {
            return MapSqlParameterSource("param", params)
        }
        if (params is Map<*, *>) {
            if (params.isEmpty()) {
                return EmptySqlParameterSource.INSTANCE
            }
            val parameterSource = MapSqlParameterSource()
            params.forEach { parameterSource.addValue(it.key as String, it.value) }
            return parameterSource
        }
        if (params is SqlParameterSource) {
            return params
        }
        return BeanPropertySqlParameterSource(params)
    }


}


