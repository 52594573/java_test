package com.ktp.project.dto;

/**
 * @Author: wuyeming
 * @Date: 2018-10-05 16:34
 */
public class ProjectDto {
    private Integer id;
    private String pName;
    private String pContent;
    private String pBegintime;
    private String pEndtime;
    private Integer zj;
    private String shengId;
    private String sheng;
    private String shiId;
    private String shi;
    private String quId;
    private String qu;
    private Integer hatchStatus;//是否是孵化中心
    private Integer PState;
    private String pAddress;
    private String pArea;

    public Integer getPState() {
        return PState;
    }

    public void setPState(Integer PState) {
        this.PState = PState;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getpArea() {
        return pArea;
    }

    public void setpArea(String pArea) {
        this.pArea = pArea;
    }

    public Integer getHatchStatus() {
        return hatchStatus;
    }

    public void setHatchStatus(Integer hatchStatus) {
        this.hatchStatus = hatchStatus;
    }

    public Integer getId() {
        return id;
    }

    public ProjectDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getpName() {
        return pName;
    }

    public ProjectDto setpName(String pName) {
        this.pName = pName;
        return this;
    }

    public String getpContent() {
        return pContent;
    }

    public ProjectDto setpContent(String pContent) {
        this.pContent = pContent;
        return this;
    }

    public String getpBegintime() {
        return pBegintime;
    }

    public ProjectDto setpBegintime(String pBegintime) {
        this.pBegintime = pBegintime;
        return this;
    }

    public String getpEndtime() {
        return pEndtime;
    }

    public ProjectDto setpEndtime(String pEndtime) {
        this.pEndtime = pEndtime;
        return this;
    }

    public Integer getZj() {
        return zj;
    }

    public ProjectDto setZj(Integer zj) {
        this.zj = zj;
        return this;
    }

    public String getShengId() {
        return shengId;
    }

    public ProjectDto setShengId(String shengId) {
        this.shengId = shengId;
        return this;
    }

    public String getSheng() {
        return sheng;
    }

    public ProjectDto setSheng(String sheng) {
        this.sheng = sheng;
        return this;
    }

    public String getShiId() {
        return shiId;
    }

    public ProjectDto setShiId(String shiId) {
        this.shiId = shiId;
        return this;
    }

    public String getShi() {
        return shi;
    }

    public ProjectDto setShi(String shi) {
        this.shi = shi;
        return this;
    }

    public String getQuId() {
        return quId;
    }

    public ProjectDto setQuId(String quId) {
        this.quId = quId;
        return this;
    }

    public String getQu() {
        return qu;
    }

    public ProjectDto setQu(String qu) {
        this.qu = qu;
        return this;
    }
}
