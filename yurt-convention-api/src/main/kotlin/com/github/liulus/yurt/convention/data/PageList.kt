package com.github.liulus.yurt.convention.data


/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
class PageList<E> : ArrayList<E> {

    var pageNum: Int = 1
    var pageSize: Int = 20
    var totalRecords: Int = 0

    constructor() : super()
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor(c: Collection<E>) : super(c)

}


