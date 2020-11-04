package com.github.liulus.yurt.jdbc.annotation

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
@Target(AnnotationTarget.FUNCTION)
@Retention
annotation class Update(
        val value: String = "",
        val table: String = "",
        val sets: Array<String> = [],
        val testSets: Array<If> = [],
        val where: Array<String> = [],
        val testWheres: Array<If> = [],
)