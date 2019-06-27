package com.ktp.project.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工作记录统计
 */
@Entity
@Table(name = "work_log_gather")
public class WorkLogGather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//表id

    @Column(name = "project_id")
    private Integer projectId;//项目id

    @Column(name = "user_id")
    private Integer userId;//用户id

    @Column(name = "job_type")
    private Integer jobType;//工作类型

    @Column(name = "total_quality_log_num")
    private BigDecimal totalQualityLogNum;//月总质量记录数

    @Column(name = "total_safe_log_num")
    private BigDecimal totalSafeLogNum;//月总安全质量记录数

    @Column(name = "total_action_log_num")
    private BigDecimal totalActionLogNum;//月总行为记录数字

    @Column(name = "job_rate")
    private Integer jobRate; //工作评级 1 优秀 2 称职 3 合格 4 不合格

    @Column(name = "rate_score")
    private Double rateScore;//评级分数

    @Column(name = "year")
    private Integer year;//年份

    @Column(name = "month")
    private Integer month;//月份

    @Column(name = "create_time")
    private Date createTime;//创建时间

    @Transient
    private Integer recordType;//记录类型 1.质量检查2.安全事件3.不当行为

    public Double getRateScore() {
        return rateScore;
    }

    public void setRateScore(Double rateScore) {
        this.rateScore = rateScore;
    }

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

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public BigDecimal getTotalQualityLogNum() {
        return totalQualityLogNum;
    }

    public void setTotalQualityLogNum(BigDecimal totalQualityLogNum) {
        this.totalQualityLogNum = totalQualityLogNum;
    }

    public BigDecimal getTotalSafeLogNum() {
        return totalSafeLogNum;
    }

    public void setTotalSafeLogNum(BigDecimal totalSafeLogNum) {
        this.totalSafeLogNum = totalSafeLogNum;
    }

    public BigDecimal getTotalActionLogNum() {
        return totalActionLogNum;
    }

    public void setTotalActionLogNum(BigDecimal totalActionLogNum) {
        this.totalActionLogNum = totalActionLogNum;
    }

    public Integer getJobRate() {
        return jobRate;
    }

    public void setJobRate(Integer jobRate) {
        this.jobRate = jobRate;
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
