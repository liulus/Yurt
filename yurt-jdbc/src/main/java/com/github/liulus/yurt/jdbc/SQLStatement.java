package com.github.liulus.yurt.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
class SQLStatement {

    public static final String INSERT_INTO = "INSERT INTO";
    public static final String VALUES = " VALUES";
    public static final String UPDATE = "UPDATE";
    public static final String SET = " SET";
    public static final String DELETE_FROM = "DELETE FROM";
    public static final String SELECT = "SELECT";
    public static final String SELECT_DISTINCT = "SELECT DISTINCT";
    public static final String FROM = " FROM";
    public static final String JOIN = " JOIN";
    public static final String INNER_JOIN = " INNER" + JOIN;
    public static final String OUTER_JOIN = " OUTER" + JOIN;
    public static final String LEFT_JOIN = " LEFT" + JOIN;
    public static final String RIGHT_JOIN = " RIGHT" + JOIN;
    public static final String WHERE = " WHERE";
    public static final String ORDER_BY = " ORDER BY";
    public static final String GROUP_BY = " GROUP BY";
    public static final String HAVING = " HAVING";
    public static final String AND = ") AND (";
    public static final String OR = ") OR (";
    public static final String SPACE = " ";
    public static final String SPACE_AND = " AND";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";
    private static final String EMPTY = "";
    private static final String COMMA = ",";

    public enum StatementType {
        DELETE, INSERT, SELECT, UPDATE, COUNT
    }


    public StatementType statementType;
    public List<String> sets = new ArrayList<>();
    public List<String> select = new ArrayList<>();
    public List<String> tables = new ArrayList<>();
    public List<String> join = new ArrayList<>();
    public List<String> innerJoin = new ArrayList<>();
    public List<String> outerJoin = new ArrayList<>();
    public List<String> leftOuterJoin = new ArrayList<>();
    public List<String> rightOuterJoin = new ArrayList<>();
    public List<String> where = new ArrayList<>();
    public List<String> having = new ArrayList<>();
    public List<String> groupBy = new ArrayList<>();
    public List<String> orderBy = new ArrayList<>();
    public List<String> lastList = new ArrayList<>();
    public List<String> columns = new ArrayList<>();
    public List<String> values = new ArrayList<>();
    public boolean distinct;

    private void sqlClause(StringBuilder builder, String keyword, List<String> parts,
                           String open, String close, String conjunction) {
        if (parts == null || parts.isEmpty()) {
            return;
        }
        builder.append(keyword).append(SPACE).append(open);
        String last = "________";
        int size = parts.size();
        for (int i = 0; i < size; i++) {
            String part = parts.get(i);
            if (i > 0 && !part.equals(AND) && !part.equals(OR) && !last.equals(AND) && !last.equals(OR)) {
                builder.append(conjunction).append(SPACE);
            }
            builder.append(part);
            last = part;
        }
        builder.append(close);
    }

    private String selectSQL(StringBuilder builder) {
        sqlClause(builder, distinct ? SELECT_DISTINCT : SELECT, select, EMPTY, EMPTY, COMMA);
        sqlClause(builder, FROM, tables, EMPTY, EMPTY, COMMA);
        joins(builder);
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND);
        sqlClause(builder, GROUP_BY, groupBy, EMPTY, EMPTY, COMMA);
        sqlClause(builder, HAVING, having, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND);
        sqlClause(builder, ORDER_BY, orderBy, EMPTY, EMPTY, COMMA);
        return builder.toString();
    }

    private String countSQL(StringBuilder builder) {
        if (distinct) {
            sqlClause(builder, SELECT + " COUNT(DISTINCT", select, EMPTY, EMPTY, COMMA);
            builder.append(RIGHT_BRACKET);
        } else {
            sqlClause(builder, SELECT, Collections.singletonList("COUNT(*)"), EMPTY, EMPTY, COMMA);
        }
        sqlClause(builder, FROM, tables, EMPTY, EMPTY, COMMA);
        joins(builder);
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND);
        sqlClause(builder, GROUP_BY, groupBy, EMPTY, EMPTY, COMMA);
        sqlClause(builder, HAVING, having, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND);
        return builder.toString();
    }

    private void joins(StringBuilder builder) {
        sqlClause(builder, JOIN, join, EMPTY, EMPTY, JOIN);
        sqlClause(builder, INNER_JOIN, innerJoin, EMPTY, EMPTY, INNER_JOIN);
        sqlClause(builder, OUTER_JOIN, outerJoin, EMPTY, EMPTY, OUTER_JOIN);
        sqlClause(builder, LEFT_JOIN, leftOuterJoin, EMPTY, EMPTY, LEFT_JOIN);
        sqlClause(builder, RIGHT_JOIN, rightOuterJoin, EMPTY, EMPTY, RIGHT_JOIN);
    }

    private String insertSQL(StringBuilder builder) {
        sqlClause(builder, INSERT_INTO, tables, EMPTY, EMPTY, EMPTY);
        sqlClause(builder, EMPTY, columns, LEFT_BRACKET, RIGHT_BRACKET, COMMA);
        sqlClause(builder, VALUES, values, LEFT_BRACKET, RIGHT_BRACKET, COMMA);
        return builder.toString();
    }

    private String deleteSQL(StringBuilder builder) {
        sqlClause(builder, DELETE_FROM, tables, EMPTY, EMPTY, EMPTY);
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND);
        return builder.toString();
    }

    private String updateSQL(StringBuilder builder) {
        sqlClause(builder, UPDATE, tables, EMPTY, EMPTY, EMPTY);
        joins(builder);
        sqlClause(builder, SET, sets, EMPTY, EMPTY, COMMA);
        sqlClause(builder, WHERE, where, LEFT_BRACKET, RIGHT_BRACKET, SPACE_AND);
        return builder.toString();
    }

    String sql(StringBuilder a) {
        if (statementType == null) {
            return EMPTY;
        }
        switch (statementType) {
            case INSERT:
                return insertSQL(a);
            case DELETE:
                return deleteSQL(a);
            case UPDATE:
                return updateSQL(a);
            case SELECT:
                return selectSQL(a);
            case COUNT:
                return countSQL(a);
        }
        return EMPTY;
    }


}
