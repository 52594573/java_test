package com.ktp.project.entity;

import javax.persistence.*;

/**
 * 找工信息表
 *
 * @author djcken
 * @date 2018/6/23
 * id
 * jobl_uid               发布人uid
 * jobl_tel                发布人联系电话
 * jobl_type              找工类型1.班组找工  2工人找工
 * jobl_gzid              工种id
 * jobl_worktime      到岗时间
 * jobl_city               所在城市
 * jobl_size               班组规模（针对班组）
 * jobl_workage        工龄（针对工人）
 * jobl_content         找工介绍
 */
@Entity
@Table(name = "jobs_looking")
public class JobLooking {

    private int id;
    private int pubUid;
    private String pubMobile;
    private int pubState;//'发布状态 0正常 1删除 2停用',
    private int pubType;
    private int gzId;
    private String gzName;
    private String workTime;
    private String city;
    private String teamSize;
    private String experience;
    private String workAge;
    private String content;
    private String createTime;

    private double skillScore;
    private String userName;
    private String userPic;
    private int userSex;
    private double userStar;

    public JobLooking() {
    }

    public JobLooking(int id, int pubUid, String pubMobile, int pubType, int gzId, String gzName, String workTime, String city, String teamSize, String experience, String workAge, String content, String createTime, double skillScore, String userName, String userPic, int userSex, double userStar) {
        this.id = id;
        this.pubUid = pubUid;
        this.pubMobile = pubMobile;
        this.pubType = pubType;
        this.gzId = gzId;
        this.gzName = gzName;
        this.workTime = workTime;
        this.city = city;
        this.teamSize = teamSize;
        this.experience = experience;
        this.workAge = workAge;
        this.content = content;
        this.createTime = createTime;
        this.skillScore = skillScore;
        this.userName = userName;
        this.userPic = userPic;
        this.userSex = userSex;
        this.userStar = userStar;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "jobl_uid")
    public int getPubUid() {
        return pubUid;
    }

    public void setPubUid(int pubUid) {
        this.pubUid = pubUid;
    }

    @Column(name = "jobl_tel")
    public String getPubMobile() {
        return pubMobile;
    }

    public void setPubMobile(String pubMobile) {
        this.pubMobile = pubMobile;
    }

    @Column(name = "jobl_state")
    public int getPubState() {
        return pubState;
    }

    public void setPubState(int pubState) {
        this.pubState = pubState;
    }

    @Column(name = "jobl_type")
    public int getPubType() {
        return pubType;
    }

    public void setPubType(int pubType) {
        this.pubType = pubType;
    }

    @Column(name = "jobl_gzid")
    public int getGzId() {
        return gzId;
    }

    public void setGzId(int gzId) {
        this.gzId = gzId;
    }

    @Column(name = "jobl_gzname")
    public String getGzName() {
        return gzName;
    }

    public void setGzName(String gzName) {
        this.gzName = gzName;
    }

    @Column(name = "jobl_worktime")
    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    @Column(name = "jobl_city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "jobl_teamsize")
    public String getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(String teamSize) {
        this.teamSize = teamSize;
    }

    @Column(name = "jobl_experience")
    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Column(name = "jobl_workage")
    public String getWorkAge() {
        return workAge;
    }

    public void setWorkAge(String workAge) {
        this.workAge = workAge;
    }

    @Column(name = "jobl_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "jobl_intime")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Transient
    public double getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(double skillScore) {
        this.skillScore = skillScore;
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
    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    @Transient
    public double getUserStar() {
        return userStar;
    }

    public void setUserStar(double userStar) {
        this.userStar = userStar;
    }
}
