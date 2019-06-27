package com.ktp.project.dto;

public class MyRecipientListDto {
    private Integer recId;//受赠id
    private String actTop;//活动主题
    private Integer recStatus;//状态
    private String donUnit;//件
    private String donDescribe;//描述
    private Integer recSum;//申请数量
    private String donPicture;//捐赠图片
    private Integer actStatus;//活动装填
    private Integer donUserId;//捐赠者id
    private String recDealTime;//捐赠时间
    private Integer recActualSum;//捐赠数量
    private Integer actId;//活动id


    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getActTop() {
        return actTop;
    }

    public void setActTop(String actTop) {
        this.actTop = actTop;
    }

    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
    }

    public String getDonUnit() {
        return donUnit;
    }

    public void setDonUnit(String donUnit) {
        this.donUnit = donUnit;
    }

    public String getDonDescribe() {
        return donDescribe;
    }

    public void setDonDescribe(String donDescribe) {
        this.donDescribe = donDescribe;
    }

    public Integer getRecSum() {
        return recSum;
    }

    public void setRecSum(Integer recSum) {
        this.recSum = recSum;
    }

    public String getDonPicture() {
        return donPicture;
    }

    public void setDonPicture(String donPicture) {
        this.donPicture = donPicture;
    }

    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }

    public Integer getDonUserId() {
        return donUserId;
    }

    public void setDonUserId(Integer donUserId) {
        this.donUserId = donUserId;
    }

    public String getRecDealTime() {
        return recDealTime;
    }

    public void setRecDealTime(String recDealTime) {
        this.recDealTime = recDealTime;
    }

    public Integer getRecActualSum() {
        return recActualSum;
    }

    public void setRecActualSum(Integer recActualSum) {
        this.recActualSum = recActualSum;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }
}
