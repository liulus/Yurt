package com.github.liulus.yurt.jdbc.annotation

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
@Target(AnnotationTarget.FUNCTION)
@Retention
annotation class Select(
        val value: String = "",
        val distinct: Boolean = false,
        val columns: Array<String> = [],
        val from: String = "",
        val join: String = "",
        val innerJoin: String = "",
        val outerJoin: String = "",
        val leftJoin: String = "",
        val rightJoin: String = "",
        val where: Array<String> = [],
        val testWheres: Array<If> = [],
        val orderBy: String = "",
        val groupBy: String = "",
        val having: Array<String> = [],
        val provider: String = "",
)

