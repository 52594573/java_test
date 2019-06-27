package com.ktp.project.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by LinHon 2018/12/12
 */
@Entity
@Table(name = "clock_in_failure")
public class ClockInFailure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
     * 考勤打卡类型，对应ProxyClockIn表的status字段
     * 3：上班
     * 4：下班
     */
    @Column(name = "type")
    private int type;

    /**
     * 状态
     * 1：未处理
     * 2：已补卡
     * 3：不处理
     */
    @Column(name = "status")
    private int status;

    /**
     * 相似度
     */
    @Column(name = "similarity")
    private double similarity;

    /**
     * 经度
     */
    @Column(name = "longitude")
    private double longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private double latitude;

    /**
     * 图像
     */
    @Column(name = "image")
    private String image;

    /**
     * 错误消息
     */
    @Column(name = "error_msg", columnDefinition = "text")
    private String errorMsg;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
