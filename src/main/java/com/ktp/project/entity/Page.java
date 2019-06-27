package com.ktp.project.entity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by LinHon 2018/10/16
 */
public class Page<T> {

    //页数
    private int pageNo = 1;

    //显示条数
    private int pageSize = 10;

    //总页数
    private int totalPages;

    //总条数
    private long totalCount;

    //是否有下一页
    private boolean hasNextPage;

    //返回数据
    private List<T> result = Lists.newArrayList();

    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public int getStartLine() {
        return (pageNo - 1) * pageSize;
    }

    public Page<T> builderPage() {
        if (totalCount != 0) {
            totalPages = (int)(totalCount / pageSize);
            if (totalCount % pageSize > 0) {
                totalPages++;
            }

            if (totalPages > pageNo) {
                hasNextPage = true;
            }
        }
        return this;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo > 0) {
            this.pageNo = pageNo;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
