package com.github.liulus.yurt.jdbc

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */

internal class SQLStatement {

    companion object {
        const val INSERT_INTO = "INSERT INTO"
        const val VALUES = " VALUES"
        const val UPDATE = "UPDATE"
        const val SET = " SET"
        const val DELETE_FROM = "DELETE FROM"
        const val SELECT = "SELECT"
        const val SELECT_DISTINCT = "$SELECT DISTINCT"
        const val FROM = " FROM"
        const val JOIN = " JOIN"
        const val INNER_JOIN = " INNER$JOIN"
        const val OUTER_JOIN = " OUTER$JOIN"
        const val LEFT_JOIN = " LEFT$JOIN"
        const val RIGHT_JOIN = " RIGHT$JOIN"
        const val WHERE = " WHERE"
        const val ORDER_BY = " ORDER BY"
        const val GROUP_BY = " GROUP BY"
        const val HAVING = " HAVING"
        const val AND = ") AND ("
        const val OR = ") OR ("
        const val SPACE = " "
        const val SPACE_AND = " AND"
        private const val LEFT_BRACKET = "("
        private const val RIGHT_BRACKET = ")"
        private const val EMPTY = ""
        private const val COMMA = ","
    }

    enum class StatementType {
        DELETE, INSERT, SELECT, UPDATE, COUNT
    }

    var statementType: StatementType? = null
    var sets: MutableList<String> = ArrayList()
    var select: MutableList<String> = ArrayList()
    var tables: MutableList<String> = ArrayList()
    var join: MutableList<String> = ArrayList()
    var innerJoin: MutableList<String> = ArrayList()
    var outerJoin: MutableList<String> = ArrayList()
    var leftOuterJoin: MutableList<String> = ArrayList()
    var rightOuterJoin: MutableList<String> = ArrayList()
    var where: MutableList<String> = ArrayList()
    var having: MutableList<String> = ArrayList()
    var groupBy: MutableList<String> = ArrayList()
    var orderBy: MutableList<String> = ArrayList()
    var lastList: MutableList<String> = ArrayList()
    var columns: MutableList<String> = ArrayList()
    var values: MutableList<String> = ArrayList()
    var distinct = false
    private fun sqlClause(builder: Appendable, keyword: String, parts: List<String>,
                          open: String, close: String, conjunction: String) {
        if (parts.isNullOrEmpty()) {
            return
        }
        builder.append(keyword).append(SPACE).append(open)
        var last = "________"
        for (i in parts.indices) {
            val part = parts[i]
            if (i > 0 && part != AND && part != OR && last != AND && last != OR) {
                builder.append(conjunction).append(SPACE)
            }
            builder.append(part)
            last = part
        }
        builder.append(close)
    }

    private fun selectSQL(builder: Appendable): String {
        sqlClause(builder, if (distinct) SELECT_DISTINCT else SELECT, select, EMPTY, EMPTY, COMMA)
        sqlClause(builder, FROM, tables, EMPTY, EMPTY, COMMA)
        joins(builder)
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND)
        sqlClause(builder, GROUP_BY, groupBy, EMPTY, EMPTY, COMMA)
        sqlClause(builder, HAVING, having, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND)
        sqlClause(builder, ORDER_BY, orderBy, EMPTY, EMPTY, COMMA)
        return builder.toString()
    }

    private fun countSQL(builder: Appendable): String {
        sqlClause(builder, SELECT, listOf("count(*)"), EMPTY, EMPTY, COMMA)
        sqlClause(builder, FROM, tables, EMPTY, EMPTY, COMMA)
        joins(builder)
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND)
        sqlClause(builder, GROUP_BY, groupBy, EMPTY, EMPTY, COMMA)
        sqlClause(builder, HAVING, having, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND)
        return builder.toString()
    }

    private fun joins(builder: Appendable) {
        sqlClause(builder, JOIN, join, EMPTY, EMPTY, JOIN)
        sqlClause(builder, INNER_JOIN, innerJoin, EMPTY, EMPTY, INNER_JOIN)
        sqlClause(builder, OUTER_JOIN, outerJoin, EMPTY, EMPTY, OUTER_JOIN)
        sqlClause(builder, LEFT_JOIN, leftOuterJoin, EMPTY, EMPTY, LEFT_JOIN)
        sqlClause(builder, RIGHT_JOIN, rightOuterJoin, EMPTY, EMPTY, RIGHT_JOIN)
    }

    private fun insertSQL(builder: Appendable): String {
        sqlClause(builder, INSERT_INTO, tables, EMPTY, EMPTY, EMPTY)
        sqlClause(builder, EMPTY, columns, LEFT_BRACKET, RIGHT_BRACKET, COMMA)
        sqlClause(builder, VALUES, values, LEFT_BRACKET, RIGHT_BRACKET, COMMA)
        return builder.toString()
    }

    private fun deleteSQL(builder: Appendable): String {
        sqlClause(builder, DELETE_FROM, tables, EMPTY, EMPTY, EMPTY)
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND)
        return builder.toString()
    }

    private fun updateSQL(builder: Appendable): String {
        sqlClause(builder, UPDATE, tables, EMPTY, EMPTY, EMPTY)
        joins(builder)
        sqlClause(builder, SET, sets, EMPTY, EMPTY, COMMA)
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND)
        return builder.toString()
    }

    fun sql(a: Appendable): String {
        if (statementType == null) {
            return EMPTY
        }
        return when (statementType) {
            StatementType.DELETE -> deleteSQL(a)
            StatementType.INSERT -> insertSQL(a)
            StatementType.SELECT -> selectSQL(a)
            StatementType.COUNT -> countSQL(a)
            StatementType.UPDATE -> updateSQL(a)
            else -> EMPTY
        }
    }

    fun sqlCount(): String {
        return countSQL(StringBuilder())
    }
}