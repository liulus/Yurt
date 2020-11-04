package com.github.liulus.yurt.jdbc

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/2
 */
class SQL {

    private val sql = SQLStatement()

    fun UPDATE(table: String): SQL {
        sql.statementType = SQLStatement.StatementType.UPDATE
        sql.tables.add(table)
        return this
    }

    fun SET(sets: String): SQL {
        sql.sets.add(sets)
        return this
    }

    fun SET(vararg sets: String): SQL {
        sql.sets.addAll(listOf(*sets))
        return this
    }

    fun INSERT_INTO(tableName: String): SQL {
        sql.statementType = SQLStatement.StatementType.INSERT
        sql.tables.add(tableName)
        return this
    }

    fun VALUES(columns: String, values: String): SQL {
        sql.columns.add(columns)
        sql.values.add(values)
        return this
    }

    fun INTO_COLUMNS(vararg columns: String): SQL {
        sql.columns.addAll(listOf(*columns))
        return this
    }

    fun INTO_VALUES(vararg values: String): SQL {
        sql.values.addAll(listOf(*values))
        return this
    }

    fun SELECT(columns: String): SQL {
        sql.statementType = SQLStatement.StatementType.SELECT
        sql.select.add(columns)
        return this
    }

    fun SELECT(vararg columns: String): SQL {
        sql.statementType = SQLStatement.StatementType.SELECT
        sql.select.addAll(listOf(*columns))
        return this
    }

    fun SELECT_DISTINCT(columns: String): SQL {
        sql.distinct = true
        SELECT(columns)
        return this
    }

    fun SELECT_DISTINCT(vararg columns: String): SQL {
        sql.distinct = true
        SELECT(*columns)
        return this
    }

    fun DELETE_FROM(table: String): SQL {
        sql.statementType = SQLStatement.StatementType.DELETE
        sql.tables.add(table)
        return this
    }

    fun FROM(table: String): SQL {
        sql.tables.add(table)
        return this
    }


    fun FROM(vararg tables: String): SQL {
        sql.tables.addAll(listOf(*tables))
        return this
    }

    fun JOIN(join: String): SQL {
        sql.join.add(join)
        return this
    }

    fun JOIN(vararg joins: String): SQL {
        sql.join.addAll(listOf(*joins))
        return this
    }

    fun INNER_JOIN(join: String): SQL {
        sql.innerJoin.add(join)
        return this
    }

    fun INNER_JOIN(vararg joins: String): SQL {
        sql.innerJoin.addAll(listOf(*joins))
        return this
    }

    fun LEFT_OUTER_JOIN(join: String): SQL {
        sql.leftOuterJoin.add(join)
        return this
    }

    fun LEFT_OUTER_JOIN(vararg joins: String): SQL {
        sql.leftOuterJoin.addAll(listOf(*joins))
        return this
    }

    fun RIGHT_OUTER_JOIN(join: String): SQL {
        sql.rightOuterJoin.add(join)
        return this
    }

    fun RIGHT_OUTER_JOIN(vararg joins: String): SQL {
        sql.rightOuterJoin.addAll(listOf(*joins))
        return this
    }

    fun OUTER_JOIN(join: String): SQL {
        sql.outerJoin.add(join)
        return this
    }

    fun OUTER_JOIN(vararg joins: String): SQL {
        sql.outerJoin.addAll(listOf(*joins))
        return this
    }

    fun WHERE(conditions: String): SQL {
        sql.where.add(conditions)
        sql.lastList = sql.where
        return this
    }

    fun WHERE(vararg conditions: String): SQL {
        sql.where.addAll(listOf(*conditions))
        sql.lastList = sql.where
        return this
    }

    fun OR(): SQL {
        sql.lastList.add(SQLStatement.OR)
        return this
    }

    fun AND(): SQL {
        sql.lastList.add(SQLStatement.AND)
        return this
    }

    fun GROUP_BY(columns: String): SQL {
        sql.groupBy.add(columns)
        return this
    }

    fun GROUP_BY(vararg columns: String): SQL {
        sql.groupBy.addAll(listOf(*columns))
        return this
    }

    fun HAVING(conditions: String): SQL {
        sql.having.add(conditions)
        sql.lastList = sql.having
        return this
    }

    fun HAVING(vararg conditions: String): SQL {
        sql.having.addAll(listOf(*conditions))
        sql.lastList = sql.having
        return this
    }

    fun ORDER_BY(columns: String): SQL {
        sql.orderBy.add(columns)
        return this
    }

    fun ORDER_BY(vararg columns: String): SQL {
        sql.orderBy.addAll(listOf(*columns))
        return this
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sql.sql(sb)
        return sb.toString()
    }

    fun toCountSql(): String {
        sql.statementType = SQLStatement.StatementType.COUNT
        val result = toString()
        sql.statementType = SQLStatement.StatementType.SELECT
        return result
    }

}


