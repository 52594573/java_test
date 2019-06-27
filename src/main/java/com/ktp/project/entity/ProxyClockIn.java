package com.ktp.project.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 代理打卡记录
 * Created by LinHon 2018/12/4
 */
@Entity
@Table(name = "proxy_clock_in")
public class ProxyClockIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 3：上班
     * 4：下班
     */
    @Column(name = "status")
    private int status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private int projectId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private int userId;

    /**
     * 代理用户ID
     */
    @Column(name = "proxy_user_id")
    private int proxyUserId;

    /**
     * 相似度
     */
    @Column(name = "similarity")
    private double similarity;

    /**
     * 经度
     */
    @Column(name = "longitude")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private BigDecimal latitude;

    /**
     * 图像
     */
    @Column(name = "image")
    private String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProxyUserId() {
        return proxyUserId;
    }

    public void setProxyUserId(int proxyUserId) {
        this.proxyUserId = proxyUserId;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
