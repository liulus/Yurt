package com.github.liulus.yurt.jdbc

import com.github.liulus.yurt.convention.util.Asserts
import com.github.liulus.yurt.convention.util.SpelUtils
import com.github.liulus.yurt.jdbc.annotation.Delete
import com.github.liulus.yurt.jdbc.annotation.If
import com.github.liulus.yurt.jdbc.annotation.Param
import com.github.liulus.yurt.jdbc.annotation.Select
import com.github.liulus.yurt.jdbc.annotation.Update
import org.springframework.core.ResolvableType
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import java.util.Optional

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/5
 */
internal class SQLContext private constructor(
        interfaceClass: Class<*>,
        val method: Method
) {

    companion object {

        @JvmStatic
        private val CONTEXT_CACHE: MutableMap<String, SQLContext> = object :
                LinkedHashMap<String, SQLContext>(64, 0.75f, true) {
            override fun removeEldestEntry(eldest: Map.Entry<String, SQLContext>): Boolean {
                return size > 128
            }
        }

        @JvmStatic
        fun getContext(interfaceClass: Class<*>, method: Method): SQLContext {
            synchronized(CONTEXT_CACHE) {
                val key = "${interfaceClass.name}-${method.name}"
                return CONTEXT_CACHE.computeIfAbsent(key) { SQLContext(interfaceClass, method) }
            }
        }
    }

    private lateinit var statementType: SQLStatement.StatementType
    private lateinit var entityClass: Class<*>

    private var select: Select? = null
    private var update: Update? = null
    private var delete: Delete? = null


    init {
        for (parent in interfaceClass.genericInterfaces) {
            val parentType = ResolvableType.forType(parent)
            if (parentType.rawClass == JdbcRepository::class.java) {
                entityClass = parentType.getGeneric(0).rawClass!!
                break
            }
        }
        when {
            method.isAnnotationPresent(Select::class.java) -> {
                statementType = SQLStatement.StatementType.SELECT
                select = AnnotationUtils.findAnnotation(method, Select::class.java)
            }
            method.isAnnotationPresent(Update::class.java) -> {
                statementType = SQLStatement.StatementType.UPDATE
                update = AnnotationUtils.findAnnotation(method, Update::class.java)
            }
            method.isAnnotationPresent(Delete::class.java) -> {
                statementType = SQLStatement.StatementType.DELETE
                delete = AnnotationUtils.findAnnotation(method, Delete::class.java)
            }
        }
    }

    fun execute(sqlExecutor: SQLExecutor, params: Array<Any>?): Any? {
        val namedParams = getNamedParams(params)
        return when (statementType) {
            SQLStatement.StatementType.SELECT -> executeSelect(sqlExecutor, namedParams)
            SQLStatement.StatementType.UPDATE -> executeUpdate(sqlExecutor, namedParams)
            SQLStatement.StatementType.DELETE -> executeDelete(sqlExecutor, namedParams)
            else -> null
        }
    }

    private fun executeSelect(sqlExecutor: SQLExecutor, params: Any?): Any? {
        val select = this.select!!
        val metadata = TableMetadata.forClass(entityClass)
        val sql = SQL()
        val columns = if (select.columns.isNullOrEmpty()) metadata.columns else select.columns.joinToString()
        if (select.distinct) sql.SELECT_DISTINCT(columns) else sql.SELECT(columns)
        sql.FROM(if (select.from.isEmpty()) metadata.tableName else select.from)

        Optional.of(select.join).filter { it.isNotEmpty() }.ifPresent { sql.JOIN(it) }
        Optional.of(select.innerJoin).filter { it.isNotEmpty() }.ifPresent { sql.INNER_JOIN(it) }
        Optional.of(select.outerJoin).filter { it.isNotEmpty() }.ifPresent { sql.OUTER_JOIN(it) }
        Optional.of(select.leftJoin).filter { it.isNotEmpty() }.ifPresent { sql.LEFT_OUTER_JOIN(it) }
        Optional.of(select.rightJoin).filter { it.isNotEmpty() }.ifPresent { sql.RIGHT_OUTER_JOIN(it) }
        Optional.of(select.orderBy).filter { it.isNotEmpty() }.ifPresent { sql.ORDER_BY(it) }
        Optional.of(select.groupBy).filter { it.isNotEmpty() }.ifPresent { sql.GROUP_BY(it) }

        Optional.of(select.where).filter { it.isNotEmpty() }.ifPresent { sql.WHERE(*it) }
        Optional.of(select.having).filter { it.isNotEmpty() }.ifPresent { sql.HAVING(*it) }

        Optional.of(evaluateTests(select.testWheres, params))
                .filter { it.isNotEmpty() }.ifPresent { sql.WHERE(*it) }

        val returnType = method.returnType
        if (Collection::class.java.isAssignableFrom(returnType)) {
            val requiredType = ResolvableType.forType(method.genericReturnType)
                    .getGeneric(0).rawClass
            Asserts.notNull(requiredType, "集合 的泛型不能为空")
            val result = sqlExecutor.selectForList(sql, params, requiredType!!)
            return if (Set::class.java.isAssignableFrom(requiredType)) HashSet(result) else result
        }
        return sqlExecutor.selectForObject(sql, params, returnType)
    }

    private fun executeUpdate(sqlExecutor: SQLExecutor, params: Any?): Int {
        val update = this.update!!
        val metadata = TableMetadata.forClass(entityClass)
        val sql = SQL().UPDATE(if (update.table.isEmpty()) metadata.tableName else update.table)
        Optional.of(update.sets).filter { it.isNotEmpty() }.ifPresent { sql.SET(*it) }
        Optional.of(update.where).filter { it.isNotEmpty() }.ifPresent { sql.WHERE(*it) }

        Optional.of(evaluateTests(update.testSets, params))
                .filter { it.isNotEmpty() }.ifPresent { sql.SET(*it) }
        Optional.of(evaluateTests(update.testWheres, params))
                .filter { it.isNotEmpty() }.ifPresent { sql.WHERE(*it) }

        return sqlExecutor.update(sql, params)
    }

    private fun executeDelete(sqlExecutor: SQLExecutor, params: Any?): Int {
        val delete = this.delete!!
        val metadata = TableMetadata.forClass(entityClass)
        val sql = SQL().UPDATE(if (delete.from.isEmpty()) metadata.tableName else delete.from)
        Optional.of(delete.where).filter { it.isNotEmpty() }.ifPresent { sql.WHERE(*it) }

        Optional.of(evaluateTests(delete.testWheres, params))
                .filter { it.isNotEmpty() }.ifPresent { sql.WHERE(*it) }
        return sqlExecutor.update(sql, params)
    }


    private fun evaluateTests(tests: Array<If>, root: Any?): Array<String> {
        if (tests.isNullOrEmpty()) {
            return emptyArray()
        }
        return tests.filter { SpelUtils.getValue(it.test, root, Boolean::class.java) ?: false }
                .map { it.value }
                .toTypedArray()
    }

    private fun getNamedParams(args: Array<Any>?): Any? {
        if (args.isNullOrEmpty()) {
            return null
        }
        val result: MutableMap<String, Any> = HashMap()
        val params = method.parameters
        for (i in params.indices) {
            val parameter = params[i]
            val param = AnnotationUtils.findAnnotation(parameter, Param::class.java)
            result["param$i"] = args[i]
            if (param != null) {
                result[param.value] = args[i]
            }
        }
        if (result.size == 1) {
            return args[0]
        }
        return result
    }


}



