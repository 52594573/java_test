package com.ktp.project.entity;

import javax.persistence.*;


@Entity
@Table(name = "real_name_project")
public class RealNameProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//int(11) NOT NULL表ID

    @Column(name = "project_code")
    private String projectCode;//varchar(255) NULL项目CODE

    @Column(name = "project_name")
    private String projectName;//varchar(255) NULL项目名称

    @Column(name = "project_id")
    private Integer projectId;//int(11) NULL项目ID,关联projec表主键

    @Column(name = "company_id")
    private Integer companyId;//int(11) NULL所属公司,关联real_name_company表主键

    @Column(name = "project_area")
    private String projectArea;//varchar(25) NULL所属区域

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
