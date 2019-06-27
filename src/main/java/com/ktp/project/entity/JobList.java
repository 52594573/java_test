package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 招聘信息表
 *
 * @author djcken
 * @date 2018/6/23
 * id
 * pro_id             项目id
 * job_uid           发布人uid
 * job_state        状态：1正常显示，2不显示
 * job_area         所在位置
 * job_address    项目地址
 * job_position   职位名称
 * job_content    职位介绍
 * job_money     薪资范围
 * job_number       招聘人数
 * job_proname     项目名称
 * job_endtime       结束招聘时间
 * job_intime     发布招聘的时间
 * job_tel          联系电话
 * is_del           是否已删除 0正常 1删除
 * job_fzr         联系人
 * job_gzid      工种id
 * job_gzname    工种名称
 * job_obj       招聘对象1工人 2班组长
 * job_addw   项目经度
 * job_addh   项目维度
 * job_type                  招聘类型1项目部招聘 2班组长招聘
 * job_protime             项目开始时间
 * job_experience         经验要求（针对工人）
 * job_teamsize            班组规模  (针对班组)
 */
@Entity
@Table(name = "jobs_list")
public class JobList {

    private int id;
    private int proId; //项目id
    private int pubUid;//发布人uid
    private int jobState; //状态：1正常显示，2不显示,3停用
    private int jobArea; //所在位置
    private String jobAddress; //项目地址
    private String jobPosition; //职位名称
    private String jobContent;// 职位介绍
    private String jobMoney;// 薪资范围
    private String jobNumber;// 招聘人数
    private String jobProname; //项目名称
    private Date jobEndtime;// 结束招聘时间
    private String jobIntime;// 发布招聘的时间
    private String jobTel;// 联系电话
    private int isDel;// 是否已删除 0正常 1删除
    private String jobFzr;// 联系人
    private int jobGzid;// 工种id
    private String jobGzname;//工种名称
    private int jobObj; //招聘对象4工人 8班组长
    private double jobAddw;// 项目经度
    private double jobAddh;// 项目维度
    private String jobLocation;//定位的具体位置
    private int jobType;// 招聘类型1项目部招聘 2班组长招聘
    private String jobProtime;// 项目开始时间
    private String jobExperience;// 经验要求（针对工人）
    private String jobTeamsize;// 班组规模(针对班组)

    private int applyNum;//申请人数
    private List<JobApply> jobApplyList;
    private double distance;//距离
    private int apply;//是否已申请

    private String pubMobile;//发布者电话
    private String userName;//发布者名字

    public JobList() {
    }

    public JobList(int id, int proId, int pubUid, int jobState, int jobArea, String jobAddress, String jobPosition, String jobContent, String jobMoney, String jobNumber, String jobProname, Date jobEndtime, String jobIntime, String jobTel, int isDel, String jobFzr, int jobGzid, String jobGzname, int jobObj, double jobAddw, double jobAddh, String jobLocation, int jobType, String jobProtime, String jobExperience, String jobTeamsize, String pubMobile, String userName) {
        this.id = id;
        this.proId = proId;
        this.pubUid = pubUid;
        this.jobState = jobState;
        this.jobArea = jobArea;
        this.jobAddress = jobAddress;
        this.jobPosition = jobPosition;
        this.jobContent = jobContent;
        this.jobMoney = jobMoney;
        this.jobNumber = jobNumber;
        this.jobProname = jobProname;
        this.jobEndtime = jobEndtime;
        this.jobIntime = jobIntime;
        this.jobTel = jobTel;
        this.isDel = isDel;
        this.jobFzr = jobFzr;
        this.jobGzid = jobGzid;
        this.jobGzname = jobGzname;
        this.jobObj = jobObj;
        this.jobAddw = jobAddw;
        this.jobAddh = jobAddh;
        this.jobLocation = jobLocation;
        this.jobType = jobType;
        this.jobProtime = jobProtime;
        this.jobExperience = jobExperience;
        this.jobTeamsize = jobTeamsize;
        this.pubMobile = pubMobile;
        this.userName = userName;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "pro_id")
    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    @Column(name = "job_uid")
    public int getPubUid() {
        return pubUid;
    }

