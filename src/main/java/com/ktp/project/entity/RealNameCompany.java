package com.ktp.project.entity;

import javax.persistence.*;

@Entity
@Table(name = "share_record")
public class RealNameCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_name")
    private String companyNme;//公司名称

    @Column(name = "credit_code")
    private String creditCode;//统一社会信用代码

    @Column(name = "company_type")
    private Integer companyType;

    @Column(name = "company_manager")
    private String companyManager;

    @Column(name = "manager_mobile")
    private String managerMobile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCompanyManager() {
        return companyManager;
    }

    public void setCompanyManager(String companyManager) {
        this.companyManager = companyManager;
    }

    public String getManagerMobile() {
        return managerMobile;
    }

    public void setManagerMobile(String managerMobile) {
        this.managerMobile = managerMobile;
    }
}
