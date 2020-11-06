package com.github.liulus.yurt.convention.util

import org.springframework.context.expression.MapAccessor
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/5
 */
object SpelUtils {

    @JvmStatic
    private val expressionParser = SpelExpressionParser()

    @JvmStatic
    private val mapAccessor = MapAccessor()

    @JvmStatic
    fun getValue(expression: String, root: Any?): Any? {
        if (root == null) {
            return null
        }
        val context = StandardEvaluationContext()
        context.setRootObject(root)
        if (root is Map<*, *>) {
            context.addPropertyAccessor(mapAccessor)
        }
        return expressionParser.parseExpression(expression).getValue(context)
    }

    @JvmStatic
    fun <T> getValue(expression: String, root: Any?, resultClass: Class<T>): T? {
        if (root == null) {
            return null
        }
        val context = StandardEvaluationContext()
        context.setRootObject(root)
        if (root is Map<*, *>) {
            context.addPropertyAccessor(mapAccessor)
        }
        return expressionParser.parseExpression(expression).getValue(context, resultClass)
    }


}