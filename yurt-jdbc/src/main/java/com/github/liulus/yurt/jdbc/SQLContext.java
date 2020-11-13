package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.convention.cache.LRUCache;
import com.github.liulus.yurt.convention.data.Page;
import com.github.liulus.yurt.convention.data.PageList;
import com.github.liulus.yurt.convention.data.Pageable;
import com.github.liulus.yurt.convention.util.Asserts;
import com.github.liulus.yurt.convention.util.Pages;
import com.github.liulus.yurt.convention.util.SpelUtils;
import com.github.liulus.yurt.jdbc.annotation.Delete;
import com.github.liulus.yurt.jdbc.annotation.If;
import com.github.liulus.yurt.jdbc.annotation.Param;
import com.github.liulus.yurt.jdbc.annotation.Select;
import com.github.liulus.yurt.jdbc.annotation.Update;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
class SQLContext {

    private static final Map<String, SQLContext> CONTEXT_CACHE = new LRUCache<>();

    private final Class<?> interfaceClass;
    private Class<?> entityClass;
    private final Class<?> returnType;
    private Class<?> genericReturnType;
    private final Method method;

    private SQLStatement.StatementType statementType;

    private Select select;
    private Update update;
    private Delete delete;
    private final boolean isReturnPage;
    private final boolean isReturnCollection;
    private final boolean isReturnSet;

    public SQLContext(Class<?> interfaceClass, Method method) {
        this.interfaceClass = interfaceClass;
        this.method = method;
        for (Type genericInterface : interfaceClass.getGenericInterfaces()) {
            ResolvableType parentType = ResolvableType.forType(genericInterface);
            if (parentType.getRawClass() == JdbcRepository.class) {
                entityClass = parentType.getGeneric(0).getRawClass();
                break;
            }
        }
        returnType = method.getReturnType();
        isReturnPage = Page.class.isAssignableFrom(returnType);
        isReturnCollection = Collection.class.isAssignableFrom(returnType);
        isReturnSet = Set.class.isAssignableFrom(returnType);
        if (isReturnPage || isReturnCollection) {
            genericReturnType = ResolvableType.forType(method.getGenericReturnType())
                    .getGeneric(0).getRawClass();
        }
        if (method.isAnnotationPresent(Select.class)) {
            statementType = SQLStatement.StatementType.SELECT;
            select = AnnotationUtils.findAnnotation(method, Select.class);
        } else if (method.isAnnotationPresent(Update.class)) {
            statementType = SQLStatement.StatementType.UPDATE;
            update = AnnotationUtils.findAnnotation(method, Update.class);
        } else if (method.isAnnotationPresent(Delete.class)) {
            statementType = SQLStatement.StatementType.DELETE;
            delete = AnnotationUtils.findAnnotation(method, Delete.class);
        }
    }

    public static SQLContext getContext(Class<?> interfaceClass, Method method) {
        synchronized (CONTEXT_CACHE) {
            String key = interfaceClass.getName() + "-" + method.getName();
            return CONTEXT_CACHE.computeIfAbsent(key, k -> new SQLContext(interfaceClass, method));
        }
    }

    public Object execute(SQLExecutor sqlExecutor, Object[] params) {
        Object namedParams = getNamedParams(params);
        switch (statementType) {
            case SELECT:
                return executeSelect(sqlExecutor, namedParams);
            case UPDATE:
                return executeUpdate(sqlExecutor, namedParams);
            case DELETE:
                return executeDelete(sqlExecutor, namedParams);
            default:
                return null;
        }
    }


