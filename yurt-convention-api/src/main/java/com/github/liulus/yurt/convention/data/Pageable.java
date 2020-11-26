package com.github.liulus.yurt.convention.data;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public interface Pageable {

    /**
     * 页码
     */
    int getPageNum();

    /**
     * 每页显示记录数
     */
    int getPageSize();

    /**
     * 偏移量
     */
    int getOffset();

    /**
     * 是否关闭分页查询
     */
    boolean isDisablePage();

    /**
     * 是否查询count
     */
    default boolean isCount() {
        return true;
    }

}
