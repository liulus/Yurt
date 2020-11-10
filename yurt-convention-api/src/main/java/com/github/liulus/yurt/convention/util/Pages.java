package com.github.liulus.yurt.convention.util;

import com.github.liulus.yurt.convention.data.DefaultPage;
import com.github.liulus.yurt.convention.data.Page;
import com.github.liulus.yurt.convention.data.Pageable;

import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public abstract class Pages {

    public static final Page<?> EMPTY = new DefaultPage<>();

    public static <T> Page<T> page(List<T> results) {
        return new DefaultPage<>(results);
    }

    public static <T> Page<T> page(Pageable pageable, List<T> results, long totalRecords) {
        return new DefaultPage<>(pageable.getPageNum(), pageable.getPageSize(), results, totalRecords);
    }

    public static <T> Page<T> page(int pageNum, int pageSize, List<T> results, long totalRecords) {
        return new DefaultPage<>(pageNum, pageSize, results, totalRecords);
    }


}
