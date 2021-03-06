package com.github.liulus.yurt.convention.data;

import com.github.liulus.yurt.convention.util.Pages;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class DefaultPage<T> implements Page<T> {

    private int pageNum;
    private int pageSize;
    private List<T> results;
    private long totalRecords;

    public DefaultPage(int pageNum, int pageSize, List<T> results, long totalRecords) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.results = results;
        this.totalRecords = totalRecords;
    }

    public DefaultPage() {
        this(1, 20, Collections.emptyList(), 0);
    }

    public DefaultPage(List<T> results) {
        this(1, 20, results, results.size());
    }

    public DefaultPage(Pageable pageable, List<T> results, int totalRecords) {
        this(pageable.getPageNum(), pageable.getPageSize(), results, totalRecords);
    }


    @Override
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @NotNull
    @Override
    public List<T> getResults() {
        return results == null ? Collections.emptyList() : results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    @Override
    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public int getTotalPages() {
        return (int) ((totalRecords - 1) / pageSize + 1);
    }

    @NotNull
    @Override
    public <R> Page<R> simpleMap(Function<T, R> transformer) {
        return new DefaultPage<>(pageNum, pageSize,
                results.stream().map(transformer).collect(Collectors.toList()), totalRecords);
    }

    @NotNull
    @Override
    public <R> Page<R> transform(List<R> data) {
        if (CollectionUtils.isEmpty(data)) {
            //noinspection unchecked
            return (Page<R>) Pages.EMPTY;
        }
        return new DefaultPage<>(pageNum, pageSize, data, totalRecords);
    }
}
