package com.github.liulus.yurt.convention.data

import kotlin.math.max
import kotlin.math.min

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/29
 */
class PageQuery(
        pageNum: Int = DEFAULT_PAGE_NUM,
        pageSize: Int = DEFAULT_PAGE_SIZE
) : Pageable {

    companion object {
        const val DEFAULT_PAGE_NUM = 1
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 200
    }

    override var pageNum: Int = pageNum
        set(value) {
            field = max(1, value)
        }

    override var pageSize: Int = pageSize
        set(value) {
            field = min(MAX_PAGE_SIZE, max(1, value))
        }

    override val offset: Int
        get() = pageSize * (pageNum - 1)

    override var isCount: Boolean = true


}