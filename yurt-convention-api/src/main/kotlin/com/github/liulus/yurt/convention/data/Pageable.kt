package com.github.liulus.yurt.convention.data

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/29
 */
interface Pageable {

    /**
     * 页码
     */
    val pageNum: Int

    /**
     * 每页显示记录数
     */
    val pageSize: Int

    /**
     * 偏移量
     */
    val offset: Int

    /**
     * 是否查询count
     */
    val isCount: Boolean
        get() = true


}