    public void setPubUid(int pubUid) {
        this.pubUid = pubUid;
    }

    @Column(name = "job_state")
    public int getJobState() {
        return jobState;
    }

    public void setJobState(int jobState) {
        this.jobState = jobState;
    }

    @Column(name = "job_area")
    public int getJobArea() {
        return jobArea;
    }

    public void setJobArea(int jobArea) {
        this.jobArea = jobArea;
    }

    @Column(name = "job_address")
    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    @Column(name = "job_position")
    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    @Column(name = "job_content")
    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    @Column(name = "job_money")
    public String getJobMoney() {
        return jobMoney;
    }

    public void setJobMoney(String jobMoney) {
        this.jobMoney = jobMoney;
    }

    @Column(name = "job_number")
    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @Column(name = "job_proname")
    public String getJobProname() {
        return jobProname;
    }

    public void setJobProname(String jobProname) {
        this.jobProname = jobProname;
    }

    @Column(name = "job_endtime")
    public Date getJobEndtime() {
        return jobEndtime;
    }

    public void setJobEndtime(Date jobEndtime) {
        this.jobEndtime = jobEndtime;
    }

    @Column(name = "job_intime")
    public String getJobIntime() {
        return jobIntime;
    }

    public void setJobIntime(String jobIntime) {
        this.jobIntime = jobIntime;
    }

    @Column(name = "job_tel")
    public String getJobTel() {
        return jobTel;
    }

    public void setJobTel(String jobTel) {
        this.jobTel = jobTel;
    }

    @Column(name = "is_del")
    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    @Column(name = "job_fzr")
    public String getJobFzr() {
        return jobFzr;
    }

    public void setJobFzr(String jobFzr) {
        this.jobFzr = jobFzr;
    }

    @Column(name = "job_gzid")
    public int getJobGzid() {
        return jobGzid;
    }

    public void setJobGzid(int jobGzid) {
        this.jobGzid = jobGzid;
    }

    @Column(name = "job_gzname")
    public String getJobGzname() {
        return jobGzname;
    }

    public void setJobGzname(String jobGzname) {
        this.jobGzname = jobGzname;
    }

    @Column(name = "job_obj")
    public int getJobObj() {
        return jobObj;
    }

    public void setJobObj(int jobObj) {
        this.jobObj = jobObj;
    }

    @Column(name = "job_addw")
    public double getJobAddw() {
        return jobAddw;
    }

    public void setJobAddw(double jobAddw) {
        this.jobAddw = jobAddw;
    }

    @Column(name = "job_addh")
    public double getJobAddh() {
        return jobAddh;
    }

    public void setJobAddh(double jobAddh) {
        this.jobAddh = jobAddh;
    }

    @Column(name = "job_location")
    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    @Column(name = "job_type")
    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    @Column(name = "job_protime")
    public String getJobProtime() {
        return jobProtime;
    }

    public void setJobProtime(String jobProtime) {
        this.jobProtime = jobProtime;
    }

    @Column(name = "job_experience")
    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    @Column(name = "job_teamsize")
    public String getJobTeamsize() {
        return jobTeamsize;
    }

    public void setJobTeamsize(String jobTeamsize) {
        this.jobTeamsize = jobTeamsize;
    }

    @Transient
    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    @Transient
    public List<JobApply> getJobApplyList() {
        return jobApplyList;
    }

    public void setJobApplyList(List<JobApply> jobApplyList) {
        this.jobApplyList = jobApplyList;
    }

    @Transient
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Transient
    public int getApply() {
        return apply;
    }

    public void setApply(int apply) {
        this.apply = apply;
    }

    @Transient
    public String getPubMobile() {
        return pubMobile;
    }

    public void setPubMobile(String pubMobile) {
        this.pubMobile = pubMobile;
    }

    @Transient
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
