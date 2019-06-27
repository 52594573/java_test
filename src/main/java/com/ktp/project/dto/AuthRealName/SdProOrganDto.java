package com.ktp.project.dto.AuthRealName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SdProOrganDto {

    @JsonProperty(value = "Token")
    private String Token;

    @JsonProperty(value = "CompanyNumber")
    private String CompanyNumber;//	String	公司营业执照编号

    @JsonProperty(value = "ProjectNumber")
    private String ProjectNumber	;//	String	项目唯一标识码，可在区平台项目详细信息处获取

    @JsonProperty(value = "TeamName")
    private String TeamName	;//	String	班组名称

    @JsonProperty(value = "ProjectPart")
    private String ProjectPart	;//	String	工程承包内容

    @JsonProperty(value = "ContractSum")
    private Integer ContractSum	;//	Int	承包合同金额

    @JsonProperty(value = "Name")
    private String Name	;//	String	班组长姓名

    @JsonProperty(value = "Tel")
    private String Tel	;//	String	班组长联系方式

    @JsonProperty(value = "IDNum")
    private String IDNum	;//	String	班组长身份证号

    @JsonProperty(value = "EmerPeople")
    private String EmerPeople	;//	String	紧急联系人

    @JsonProperty(value = "EmerTel")
    private String EmerTel	;//	String	紧急联系人电话

    @JsonProperty(value = "ContractID")
    private String ContractID	;//	String	合同文件在文件系统上的ID（调用接口3.10的返回结果），必须上传合同

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    @JsonIgnore
    public void setToken(String token) {
        Token = token;
    }

    @JsonIgnore
    public String getCompanyNumber() {
        return CompanyNumber;
    }

    @JsonIgnore
    public void setCompanyNumber(String companyNumber) {
        CompanyNumber = companyNumber;
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
    public String getTeamName() {
        return TeamName;
    }

    @JsonIgnore
    public void setTeamName(String teamName) {
        TeamName = teamName;
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
    public Integer getContractSum() {
        return ContractSum;
    }

    @JsonIgnore
    public void setContractSum(Integer contractSum) {
        ContractSum = contractSum;
    }

    @JsonIgnore
    public String getName() {
        return Name;
    }

    @JsonIgnore
    public void setName(String name) {
        Name = name;
    }

    @JsonIgnore
    public String getTel() {
        return Tel;
    }

    @JsonIgnore
    public void setTel(String tel) {
        Tel = tel;
    }

    @JsonIgnore
    public String getIDNum() {
        return IDNum;
    }

    @JsonIgnore
    public void setIDNum(String IDNum) {
        this.IDNum = IDNum;
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
    public String getContractID() {
        return ContractID;
    }

    @JsonIgnore
    public void setContractID(String contractID) {
        ContractID = contractID;
    }
}


