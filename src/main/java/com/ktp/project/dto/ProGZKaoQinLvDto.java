package com.ktp.project.dto;

import java.math.BigDecimal;

/**
 * @Author: tangbin
 * @Date: 2018/12/12 16:31
 * @Version 1.0
 */
public class ProGZKaoQinLvDto {
    public String getGzName() {
        return gzName;
    }

    public void setGzName(String gzName) {
        this.gzName = gzName;
    }

    public Integer getChuQinCount() {
        return chuQinCount;
    }

    public void setChuQinCount(Integer chuQinCount) {
        this.chuQinCount = chuQinCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getChuQinLv() {
        return chuQinLv;
    }

    public void setChuQinLv(Integer chuQinLv) {
        this.chuQinLv = chuQinLv;
    }

    private String gzName;
    private Integer chuQinCount;
    private Integer count;
    private Integer chuQinLv;
}
