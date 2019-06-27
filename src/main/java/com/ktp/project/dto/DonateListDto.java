package com.ktp.project.dto;

public class DonateListDto {
    private Integer userId;
    private Integer actId;
    private Integer donId;//活动id
    private Long joinSum;//参加活动次数
    private Long donSum;//捐赠成功次数
    private String donDescribe;//活动描述
    private Float donPercent;//新旧程度
    private String donPicture;//图片
    private Double donPrince;//价格
    private String donWay;//捐赠领取方式
    private String donPostage;//支付方式
    private Integer donInventory;//库存
    private String donUnit;//单位
    private String donHead;//头像
    private String donName;//名字
    private Integer userSex;//性别
    private String donAddress;//捐赠地址

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    public Integer getDonId() {
        return donId;
    }

    public void setDonId(Integer donId) {
        this.donId = donId;
    }

    public Long getJoinSum() {
        return joinSum;
    }

    public void setJoinSum(Long joinSum) {
        this.joinSum = joinSum;
    }

    public Long getDonSum() {
        return donSum;
    }

    public void setDonSum(Long donSum) {
        this.donSum = donSum;
    }

    public String getDonDescribe() {
        return donDescribe;
    }

    public void setDonDescribe(String donDescribe) {
        this.donDescribe = donDescribe;
    }

    public Float getDonPercent() {
        return donPercent;
    }

    public void setDonPercent(Float donPercent) {
        this.donPercent = donPercent;
    }

    public String getDonPicture() {
        return donPicture;
    }

    public void setDonPicture(String donPicture) {
        this.donPicture = donPicture;
    }

    public Double getDonPrince() {
        return donPrince;
    }

    public void setDonPrince(Double donPrince) {
        this.donPrince = donPrince;
    }

    public String getDonWay() {
        return donWay;
    }

    public void setDonWay(String donWay) {
        this.donWay = donWay;
    }

    public Integer getDonInventory() {
        return donInventory;
    }

    public String getDonPostage() {
        return donPostage;
    }

    public void setDonPostage(String donPostage) {
        this.donPostage = donPostage;
    }

    public void setDonInventory(Integer donInventory) {
        this.donInventory = donInventory;
    }

    public String getDonUnit() {
        return donUnit;
    }

    public void setDonUnit(String donUnit) {
        this.donUnit = donUnit;
    }

    public String getDonHead() {
        return donHead;
    }

    public void setDonHead(String donHead) {
        this.donHead = donHead;
    }

    public String getDonName() {
        return donName;
    }

    public void setDonName(String donName) {
        this.donName = donName;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getDonAddress() {
        return donAddress;
    }

    public void setDonAddress(String donAddress) {
        this.donAddress = donAddress;
    }
}
