package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 招聘申请表
 *
 * @author djcken
 * @date 2018/6/23
 */
@Entity
@Table(name = "jobs_apply")
public class JobApply {

    private int id;
    private int jobId;
    private int userId;
    private Date inTime;
    private String mobile;
    private String content;

    private String userName;
    private String userPic;
    private double skillScore;
    private int userSex;

    public JobApply() {
    }

    public JobApply(int id, int jobId, int userId, Date inTime, String mobile, String content, String userName, String userPic, double skillScore, int userSex) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.inTime = inTime;
        this.mobile = mobile;
        this.content = content;
        this.userName = userName;
        this.userPic = userPic;
        this.skillScore = skillScore;
        this.userSex = userSex;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "jl_id")
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Column(name = "a_uid")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "a_intime")
    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    @Column(name = "a_tel")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "a_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Transient
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Transient
    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    @Transient
    public double getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(double skillScore) {
        this.skillScore = skillScore;
    }

    @Transient
    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }
}
