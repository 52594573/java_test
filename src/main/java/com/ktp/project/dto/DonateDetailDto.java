package com.ktp.project.dto;

import java.util.List;

public class DonateDetailDto {
    private Integer donUserId;
    private Integer donId;//捐赠id
    private Integer donStatus;//捐赠状态
    private Integer donInventory;//库存
    private Integer FinishSum;//这次发布已捐赠多少
    private Long donFinishSum;//完成捐赠数
    private Long joinSum;//参加活动次数
    private String donName;//捐赠者名字
    private Double donPrince;//物品价格
    private String donPostage;//包邮说明
    private String donWay;//包邮说明
    private Float donPercent;//新旧程度
    private String donUnit;//单位
    private String donDescribe;//描述
    private Integer donApplySum;//申请未读
    private Integer donCommentSum;//评论未读
    private String donPicture;//捐赠图片
    private String donHead;//捐赠者头像
    private String donAddress;//捐赠地址
    private Integer actStatus;//活动状态
    private String donRejectReason;//拒绝原因

    public String getDonRejectReason() {
        return donRejectReason;
    }

    public void setDonRejectReason(String donRejectReason) {
        this.donRejectReason = donRejectReason;
    }

    public Integer getFinishSum() {
        return FinishSum;
    }

    public void setFinishSum(Integer finishSum) {
        FinishSum = finishSum;
    }

    public String getDonWay() {
        return donWay;
    }

    public void setDonWay(String donWay) {
        this.donWay = donWay;
    }

    public Integer getDonUserId() {
        return donUserId;
    }

    public void setDonUserId(Integer donUserId) {
        this.donUserId = donUserId;
    }

    public Integer getDonCommentSum() {
        return donCommentSum;
    }

    public void setDonCommentSum(Integer donCommentSum) {
        this.donCommentSum = donCommentSum;
    }

    public Integer getDonId() {
        return donId;
    }

    public void setDonId(Integer donId) {
        this.donId = donId;
    }

    public Integer getDonStatus() {
        return donStatus;
    }

    public void setDonStatus(Integer donStatus) {
        this.donStatus = donStatus;
    }

    public Integer getDonInventory() {
        return donInventory;
    }

    public void setDonInventory(Integer donInventory) {
        this.donInventory = donInventory;
    }

    public Long getDonFinishSum() {
        return donFinishSum;
    }

    public void setDonFinishSum(Long donFinishSum) {
        this.donFinishSum = donFinishSum;
    }

    public Long getJoinSum() {
        return joinSum;
    }

    public void setJoinSum(Long joinSum) {
        this.joinSum = joinSum;
    }

    public String getDonName() {
        return donName;
    }

    public void setDonName(String donName) {
        this.donName = donName;
    }

    public Double getDonPrince() {
        return donPrince;
    }

    public void setDonPrince(Double donPrince) {
        this.donPrince = donPrince;
    }

    public String getDonPostage() {
        return donPostage;
    }

    public void setDonPostage(String donPostage) {
        this.donPostage = donPostage;
    }

    public Float getDonPercent() {
        return donPercent;
    }

    public void setDonPercent(Float donPercent) {
        this.donPercent = donPercent;
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

    public Integer getDonApplySum() {
        return donApplySum;
    }

    public void setDonApplySum(Integer donApplySum) {
        this.donApplySum = donApplySum;
    }

    public String getDonPicture() {
        return donPicture;
    }

    public void setDonPicture(String donPicture) {
        this.donPicture = donPicture;
    }

    public String getDonHead() {
        return donHead;
    }

    public void setDonHead(String donHead) {
        this.donHead = donHead;
    }

    public String getDonAddress() {
        return donAddress;
    }

    public void setDonAddress(String donAddress) {
        this.donAddress = donAddress;
    }

    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }
}
