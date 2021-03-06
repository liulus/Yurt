package com.github.liulus.yurt.convention.util;

import com.github.liulus.yurt.convention.data.DefaultPage;
import com.github.liulus.yurt.convention.data.Page;
import com.github.liulus.yurt.convention.data.PageList;
import com.github.liulus.yurt.convention.data.Pageable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public abstract class Pages {

    @NotNull
    public static final Page<?> EMPTY = new DefaultPage<>();

    @NotNull
    public static <T> Page<T> page(@NotNull List<T> results) {
        if (results instanceof PageList) {
            PageList<T> ts = (PageList<T>) results;
            return page(ts.getPageNum(), ts.getPageSize(), ts, ts.getTotalRecords());
        }


        return new DefaultPage<>(results);
    }

    @NotNull
    public static <T> Page<T> page(Pageable pageable, List<T> results, long totalRecords) {
        return new DefaultPage<>(pageable.getPageNum(), pageable.getPageSize(), results, totalRecords);
    }

    @NotNull
    public static <T> Page<T> page(int pageNum, int pageSize, List<T> results, long totalRecords) {
        return new DefaultPage<>(pageNum, pageSize, results, totalRecords);
    }


}
