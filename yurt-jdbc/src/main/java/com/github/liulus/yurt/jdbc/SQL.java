package com.github.liulus.yurt.jdbc;

import java.util.Arrays;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class SQL {
    private final SQLStatement sql = new SQLStatement();


    public SQL UPDATE(String table) {
        sql.statementType = SQLStatement.StatementType.UPDATE;
        sql.tables.add(table);
        return this;
    }

    public SQL SET(String sets) {
        sql.sets.add(sets);
        return this;
    }

    public SQL SET(String... sets) {
        sql.sets.addAll(Arrays.asList(sets));
        return this;
    }

    public SQL INSERT_INTO(String tableName) {
        sql.statementType = SQLStatement.StatementType.INSERT;
        sql.tables.add(tableName);
        return this;
    }

    public SQL VALUES(String columns, String values) {
        sql.columns.add(columns);
        sql.values.add(values);
        return this;
    }

    public SQL INTO_COLUMNS(String... columns) {
        sql.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public SQL INTO_VALUES(String... values) {
        sql.values.addAll(Arrays.asList(values));
        return this;
    }

    public SQL SELECT(String columns) {
        sql.statementType = SQLStatement.StatementType.SELECT;
        sql.select.add(columns);
        return this;
    }

    public SQL SELECT(String... columns) {
        sql.statementType = SQLStatement.StatementType.SELECT;
        sql.select.addAll(Arrays.asList(columns));
        return this;
    }

    public SQL SELECT_DISTINCT(String columns) {
        sql.distinct = true;
        SELECT(columns);
        return this;
    }

    public SQL SELECT_DISTINCT(String... columns) {
        sql.distinct = true;
        SELECT(columns);
        return this;
    }

    public SQL DELETE_FROM(String table) {
        sql.statementType = SQLStatement.StatementType.DELETE;
        sql.tables.add(table);
        return this;
    }

    public SQL FROM(String table) {
        sql.tables.add(table);
        return this;
    }

    public SQL FROM(String... tables) {
        sql.tables.addAll(Arrays.asList(tables));
        return this;
    }

    public SQL JOIN(String join) {
        sql.join.add(join);
        return this;
    }

    public SQL JOIN(String... joins) {
        sql.join.addAll(Arrays.asList(joins));
        return this;
    }

    public SQL INNER_JOIN(String join) {
        sql.innerJoin.add(join);
        return this;
    }

    public SQL INNER_JOIN(String... joins) {
        sql.innerJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public SQL LEFT_OUTER_JOIN(String join) {
        sql.leftOuterJoin.add(join);
        return this;
    }

    public SQL LEFT_OUTER_JOIN(String... joins) {
        sql.leftOuterJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public SQL RIGHT_OUTER_JOIN(String join) {
        sql.rightOuterJoin.add(join);
        return this;
    }

    public SQL RIGHT_OUTER_JOIN(String... joins) {
        sql.rightOuterJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public SQL OUTER_JOIN(String join) {
        sql.outerJoin.add(join);
        return this;
    }

    public SQL OUTER_JOIN(String... joins) {
        sql.outerJoin.addAll(Arrays.asList(joins));
        return this;
    }

    public SQL WHERE(String conditions) {
        sql.where.add(conditions);
        sql.lastList = sql.where;
        return this;
    }

    public SQL WHERE(String... conditions) {
        sql.where.addAll(Arrays.asList(conditions));
        sql.lastList = sql.where;
        return this;
    }

    public SQL OR() {
        sql.lastList.add(SQLStatement.OR);
        return this;
    }

    public SQL AND() {
        sql.lastList.add(SQLStatement.AND);
        return this;
    }

    public SQL GROUP_BY(String columns) {
        sql.groupBy.add(columns);
        return this;
    }

    public SQL GROUP_BY(String... columns) {
        sql.groupBy.addAll(Arrays.asList(columns));
        return this;
    }

    public SQL HAVING(String conditions) {
        sql.having.add(conditions);
        sql.lastList = sql.having;
        return this;
    }

    public SQL HAVING(String... conditions) {
        sql.having.addAll(Arrays.asList(conditions));
        sql.lastList = sql.having;
        return this;
    }

    public SQL ORDER_BY(String columns) {
        sql.orderBy.add(columns);
        return this;
    }

    public SQL ORDER_BY(String... columns) {
        sql.orderBy.addAll(Arrays.asList(columns));
        return this;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sql.sql(sb);
    }

    public String toCountSql() {
        sql.statementType = SQLStatement.StatementType.COUNT;
        String result = toString();
        sql.statementType = SQLStatement.StatementType.SELECT;
        return result;
    }
}
