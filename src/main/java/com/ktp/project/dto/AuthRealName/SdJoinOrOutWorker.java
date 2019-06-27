package com.ktp.project.dto.AuthRealName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 顺德项目进退场员工信息
 */
public class SdJoinOrOutWorker {

    @JsonProperty(value = "Token")
    private String Token;

    @JsonProperty(value = "ProjectPart")
    private String ProjectPart;

    @JsonProperty(value = "ProjectNumber")
    private String ProjectNumber;

    @JsonProperty(value = "IDcardNum")
    private String IDcardNum;

    @JsonProperty(value = "joinTime")
    private String joinTime;

    @JsonProperty(value = "outTime")
    private String outTime;

    @JsonProperty(value = "IsIn")
    private boolean IsIn;

    @JsonProperty(value = "Bank")
    private String Bank;

    @JsonProperty(value = "Account")
    private String Account;

    @JsonProperty(value = "TeamName")
    private String TeamName;

    @JsonProperty(value = "HappenTime")
    private String HappenTime;

    @JsonProperty(value = "ContractID")
    private String ContractID;

    @JsonProperty(value = "EmerPeople")
    private String EmerPeople;

    @JsonProperty(value = "EmerTel")
    private String EmerTel;

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    @JsonIgnore
    public void setToken(String token) {
        Token = token;
    }

    @JsonIgnore
    public boolean isIn() {
        return IsIn;
    }

    @JsonIgnore
    public void setIn(boolean in) {
        IsIn = in;
    }

    @JsonIgnore
    public String getProjectPart() {
        return ProjectPart;
    }

    @JsonIgnore
    public void setProjectPart(String projectPart) {
        ProjectPart = projectPart;
    }

    @JsonIgnore
    public String getProjectNumber() {
        return ProjectNumber;
    }

    @JsonIgnore
    public void setProjectNumber(String projectNumber) {
        ProjectNumber = projectNumber;
    }

    @JsonIgnore
    public String getIDcardNum() {
        return IDcardNum;
    }

    @JsonIgnore
    public void setIDcardNum(String IDcardNum) {
        this.IDcardNum = IDcardNum;
    }

    @JsonIgnore
    public String getContractID() {
        return ContractID;
    }

    @JsonIgnore
    public void setContractID(String contractID) {
        ContractID = contractID;
    }

    @JsonIgnore
    public String getEmerPeople() {
        return EmerPeople;
    }

    @JsonIgnore
    public void setEmerPeople(String emerPeople) {
        EmerPeople = emerPeople;
    }

    @JsonIgnore
    public String getEmerTel() {
        return EmerTel;
    }

    @JsonIgnore
    public void setEmerTel(String emerTel) {
        EmerTel = emerTel;
    }

    @JsonIgnore
    public String getHappenTime() {
        return HappenTime;
    }

    @JsonIgnore
    public void setHappenTime(String happenTime) {
        HappenTime = happenTime;
    }

    @JsonIgnore
    public String getJoinTime() {
        return joinTime;
    }

    @JsonIgnore
    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    @JsonIgnore
    public String getOutTime() {
        return outTime;
    }

    @JsonIgnore
    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    @JsonIgnore
    public String getBank() {
        return Bank;
    }

    @JsonIgnore
    public void setBank(String bank) {
        Bank = bank;
    }

    @JsonIgnore
    public String getAccount() {
        return Account;
    }

    @JsonIgnore
    public void setAccount(String account) {
        Account = account;
    }

    @JsonIgnore
    public String getTeamName() {
        return TeamName;
    }

    @JsonIgnore
    public void setTeamName(String teamName) {
        TeamName = teamName;
    }
}
