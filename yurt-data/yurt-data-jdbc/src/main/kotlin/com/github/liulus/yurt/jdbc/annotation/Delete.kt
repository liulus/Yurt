package com.github.liulus.yurt.jdbc.annotation

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
@Target(AnnotationTarget.FUNCTION)
@Retention
annotation class Delete(
        val value: String = "",
        val from: String = "",
        val where: Array<String> = [],
        val testWheres: Array<If> = [],
)