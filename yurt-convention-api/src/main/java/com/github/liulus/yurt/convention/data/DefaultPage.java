package com.github.liulus.yurt.convention.data;

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
    private List<? extends T> results;
    private long totalRecords;

    public DefaultPage(int pageNum, int pageSize, List<? extends T> results, long totalRecords) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.results = results;
        this.totalRecords = totalRecords;
    }

    public DefaultPage() {
        this(1, 20, Collections.emptyList(), 0);
    }

    public DefaultPage(List<? extends T> results) {
        this(1, 20, results, results.size());
    }

    public DefaultPage(Pageable pageable, List<? extends T> results, int totalRecords) {
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

    @Override
    public List<? extends T> getResults() {
        return results == null ? Collections.emptyList() : results;
    }

    public void setResults(List<? extends T> results) {
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

    @Override
    public <R> Page<T> map(Function<T, R> transformer) {
        return new DefaultPage(pageNum, pageSize,
                results.stream().map(transformer).collect(Collectors.toList()), totalRecords);
    }
}
