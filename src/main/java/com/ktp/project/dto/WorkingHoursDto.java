package com.ktp.project.dto;

/**
 * @Author: tangbin
 * @Date: 2018/12/13 15:24
 * @Version 1.0
 */
public class WorkingHoursDto {
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(Integer timeCount) {
        this.timeCount = timeCount;
    }

    private Integer userId;
    private Integer timeCount;
    private Integer proId;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }
}