    private Object executeSelect(SQLExecutor sqlExecutor, Object params) {
        TableMetadata metadata = TableMetadata.forClass(entityClass);
        SQL sql = new SQL();
        String columns = select.columns().length == 0 ? metadata.getColumns() :
                StringUtils.arrayToCommaDelimitedString(select.columns());
        sql = select.distinct() ? sql.SELECT_DISTINCT(columns) : sql.SELECT(columns);
        sql.FROM(StringUtils.isEmpty(select.from()) ? metadata.getTableName() : select.from());
        Optional.of(select.join()).filter(StringUtils::hasText).ifPresent(sql::JOIN);
        Optional.of(select.innerJoin()).filter(StringUtils::hasText).ifPresent(sql::INNER_JOIN);
        Optional.of(select.outerJoin()).filter(StringUtils::hasText).ifPresent(sql::OUTER_JOIN);
        Optional.of(select.leftJoin()).filter(StringUtils::hasText).ifPresent(sql::LEFT_OUTER_JOIN);
        Optional.of(select.rightJoin()).filter(StringUtils::hasText).ifPresent(sql::RIGHT_OUTER_JOIN);
        Optional.of(select.orderBy()).filter(StringUtils::hasText).ifPresent(sql::ORDER_BY);
        Optional.of(select.groupBy()).filter(StringUtils::hasText).ifPresent(sql::GROUP_BY);

        Optional.of(select.where()).filter(a -> a.length > 0).ifPresent(sql::WHERE);
        Optional.of(select.having()).filter(a -> a.length > 0).ifPresent(sql::HAVING);

        Optional.of(evaluateTests(select.testWheres(), params))
                .filter(a -> a.length > 0).ifPresent(sql::WHERE);
        if (select.isPageQuery()) {
            Asserts.notNull(params, "分页查询参数不能为空");
            Asserts.notNull(genericReturnType, "分页查询 的泛型不能为空");
            Asserts.isTrue(!isReturnSet && (isReturnPage || isReturnCollection),
                    "分页查询只支持返回List, Collection, Page");
            Pageable pageParam = getPageParam(params);
            Asserts.notNull(pageParam, "分页查询参数Pageable不存在");
            long count = sqlExecutor.count(sql, params);
            if (count == 0L) {
                return Optional.of(isReturnPage).filter(Boolean::valueOf)
                        .map((Function<Boolean, Object>) b -> Pages.EMPTY)
                        .orElse(Collections.emptyList());
            }
            int pageNum = pageParam.getPageNum();
            int pageSize = pageParam.getPageSize();
            List<?> pageResult = sqlExecutor.selectForPage(sql, params, pageNum, pageSize, genericReturnType);

            return isReturnPage ? Pages.page(pageNum, pageSize, pageResult, count)
                    : new PageList<>(pageNum, pageSize, pageResult, count);
        }
        if (isReturnCollection) {
            Asserts.notNull(genericReturnType, "集合 的泛型不能为空");
            List<?> result = sqlExecutor.selectForList(sql, params, genericReturnType);
            return isReturnSet ? new HashSet<>(result) : result;
        }
        return sqlExecutor.selectForObject(sql, params, returnType);
    }

    private Pageable getPageParam(Object params) {
        Pageable pageParam = null;
        if (StringUtils.isEmpty(select.pageParam())) {
            if (params instanceof Pageable) {
                pageParam = (Pageable) params;
            } else if (params instanceof Map) {
                pageParam = (Pageable) ((Map<?, ?>) params).values()
                        .stream().filter(v -> v instanceof Pageable)
                        .findFirst().orElse(null);
            }
        } else if (params instanceof Map) {
            pageParam = (Pageable) ((Map<?, ?>) params).get(select.pageParam());
        }
        return pageParam;
    }


    private int executeUpdate(SQLExecutor sqlExecutor, Object params) {
        TableMetadata metadata = TableMetadata.forClass(entityClass);
        String table = StringUtils.isEmpty(update.table()) ? metadata.getTableName() : update.table();
        SQL sql = new SQL().UPDATE(table);
        Optional.of(update.sets()).filter(a -> a.length > 0).ifPresent(sql::SET);
        Optional.of(update.where()).filter(a -> a.length > 0).ifPresent(sql::WHERE);

        Optional.of(evaluateTests(update.testSets(), params))
                .filter(a -> a.length > 0).ifPresent(sql::SET);
        Optional.of(evaluateTests(update.testWheres(), params))
                .filter(a -> a.length > 0).ifPresent(sql::WHERE);
        return sqlExecutor.update(sql, params);
    }

    private int executeDelete(SQLExecutor sqlExecutor, Object params) {
        TableMetadata metadata = TableMetadata.forClass(entityClass);
        String from = StringUtils.isEmpty(delete.from()) ? metadata.getTableName() : delete.from();
        SQL sql = new SQL().DELETE_FROM(from);
        Optional.of(delete.where()).filter(a -> a.length > 0).ifPresent(sql::WHERE);

        Optional.of(evaluateTests(delete.testWheres(), params))
                .filter(a -> a.length > 0).ifPresent(sql::WHERE);
        return sqlExecutor.update(sql, params);
    }


    private String[] evaluateTests(If[] tests, Object root) {
        if (tests == null || tests.length == 0) {
            return new String[0];
        }
        return Stream.of(tests)
                .filter(test -> SpelUtils.getValue(test.test(), root, boolean.class))
                .map(If::value)
                .toArray(String[]::new);
    }

    private Object getNamedParams(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        int i = 0;

        for (Parameter parameter : parameters) {
            Param param = AnnotationUtils.findAnnotation(parameter, Param.class);
            Object arg = args[i];
            result.put("param" + i++, arg);
            if (param != null) {
                result.put(param.value(), arg);
            }
        }
        if (result.size() == 1) {
            return args[0];
        }
        return result;
    }


}
