package com.github.liulus.yurt.convention.page

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

}