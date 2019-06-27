package com.ktp.project.dto;

import com.ktp.project.logic.schedule.OrderTask;

import java.util.Date;

public class DonateApplyDetailDto {
    private Integer donId;   //捐赠id
    private Integer recId;   //受赠id
    private Integer actStatus;  //活动状态
    private Integer donInventory;//捐赠库存
    private String recName;//受赠者名字
    private Integer recSum;//受赠申请数量
    private String recTime;//受赠申请时间
    private String donUnit;//捐赠物品单位
    private String donDescribe;//捐赠描述
    private String recHead;//受赠者头像
    private Integer donCommentSum;//捐赠评论数
    private String recTel;//受赠者收货电话
    private Double recStar;//受赠者星级
    private Integer recCert;//受赠者认证
    private String recWay;//受赠方式
    private String evaDescribe;//评论
    private String evaPicture;//评论图片
    private String recAddress;
    private Integer recStatus;//受赠申请状态
    private String recReason;//受赠申请原因说明
    private String recConsignee;//收货人姓名
    private String recTimeStr;//受赠同意时间
    private Integer recActualSum;//实际捐赠数量
    private Integer recUserId;//受赠者id
    private Integer donStatus;//捐赠的状态（是否审核）

    public String getRecConsignee() {
        return recConsignee;
    }

    public void setRecConsignee(String recConsignee) {
        this.recConsignee = recConsignee;
    }

    public String getRecReason() {
        return recReason;
    }

    public void setRecReason(String recReason) {
        this.recReason = recReason;
    }

    public Integer getDonId() {
        return donId;
    }

    public void setDonId(Integer donId) {
        this.donId = donId;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }

    public Integer getDonInventory() {
        return donInventory;
    }

    public void setDonInventory(Integer donInventory) {
        this.donInventory = donInventory;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public Integer getRecSum() {
        return recSum;
    }

    public void setRecSum(Integer recSum) {
        this.recSum = recSum;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
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

    public String getRecHead() {
        return recHead;
    }

    public void setRecHead(String recHead) {
        this.recHead = recHead;
    }

    public Integer getDonCommentSum() {
        return donCommentSum;
    }

    public void setDonCommentSum(Integer donCommentSum) {
        this.donCommentSum = donCommentSum;
    }

    public String getRecTel() {
        return recTel;
    }

    public void setRecTel(String recTel) {
        this.recTel = recTel;
    }

    public Double getRecStar() {
        return recStar;
    }

    public void setRecStar(Double recStar) {
        this.recStar = recStar;
    }

    public Integer getRecCert() {
        return recCert;
    }

    public void setRecCert(Integer recCert) {
        this.recCert = recCert;
    }

    public String getRecWay() {
        return recWay;
    }

    public void setRecWay(String recWay) {
        this.recWay = recWay;
    }

    public String getEvaDescribe() {
        return evaDescribe;
    }

    public void setEvaDescribe(String evaDescribe) {
        this.evaDescribe = evaDescribe;
    }

    public String getEvaPicture() {
        return evaPicture;
    }

    public void setEvaPicture(String evaPicture) {
        this.evaPicture = evaPicture;
    }

    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
    }

    public String getRecTimeStr() {
        return recTimeStr;
    }

    public void setRecTimeStr(String recTimeStr) {
        this.recTimeStr = recTimeStr;
    }

    public Integer getRecActualSum() {
        return recActualSum;
    }

    public void setRecActualSum(Integer recActualSum) {
        this.recActualSum = recActualSum;
    }

    public Integer getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(Integer recUserId) {
        this.recUserId = recUserId;
    }

    public Integer getDonStatus() {
        return donStatus;
    }

    public void setDonStatus(Integer donStatus) {
        this.donStatus = donStatus;
    }
}
