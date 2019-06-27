package com.ktp.project.dto;

import com.sun.istack.internal.NotNull;

import javax.persistence.Transient;
import java.util.Date;

public class WorkLogGatherDto {

    private Integer id;//表id
    @NotNull
    private Integer projectId;//项目id
    private Integer userId;//用户id
    private String userName;//用户名
    private Integer userSex;//用户性别
    private String headPhotoUrl;//用户头像
    private Integer jobType;//工作类型
    private Integer totalQualityLogNum;//月总质量记录数
    private Integer totalSafeLogNum;//月总安全质量记录数
    private Integer totalActionLogNum;//月总行为记录数字
    private Integer jobRate; //工作评级 1 优秀 2 称职 3 合格 4 不合格
    private Double rateScore;//评级分数
    private Integer year;//年份
    private Integer month;//月份
    private Date createTime;//创建时间
    @Transient
    private Integer recordType;//记录类型 1.质量检查2.安全事件3.不当行为

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getHeadPhotoUrl() {
        return headPhotoUrl;
    }

    public void setHeadPhotoUrl(String headPhotoUrl) {
        this.headPhotoUrl = headPhotoUrl;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Integer getTotalQualityLogNum() {
        return totalQualityLogNum;
    }

    public void setTotalQualityLogNum(Integer totalQualityLogNum) {
        this.totalQualityLogNum = totalQualityLogNum;
    }

    public Integer getTotalSafeLogNum() {
        return totalSafeLogNum;
    }

    public void setTotalSafeLogNum(Integer totalSafeLogNum) {
        this.totalSafeLogNum = totalSafeLogNum;
    }

    public Integer getTotalActionLogNum() {
        return totalActionLogNum;
    }

    public void setTotalActionLogNum(Integer totalActionLogNum) {
        this.totalActionLogNum = totalActionLogNum;
    }

    public Integer getJobRate() {
        return jobRate;
    }

    public void setJobRate(Integer jobRate) {
        this.jobRate = jobRate;
    }

    public Double getRateScore() {
        return rateScore;
    }

    public void setRateScore(Double rateScore) {
        this.rateScore = rateScore;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
