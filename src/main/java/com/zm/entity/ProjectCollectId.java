package com.zm.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: tangbin
 * @Date: 2018/12/10 9:51
 * @Version 1.0
 */
public class ProjectCollectId implements java.io.Serializable{

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStatisticsDate() {
        return statisticsDate;
    }

    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public Integer getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(Integer attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public Integer getNewAttendanceCount() {
        return newAttendanceCount;
    }

    public void setNewAttendanceCount(Integer newAttendanceCount) {
        this.newAttendanceCount = newAttendanceCount;
    }

    public Integer getAppAttendanceCount() {
        return appAttendanceCount;
    }

    public void setAppAttendanceCount(Integer appAttendanceCount) {
        this.appAttendanceCount = appAttendanceCount;
    }

    public Integer getZjAttendanceCount() {
        return zjAttendanceCount;
    }

    public void setZjAttendanceCount(Integer zjAttendanceCount) {
        this.zjAttendanceCount = zjAttendanceCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getNewProjectCount() {
        return newProjectCount;
    }

    public Integer getAppActivity() {
        return appActivity;
    }

    public void setAppActivity(Integer appActivity) {
        this.appActivity = appActivity;
    }

    public void setNewProjectCount(Integer newProjectCount) {
        this.newProjectCount = newProjectCount;
    }
    /**
     * 获取项目汇总信息
     */

    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date statisticsDate;//统计日期
    private Integer userCount;//用户总数
    private Integer newUserCount;//当日新增用户数
    private Integer workCount;//工人总数
    private Integer attendanceCount;//当日考勤量
    private Integer newAttendanceCount;//当日新增考勤数量
    private Integer appAttendanceCount;//app考勤量
    private Integer zjAttendanceCount;//闸机考勤量
    private Integer appActivity;//app活跃度
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;//统计日期
    private Integer projectCount;//所有项目数
    private Integer newProjectCount;//当前新增项目数
    private Integer projectId;//项目ID
    private Integer allProjectCount;//项目总人数
    private Integer KaoQinLv;//考勤率
    private Integer allKaoQinCount;//考勤人数
    private String createTimeStr;
    private String statisticsDateStr;
    public String getStatisticsDateStr() {
        return statisticsDateStr;
    }
    public void setStatisticsDateStr(String statisticsDateStr) {
        this.statisticsDateStr = statisticsDateStr;
    }
    public String getCreateTimeStr() {
        return createTimeStr;
    }
    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getAllProjectCount() {
        return allProjectCount;
    }

    public void setAllProjectCount(Integer allProjectCount) {
        this.allProjectCount = allProjectCount;
    }

    public Integer getKaoQinLv() {
        return KaoQinLv;
    }

    public void setKaoQinLv(Integer kaoQinLv) {
        KaoQinLv = kaoQinLv;
    }

    public Integer getAllKaoQinCount() {
        return allKaoQinCount;
    }

    public void setAllKaoQinCount(Integer allKaoQinCount) {
        this.allKaoQinCount = allKaoQinCount;
    }
}
