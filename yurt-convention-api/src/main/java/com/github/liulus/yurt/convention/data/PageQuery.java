package com.github.liulus.yurt.convention.data;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class PageQuery implements Pageable {

    private static final int MAX_PAGE_SIZE = 200;
    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private int pageNum;
    private int pageSize;
    private boolean isCount;

    public PageQuery() {
        pageNum = DEFAULT_PAGE_NUM;
        pageSize = DEFAULT_PAGE_SIZE;
    }

    public PageQuery(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }


    @Override
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = Math.max(1, pageNum);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.min(MAX_PAGE_SIZE, Math.max(1, pageSize));
    }

    @Override
    public int getOffset() {
        return pageSize * (pageNum - 1);
    }

    @Override
    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean count) {
        isCount = count;
    }
}
