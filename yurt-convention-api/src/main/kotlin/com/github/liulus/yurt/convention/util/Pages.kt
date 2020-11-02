package com.github.liulus.yurt.convention.util

import com.github.liulus.yurt.convention.data.DefaultPage
import com.github.liulus.yurt.convention.data.Page
import com.github.liulus.yurt.convention.data.PageList
import com.github.liulus.yurt.convention.data.Pageable

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/29
 */
object Pages {

    @JvmField
    val EMPTY: Page<Any> = DefaultPage()

    @JvmStatic
    fun <T> page(results: List<T>): Page<T> {
        if (results is PageList) {
            return DefaultPage(results.pageNum, results.pageSize, results, results.totalRecords)
        }
        return DefaultPage(results)
    }

    @JvmStatic
    fun <T> page(pageable: Pageable, results: List<T>, totalRecords: Int): Page<T> {
        return DefaultPage(pageable, results, totalRecords)
    }

    @JvmStatic
    fun <T, S> page(pageable: Pageable, results: List<T>, totalCount: Int, transformer: (T) -> S): Page<S>? {
        return page(pageable, results.asSequence().map(transformer).toList(), totalCount)
    }

    @JvmStatic
    fun <T> page(pageNum: Int, pageSize: Int, results: List<T>, totalRecords: Int): Page<T> {
        return DefaultPage(pageNum, pageSize, results, totalRecords)
    }

    @JvmStatic
    fun <T, S> page(pageNum: Int, pageSize: Int, results: List<T>, totalRecords: Int, transformer: (T) -> S): Page<S> {
        return DefaultPage(pageNum, pageSize, results.asSequence().map(transformer).toList(), totalRecords)
    }

}