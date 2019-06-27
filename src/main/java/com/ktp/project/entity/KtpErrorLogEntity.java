package com.ktp.project.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ktp_error_log")
public class KtpErrorLogEntity {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //项目id
    @Column(name = "project_id")
    private Integer project_id;
    //错误信息
    @Column(name = "error_info")
    private String error_info;
    //调用的接口
    @Column(name = "api_method")
    private String api_method;
    //地区
    @Column(name = "region_code")
    private String region_code;
    //访问错误的url
    @Column(name = "api_url")
    private String api_url;
    //参数
    @Column(name = "parm_str")
    private String parm_str;

    public String getParm_str() {
        return parm_str;
    }

    public void setParm_str(String parm_str) {
        this.parm_str = parm_str;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public String getApi_method() {
        return api_method;
    }

    public void setApi_method(String api_method) {
        this.api_method = api_method;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getApi_url() {
        return api_url;
    }

    public void setApi_url(String api_url) {
        this.api_url = api_url;
    }
}
