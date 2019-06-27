package com.ktp.project.dto.project;

import java.util.Date;

public class PoUser {
    private Integer poId;//班组编号
    private Integer uId;//人员编号
    private Integer uStatus;//状态（0正常。4.停止使用）
    private String uTypeName;//工种类型名称
    private Double uSkillScore;//在项目内技能分
    private Double uCreditScore;//信用分
    private Date uIntime;//在项目中开始时间
    private String uEndtime;//在项目中结束时间
    private Integer uCard;//考勤卡号
    private String uFacePicture;//人脸照
    private String uIDCardFront;//身份证正面照
    private String uIDCardReverse;//身份证反面照
    private String uBustShot;//半身照
    private Integer uSgzt;//工人在项目中的施工状态1为正在施工 2.离场
    private String uBankId;//该班组中银行卡号

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Integer getuStatus() {
        return uStatus;
    }

    public void setuStatus(Integer uStatus) {
        this.uStatus = uStatus;
    }

    public String getuTypeName() {
        return uTypeName;
    }

    public void setuTypeName(String uTypeName) {
        this.uTypeName = uTypeName;
    }

    public Double getuSkillScore() {
        return uSkillScore;
    }

    public void setuSkillScore(Double uSkillScore) {
        this.uSkillScore = uSkillScore;
    }

    public Double getuCreditScore() {
        return uCreditScore;
    }

    public void setuCreditScore(Double uCreditScore) {
        this.uCreditScore = uCreditScore;
    }

    public Date getuIntime() {
        return uIntime;
    }

    public void setuIntime(Date uIntime) {
        this.uIntime = uIntime;
    }

    public String getuEndtime() {
        return uEndtime;
    }

    public void setuEndtime(String uEndtime) {
        this.uEndtime = uEndtime;
    }

    public Integer getuCard() {
        return uCard;
    }

    public void setuCard(Integer uCard) {
        this.uCard = uCard;
    }

    public String getuFacePicture() {
        return uFacePicture;
    }

    public void setuFacePicture(String uFacePicture) {
        this.uFacePicture = uFacePicture;
    }

    public String getuIDCardFront() {
        return uIDCardFront;
    }

    public void setuIDCardFront(String uIDCardFront) {
        this.uIDCardFront = uIDCardFront;
    }

    public String getuIDCardReverse() {
        return uIDCardReverse;
    }

    public void setuIDCardReverse(String uIDCardReverse) {
        this.uIDCardReverse = uIDCardReverse;
    }

    public String getuBustShot() {
        return uBustShot;
    }

    public void setuBustShot(String uBustShot) {
        this.uBustShot = uBustShot;
    }

    public Integer getuSgzt() {
        return uSgzt;
    }

    public void setuSgzt(Integer uSgzt) {
        this.uSgzt = uSgzt;
    }

    public String getuBankId() {
        return uBankId;
    }

    public void setuBankId(String uBankId) {
        this.uBankId = uBankId;
    }

    public PoUser() {
    }
}
