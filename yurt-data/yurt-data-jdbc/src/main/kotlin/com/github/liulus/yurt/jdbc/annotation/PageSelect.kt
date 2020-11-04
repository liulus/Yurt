package com.github.liulus.yurt.jdbc.annotation

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
@Target(AnnotationTarget.FUNCTION)
@Retention
annotation class PageSelect(
        val select: Select,
        val pageParam: String,
)


