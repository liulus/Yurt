package com.github.liulus.yurt.jdbc

import org.springframework.beans.BeanUtils
import org.springframework.util.ReflectionUtils
import org.springframework.util.StringUtils

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/5
 */
internal object SQLBuilder {

    @JvmStatic
    fun insertSQL(entity: Any): SQL {
        val eClass = entity.javaClass
        val tableMetadata = TableMetadata.forClass(eClass)
        val fieldColumnMap = tableMetadata.fieldColumnMap
        val sql = SQL().INSERT_INTO(tableMetadata.tableName)
        for ((k, v) in fieldColumnMap) {
            // 忽略主键
            val ps = BeanUtils.getPropertyDescriptor(eClass, k)
            if (k == tableMetadata.idField || ps == null || ps.readMethod == null) {
                continue
            }
            val value = ReflectionUtils.invokeMethod(ps.readMethod, entity)
            if (!StringUtils.isEmpty(value)) {
                sql.VALUES(v, tokenParam(k))
            }
        }
        return sql
    }

    @JvmStatic
    fun updateSQL(entity: Any, ignoreNull: Boolean): SQL {
        val entityClass = entity.javaClass
        val metadata = TableMetadata.forClass(entityClass)
        val fieldColumnMap = metadata.fieldColumnMap
        val sql: SQL = SQL().UPDATE(metadata.tableName)
        for ((k, v) in fieldColumnMap) {
            // 忽略主键
            val ps = BeanUtils.getPropertyDescriptor(entityClass, k)
            if (k == metadata.idField || ps == null || ps.readMethod == null) {
                continue
            }
            if (ignoreNull) {
                val value = ReflectionUtils.invokeMethod(ps.readMethod, entity)
                if (value != null) {
                    sql.SET(eq(v, k))
                }
            } else {
                sql.SET(eq(v, k))
            }
        }
        sql.WHERE(eq(metadata.idColumn, metadata.idField))
        return sql
    }

    @JvmStatic
    fun deleteSQL(entityClass: Class<*>): SQL {
        val tableMetadata = TableMetadata.forClass(entityClass)
        return SQL().DELETE_FROM(tableMetadata.tableName)
                .WHERE(eq(tableMetadata.idColumn, "param"))
    }

    @JvmStatic
    fun selectSQL(entityClass: Class<*>): SQL {
        val tableMetadata = TableMetadata.forClass(entityClass)
        return SQL().DELETE_FROM(tableMetadata.tableName)
                .WHERE(eq(tableMetadata.idColumn, "param"))
    }


    @JvmStatic
    private fun eq(column: String, param: String): String {
        return eq(column, param, SQLType.JDBC)
    }

    @JvmStatic
    private fun eq(column: String, param: String, sqlType: SQLType): String {
        return "$column = ${tokenParam(param, sqlType)}"
    }

    @JvmStatic
    private fun tokenParam(param: String): String {
        return tokenParam(param, SQLType.JDBC)
    }

    @JvmStatic
    private fun tokenParam(param: String, sqlType: SQLType): String {
        return when (sqlType) {
            SQLType.JDBC -> ":$param"
            SQLType.MYBATIS -> "#{$param}"
        }
    }


    enum class SQLType {
        JDBC, MYBATIS
    }

}
