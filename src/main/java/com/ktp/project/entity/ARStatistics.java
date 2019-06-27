package com.ktp.project.entity;

/**
 * @Author: tangbin
 * @Date: 2018/12/17 14:45
 * @Version 1.0
 */

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "a_r_statistics")
public class ARStatistics {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getGzName() {
        return gzName;
    }

    public void setGzName(String gzName) {
        this.gzName = gzName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getKlTime() {
        return klTime;
    }

    public void setKlTime(Date klTime) {
        this.klTime = klTime;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// int(11) NOT NULL

    @Column(name = "pro_id")
    private Integer proId;// int(11) NULL;项目id

    @Column(name = "gz_name")
    private String gzName;//班组名

    @Column(name = "user_id")
    private Integer userId;// int(11) NULL类型；用户id

    @Column(name = "kl_time")
    private Date klTime;//考勤时间

    @Column(name = "hours")
    private Integer hours;// int(11)；工作时长

    @Column(name = "create_time")
    private Date createTime;//创建时间

    public Integer getGzId() {
        return gzId;
    }

    public void setGzId(Integer gzId) {
        this.gzId = gzId;
    }

    @Column(name = "gz_id")
    private Integer gzId;//工

    public String getKlTimeStr() {
        return klTimeStr;
    }

    public void setKlTimeStr(String klTimeStr) {
        this.klTimeStr = klTimeStr;
    }

    private String klTimeStr;

}

