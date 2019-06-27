package com.ktp.project.entity;

import java.util.List;

/**
 * 分类信息
 *
 * @author djcken
 * @date 2018/5/30
 */
public class MallSortInfo {

    private long count;
    private List<MallSort> list;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<MallSort> getList() {
        return list;
    }

    public void setList(List<MallSort> list) {
        this.list = list;
    }
}
