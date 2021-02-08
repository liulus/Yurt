package com.github.liulus.yurt.convention.data;

import java.util.List;
import java.util.function.Function;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public interface Page<T> {

    /**
     * 获取页号
     *
     * @return 页号
     */
    int getPageNum();

    /**
     * 获取每页可显示的记录数
     *
     * @return 每页可显示的记录数
     */
    int getPageSize();

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    List<T> getResults();

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    long getTotalRecords();

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    int getTotalPages();

    /**
     * 映射成另外的一种分页对象
     *
     * @param transformer 转换对象的函数接口
     * @param <R>         另外的数据类型
     * @return 分页对象 R
     */
    <R> Page<T> map(Function<T, R> transformer);
}
