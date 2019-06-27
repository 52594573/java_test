package com.ktp.project.dto.project;

import java.util.Date;

public class WageUser {
    private Integer uId;//人员编号
    private String uName;//人员名称
    private Date wTime;//工资单日期
    private Double wWage;//工资金额
    private Double wWageSent;//已发放的工资
    private Integer wpoId;//班组编号
    private Integer wYear;//年份
    private Integer wMonth;//月份
    private Integer wCertificateType;//证件类型
    private String wCardType;//证件类型
    private String wCard;//证件号码
    private String wBankCard;//工资卡号
    private String wBankNCode;//工资卡银行代码
    private String wBankName;//工资开户行名称
    private Date wGrantTime;//发放时间
    private Integer proId;//平台为项目分配的接入编码
    private String wCorpCode;//工人所属企业统一社会信用代码
    private String wCorpName;//工人所属企业名称

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Date getwTime() {
        return wTime;
    }

    public void setwTime(Date wTime) {
        this.wTime = wTime;
    }

    public Double getwWage() {
        return wWage;
    }

    public void setwWage(Double wWage) {
        this.wWage = wWage;
    }

    public Double getwWageSent() {
        return wWageSent;
    }

    public void setwWageSent(Double wWageSent) {
        this.wWageSent = wWageSent;
    }

    public Integer getWpoId() {
        return wpoId;
    }

    public void setWpoId(Integer wpoId) {
        this.wpoId = wpoId;
    }

    public Integer getwYear() {
        return wYear;
    }

    public void setwYear(Integer wYear) {
        this.wYear = wYear;
    }

    public Integer getwMonth() {
        return wMonth;
    }

    public void setwMonth(Integer wMonth) {
        this.wMonth = wMonth;
    }

    public Integer getwCertificateType() {
        return wCertificateType;
    }

    public void setwCertificateType(Integer wCertificateType) {
        this.wCertificateType = wCertificateType;
    }

    public String getwCardType() {
        return wCardType;
    }

    public void setwCardType(String wCardType) {
        this.wCardType = wCardType;
    }

    public String getwCard() {
        return wCard;
    }

    public void setwCard(String wCard) {
        this.wCard = wCard;
    }

    public String getwBankCard() {
        return wBankCard;
    }

    public void setwBankCard(String wBankCard) {
        this.wBankCard = wBankCard;
    }

    public String getwBankNCode() {
        return wBankNCode;
    }

    public void setwBankNCode(String wBankNCode) {
        this.wBankNCode = wBankNCode;
    }

    public String getwBankName() {
        return wBankName;
    }

    public void setwBankName(String wBankName) {
        this.wBankName = wBankName;
    }

    public Date getwGrantTime() {
        return wGrantTime;
    }

    public void setwGrantTime(Date wGrantTime) {
        this.wGrantTime = wGrantTime;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getwCorpCode() {
        return wCorpCode;
    }

    public void setwCorpCode(String wCorpCode) {
        this.wCorpCode = wCorpCode;
    }

    public String getwCorpName() {
        return wCorpName;
    }

    public void setwCorpName(String wCorpName) {
        this.wCorpName = wCorpName;
    }

    public WageUser() {
    }
}
