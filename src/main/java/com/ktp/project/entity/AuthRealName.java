package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "auth_real_name")
public class AuthRealName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "is_syn_join")
    private Integer isSynJoin = 0;//是否同步进场信息 0 否 1 是

    @Column(name = "is_syn_out")
    private Integer isSynOut = 0;//是否同步离场信息 0 否 1 是

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;//更新时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsSynOut() {
        return isSynOut;
    }

    public void setIsSynOut(Integer isSynOut) {
        this.isSynOut = isSynOut;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsSynJoin() {
        return isSynJoin;
    }

    public void setIsSynJoin(Integer isSynJoin) {
        this.isSynJoin = isSynJoin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
