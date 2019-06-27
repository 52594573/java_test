package com.zm.entity;

import javax.persistence.Transient;
import java.util.Date;

/**
 * ProOrgan entity. @author MyEclipse Persistence Tools
 */

public class ProOrgan implements java.io.Serializable {

    private Integer id;//机构id
    private Integer proId;//项目id
    private String poName;//机构名称
    private Integer poState;//机构类型
    private Integer poFzr;//负责人
    private Integer poGzid;//工种类型
    private Double poGua;//班组在项目中挂账金额
    private Integer creatUid;//创建人
    private String poZf;//同步政府返回值
    private String groupId;//群组ID
    private Integer poKqzt;//考勤状态 ，1，为必须位置内打卡。2.签到式打卡
    private Integer poRzzt;//认证状态1.身份证实名认证2.人脸采集认证
    private Integer poWzzt;//位置状态 ，1，附近的人可以看到此人。2.隐藏位置
    private Date operationTime;
    private Integer createType=0;
    @Transient
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public ProOrgan setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public Integer getPoState() {
        return poState;
    }

    public void setPoState(Integer poState) {
        this.poState = poState;
    }

    public Integer getPoFzr() {
        return poFzr;
    }

    public void setPoFzr(Integer poFzr) {
        this.poFzr = poFzr;
    }

    public Integer getPoGzid() {
        return poGzid;
    }

    public void setPoGzid(Integer poGzid) {
        this.poGzid = poGzid;
    }

    public Double getPoGua() {
        return poGua;
    }

    public void setPoGua(Double poGua) {
        this.poGua = poGua;
    }

    public String getPoZf() {
        return poZf;
    }

    public void setPoZf(String poZf) {
        this.poZf = poZf;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getPoKqzt() {
        return poKqzt;
    }

    public void setPoKqzt(Integer poKqzt) {
        this.poKqzt = poKqzt;
    }

    public Integer getPoRzzt() {
        return poRzzt;
    }

    public void setPoRzzt(Integer poRzzt) {
        this.poRzzt = poRzzt;
    }

    public Integer getPoWzzt() {
        return poWzzt;
    }

    public void setPoWzzt(Integer poWzzt) {
        this.poWzzt = poWzzt;
    }

    public Integer getCreatUid() {
        return creatUid;
    }

    public void setCreatUid(Integer creatUid) {
        this.creatUid = creatUid;
    }
}