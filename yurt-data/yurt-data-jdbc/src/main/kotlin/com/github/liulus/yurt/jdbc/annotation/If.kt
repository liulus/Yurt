package com.github.liulus.yurt.jdbc.annotation

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
@Retention
@Target(AnnotationTarget.TYPE)
annotation class If(
        val value: String,
        val test: String = "",
)