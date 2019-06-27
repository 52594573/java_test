package com.ktp.project.dto.project;

import java.util.Date;

public class ProMessage {
    private Integer proId;//项目编号
    private String proName;//项目名称
    private String proPkCorp;//总包企业用户编号, 合作平台的企业用户编号
    private String proCorpName;//总包企业全称
    private String proCorpId;//总包企业证件编号
    private String proLongitude;//经度（多个用#分隔，和纬度对应）
    private String proLatitude;//纬度（多个用#分隔，和经度对应）
    private String proCityName;//所在城市
    private String proPrincipalPhone;//负责人电话
    private String proPrincipal;//主要负责人
    private String proContent;//项目介绍
    private Date proBeginTime;//开始时间
    private Date proEndTime;//结束时间
    private String proLogo;//logo
    private Integer proStatus;//工程状态：1未开始2.进行中3.暂时停工4.已竣工
    private String proDevelopers;//开发商
    private String proRange;//打卡范围（经纬度）
    private String proAddress;//项目地址
    private Integer proPeopleNum;//项目人数
    private Integer proGroupNum;//班组人数
    private Double proAttendanceRate;//考勤率

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProPkCorp() {
        return proPkCorp;
    }

    public void setProPkCorp(String proPkCorp) {
        this.proPkCorp = proPkCorp;
    }

    public String getProCorpName() {
        return proCorpName;
    }

    public void setProCorpName(String proCorpName) {
        this.proCorpName = proCorpName;
    }

    public String getProCorpId() {
        return proCorpId;
    }

    public void setProCorpId(String proCorpId) {
        this.proCorpId = proCorpId;
    }

    public String getProLongitude() {
        return proLongitude;
    }

    public void setProLongitude(String proLongitude) {
        this.proLongitude = proLongitude;
    }

    public String getProLatitude() {
        return proLatitude;
    }

    public void setProLatitude(String proLatitude) {
        this.proLatitude = proLatitude;
    }

    public String getProCityName() {
        return proCityName;
    }

    public void setProCityName(String proCityName) {
        this.proCityName = proCityName;
    }

    public String getProPrincipalPhone() {
        return proPrincipalPhone;
    }

    public void setProPrincipalPhone(String proPrincipalPhone) {
        this.proPrincipalPhone = proPrincipalPhone;
    }

    public String getProPrincipal() {
        return proPrincipal;
    }

    public void setProPrincipal(String proPrincipal) {
        this.proPrincipal = proPrincipal;
    }

    public String getProContent() {
        return proContent;
    }

    public void setProContent(String proContent) {
        this.proContent = proContent;
    }

    public Date getProBeginTime() {
        return proBeginTime;
    }

    public void setProBeginTime(Date proBeginTime) {
        this.proBeginTime = proBeginTime;
    }

    public Date getProEndTime() {
        return proEndTime;
    }

    public void setProEndTime(Date proEndTime) {
        this.proEndTime = proEndTime;
    }

    public String getProLogo() {
        return proLogo;
    }

    public void setProLogo(String proLogo) {
        this.proLogo = proLogo;
    }

    public Integer getProStatus() {
        return proStatus;
    }

    public void setProStatus(Integer proStatus) {
        this.proStatus = proStatus;
    }

    public String getProDevelopers() {
        return proDevelopers;
    }

    public void setProDevelopers(String proDevelopers) {
        this.proDevelopers = proDevelopers;
    }

    public String getProRange() {
        return proRange;
    }

    public void setProRange(String proRange) {
        this.proRange = proRange;
    }

    public String getProAddress() {
        return proAddress;
    }

    public void setProAddress(String proAddress) {
        this.proAddress = proAddress;
    }

    public Integer getProPeopleNum() {
        return proPeopleNum;
    }

    public void setProPeopleNum(Integer proPeopleNum) {
        this.proPeopleNum = proPeopleNum;
    }

    public Integer getProGroupNum() {
        return proGroupNum;
    }

    public void setProGroupNum(Integer proGroupNum) {
        this.proGroupNum = proGroupNum;
    }

    public Double getProAttendanceRate() {
        return proAttendanceRate;
    }

    public void setProAttendanceRate(Double proAttendanceRate) {
        this.proAttendanceRate = proAttendanceRate;
    }

    public ProMessage() {
    }
}
