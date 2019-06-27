package com.ktp.project.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author mhj code generator
 * @date 2019-04-30 18:21:47
 */
@Entity
@Table(name = "openapi_init_project")
public class OpenapiInitProject implements Serializable {
private static final long serialVersionUID = 1L;

    /**
   * 表ID
   */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
   * 项目ID
   */
    @Column(name = "project_id")
    private Integer projectId;
    /**
   * 请求IP
   */
    @Column(name = "req_ip")
    private String reqIp;
    /**
   * token
   */
    @Column(name = "token")
    private String token;
    /**
   * key
   */
    @Column(name = "secret_key")
    private String secretKey;
    /**
   * 关联service
   */
    @Column(name = "service")
    private String service;
    /**
   * 创建时间
   */
    @Column(name = "create_time")
    private Date createTime;
    /**
   * 更新时间
   */
    @Column(name = "update_time")
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}