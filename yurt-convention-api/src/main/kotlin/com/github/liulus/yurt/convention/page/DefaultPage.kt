package com.github.liulus.yurt.convention.page

import kotlin.math.max


/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/29
 */
data class DefaultPage<T>(
        override var pageNum: Int,
        override var pageSize: Int,
        override var results: List<T>,
        override var totalRecords: Int,
) : Page<T> {

    init {
        totalRecords = max(totalRecords, results.size)
    }

    constructor() : this(PageQuery.DEFAULT_PAGE_NUM, PageQuery.DEFAULT_PAGE_SIZE, emptyList(), 0)

    constructor(pageable: Pageable, results: List<T>, totalRecords: Int)
            : this(pageable.pageNum, pageable.pageSize, results, totalRecords)


    override val totalPages: Int
        get() = (totalRecords - 1) / pageSize + 1


}