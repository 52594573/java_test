package com.ktp.project.entity;


import javax.persistence.*;

@Entity
@Table(name = "ktp_project_info")
public class KtpProjectInfoEntity {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //总承包单位统一社会信用代码
    @Column(name = "contractor_corp_code")
    private String contractor_corp_code;
    //总承包单位名称
    @Column(name = "contractor_corp_name")
    private String contractor_corp_name;
    //项目id
    @Column(name = "project_id")
    private Integer project_id;
    //建设单位统一社会信用代码
    @Column(name = "build_corp_code")
    private String build_corp_code;
    //建设单位名称
    @Column(name = "build_corp_name")
    private String build_corp_name;

    //施工许可证,json数组
    @Column(name = "builderLicenses")
    private String builderLicenses;
    //区域code
    @Column(name = "area_code")
    private String area_code;

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getBuilderLicenses() {
        return builderLicenses;
    }

    public void setBuilderLicenses(String builderLicenses) {
        this.builderLicenses = builderLicenses;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractor_corp_code() {
        return contractor_corp_code;
    }

    public void setContractor_corp_code(String contractor_corp_code) {
        this.contractor_corp_code = contractor_corp_code;
    }

    public String getContractor_corp_name() {
        return contractor_corp_name;
    }

    public void setContractor_corp_name(String contractor_corp_name) {
        this.contractor_corp_name = contractor_corp_name;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public String getBuild_corp_code() {
        return build_corp_code;
    }

    public void setBuild_corp_code(String build_corp_code) {
        this.build_corp_code = build_corp_code;
    }

    public String getBuild_corp_name() {
        return build_corp_name;
    }

    public void setBuild_corp_name(String build_corp_name) {
        this.build_corp_name = build_corp_name;
    }
}
