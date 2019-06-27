package com.ktp.project.dto.AuthRealName;

public class SdProjectInfoDto {

    private String projectCode;//varchar(255) NULL项目CODE
    private String projectName;//varchar(255) NULL项目名称
    private Integer projectId;//int(11) NULL项目ID,关联projec表主键
    private Integer companyId;//int(11) NULL所属公司,关联real_name_company表主键
    private String projectArea;//varchar(25) NULL所属区域
    private String companyNme;//公司名称
    private String creditCode;//统一社会信用代码
    private Integer companyType;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getProjectArea() {
        return projectArea;
    }

    public void setProjectArea(String projectArea) {
        this.projectArea = projectArea;
    }

    public String getCompanyNme() {
        return companyNme;
    }

    public void setCompanyNme(String companyNme) {
        this.companyNme = companyNme;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }
}
