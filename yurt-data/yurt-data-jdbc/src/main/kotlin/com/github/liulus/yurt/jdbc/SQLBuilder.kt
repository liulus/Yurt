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

    /**
     * MySQL 分页，参数1 ：第几条开始( offset ); 参数2：查询多少条(pageSize)
     */
    private const val MYSQL_PAGE_SQL = "%s limit %d, %d "

    /**
     * DB2 分页，参数1 ：第几条开始( offset ); 参数2：第几条为止(maxResult)
     */
    private const val DB2_PAGE_SQL = "select * from ( select t.*, rownumber() over() rowid from ( %s ) t ) where rowid > %d ) and rowid <= %d "

    /**
     * Oracle 分页，参数1：第几条为止(maxResult); 参数2 ：第几条开始( offset )
     */
    private const val ORACLE_PAGE_SQL = "select * from (select t.*, rownum rowno from ( %s ) t where rownum <= %d ) where rowno > %d "


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
    fun pageSQL(sql: SQL, dbName: String, pageNum: Int, pageSize: Int): String {
        if (StringUtils.isEmpty(dbName)) {
            throw UnsupportedOperationException("unsupported operation build page sql un_know database")
        }
        return when (dbName.toUpperCase()) {
            "DB2" -> String.format(DB2_PAGE_SQL, sql.toString(), pageSize * (pageNum - 1), pageSize * pageNum)
            "MYSQL" -> String.format(MYSQL_PAGE_SQL, sql.toString(), pageSize * (pageNum - 1), pageSize)
            "ORACLE" -> String.format(ORACLE_PAGE_SQL, sql.toString(), pageSize * pageNum, pageSize * (pageNum - 1))
            else -> throw UnsupportedOperationException("unsupported operation build page sql for database: $dbName")
        }
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
