package com.ktp.project.entity;


import javax.persistence.*;

@Entity
@Table(name = "ktp_company_Info")
public class KtpCompanyInfoEntity {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //企业统一社会信用代码或者组织机构码
    @Column(name = "corp_code")
    private String corp_code;
    //企业名称
    @Column(name = "corp_name")
    private String corp_name;
    //区域CODE
    @Column(name = "area_code")
    private String area_code;
    //注册日期，格式yyyy-MM-dd
    @Column(name = "register_date")
    private String register_date;
    //安全生产许可证编号
    @Column(name = "product_no")
    private String product_no;
    //1-总包、2-分包、3-劳务分包
    @Column(name = "compamy_type")
    private Integer compamy_type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorp_code() {
        return corp_code;
    }

    public void setCorp_code(String corp_code) {
        this.corp_code = corp_code;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getProduct_no() {
        return product_no;
    }

    public void setProduct_no(String product_no) {
        this.product_no = product_no;
    }

    public Integer getCompamy_type() {
        return compamy_type;
    }

    public void setCompamy_type(Integer compamy_type) {
        this.compamy_type = compamy_type;
    }
}
