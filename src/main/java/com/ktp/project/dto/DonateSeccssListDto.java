package com.ktp.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DonateSeccssListDto {
    private Integer id;//受赠id
    private Integer donUserId;//捐赠者id
    private String donHead;//捐赠者头像
    private String recHead;//受赠者头像
    private String actTop;//主题
    private Integer recActualSum;//捐赠数量
    private String donName;//捐赠者名字
    private String recName;//受赠者名字
    private String donUnit;//单位


    private Integer recSum;//申请数量
    private String donDescribe;//捐赠描述
    private String donPicture;//捐赠图片
    private String recAddress;//收货地址
    private String donTel;//捐赠者电话
    private String recTel;//捐赠者电话
    private String recWay;//捐赠方式


    private Integer actStatus;//活动状态
    private String evaPicture;//评论图片
    private String evaDescribe;//评论信息
    private String recDealTime;//受赠时间

    private Integer recSex;//受赠者性别
    private Integer donSex;//捐赠者性别
    private Integer recStatus;//受赠状态
    private String recGetTime;//确认收货时间
    private Integer actId;//活动id
    private Integer donId;//捐赠id
    private Integer recId;//受赠id

    private String donWay;//捐赠方式
    private String donPostage;//谁支付邮费


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDonId() {
        return donId;
    }

    public void setDonId(Integer donId) {
        this.donId = donId;
    }

    public String getDonHead() {
        return donHead;
    }

    public void setDonHead(String donHead) {
        this.donHead = donHead;
    }

    public String getRecHead() {
        return recHead;
    }

    public void setRecHead(String recHead) {
        this.recHead = recHead;
    }

    public String getActTop() {
        return actTop;
    }

    public void setActTop(String actTop) {
        this.actTop = actTop;
    }

    public Integer getRecActualSum() {
        return recActualSum;
    }

    public void setRecActualSum(Integer recActualSum) {
        this.recActualSum = recActualSum;
    }

    public String getDonName() {
        return donName;
    }

    public void setDonName(String donName) {
        this.donName = donName;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getDonUnit() {
        return donUnit;
    }

    public void setDonUnit(String donUnit) {
        this.donUnit = donUnit;
    }

    public Integer getRecSum() {
        return recSum;
    }

    public void setRecSum(Integer recSum) {
        this.recSum = recSum;
    }

    public String getDonDescribe() {
        return donDescribe;
    }

    public void setDonDescribe(String donDescribe) {
        this.donDescribe = donDescribe;
    }

    public String getDonPicture() {
        return donPicture;
    }

    public void setDonPicture(String donPicture) {
        this.donPicture = donPicture;
    }

    public String getRecAddress() {
        return recAddress;
    }

    public void setRecAddress(String recAddress) {
        this.recAddress = recAddress;
    }

    public String getDonTel() {
        return donTel;
    }

    public void setDonTel(String donTel) {
        this.donTel = donTel;
    }

    public String getRecWay() {
        return recWay;
    }

    public void setRecWay(String recWay) {
        this.recWay = recWay;
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

    public String getEvaPicture() {
        return evaPicture;
    }

    public void setEvaPicture(String evaPicture) {
        this.evaPicture = evaPicture;
    }

    public String getEvaDescribe() {
        return evaDescribe;
    }

    public void setEvaDescribe(String evaDescribe) {
        this.evaDescribe = evaDescribe;
    }

    public String getRecDealTime() {
        return recDealTime;
    }

    public void setRecDealTime(String recDealTime) {
        this.recDealTime = recDealTime;
    }

    public Integer getRecSex() {
        return recSex;
    }

    public void setRecSex(Integer recSex) {
        this.recSex = recSex;
    }

    public Integer getDonSex() {
        return donSex;
    }

    public void setDonSex(Integer donSex) {
        this.donSex = donSex;
    }

    public String getRecTel() {
        return recTel;
    }

    public void setRecTel(String recTel) {
        this.recTel = recTel;
    }

    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
    }

    public String getRecGetTime() {
        return recGetTime;
    }

    public void setRecGetTime(String recGetTime) {
        this.recGetTime = recGetTime;
    }

    public Integer getDonUserId() {
        return donUserId;
    }

    public void setDonUserId(Integer donUserId) {
        this.donUserId = donUserId;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public String getDonWay() {
        return donWay;
    }

    public void setDonWay(String donWay) {
        this.donWay = donWay;
    }

    public String getDonPostage() {
        return donPostage;
    }

    public void setDonPostage(String donPostage) {
        this.donPostage = donPostage;
    }
}
