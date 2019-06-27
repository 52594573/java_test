package com.ktp.project.dto.project;

public class PoMessage {
    private Integer poId;//班组编号
    private String poName;//班组名称
    private Integer poMonitorId;//班组长编号
    private String poMonitorName;//班组长名称
    private Integer poProId;//所属项目编号
    private String poProName;//项目名称
    private String poPkCorpFinance;//劳务分包企业用户编号
    private String poCorpNameFinance;//劳务分包企业全称
    private String poCorpIdCodeFinance;//劳务分包企业证件编号

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public Integer getPoMonitorId() {
        return poMonitorId;
    }

    public void setPoMonitorId(Integer poMonitorId) {
        this.poMonitorId = poMonitorId;
    }

    public String getPoMonitorName() {
        return poMonitorName;
    }

    public void setPoMonitorName(String poMonitorName) {
        this.poMonitorName = poMonitorName;
    }

    public Integer getPoProId() {
        return poProId;
    }

    public void setPoProId(Integer poProId) {
        this.poProId = poProId;
    }

    public String getPoProName() {
        return poProName;
    }

    public void setPoProName(String poProName) {
        this.poProName = poProName;
    }


    public String getPoPkCorpFinance() {
        return poPkCorpFinance;
    }

    public void setPoPkCorpFinance(String poPkCorpFinance) {
        this.poPkCorpFinance = poPkCorpFinance;
    }

    public String getPoCorpNameFinance() {
        return poCorpNameFinance;
    }

    public void setPoCorpNameFinance(String poCorpNameFinance) {
        this.poCorpNameFinance = poCorpNameFinance;
    }

    public String getPoCorpIdCodeFinance() {
        return poCorpIdCodeFinance;
    }

    public void setPoCorpIdCodeFinance(String poCorpIdCodeFinance) {
        this.poCorpIdCodeFinance = poCorpIdCodeFinance;
    }

    public PoMessage() {
    }
}
