package com.github.liulus.yurt.convention.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class PageList<E> extends ArrayList<E> {

    private int pageNum;
    private int pageSize;
    private long totalRecords;

    public PageList() {
        this.pageNum = 1;
        this.pageSize = 20;
    }

    public PageList(int initialCapacity) {
        super(initialCapacity);
        this.pageNum = 1;
        this.pageSize = 20;
    }

    public PageList(Collection<? extends E> c) {
        super(c);
        this.pageNum = 1;
        this.pageSize = 20;
    }

    public PageList(int pageNum, int pageSize, List<? extends E> results, long totalRecords) {
        super(results);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
    }


    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return this.totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
