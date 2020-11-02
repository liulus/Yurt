package com.github.liulus.yurt.convention.data

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/29
 */
interface Page<T> {

    /**
     * 获取页号
     * @return 页号
     */
    val pageNum: Int

    /**
     * 获取每页可显示的记录数
     * @return 每页可显示的记录数
     */
    val pageSize: Int

    /**
     * 获取总记录数
     * @return 总记录数
     */
    val totalRecords: Int

    /**
     * 获取数据列表
     * @return 数据列表
     */
    val results: List<T>

    /**
     * 获取总页数
     * @return 总页数
     */
    val totalPages: Int

    /**
     * 映射成另外的一种分页对象
     * @param transformer 转换对象的函数接口
     * @param <S> 另外的数据类型
     * @return 分页对象 S
     */
    fun <S> map(transformer: (T) -> S): Page<S>

}