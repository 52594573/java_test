package com.ktp.project.entity;

import javax.persistence.*;

@Entity
@Table(name = "massage_switch")
public class MassageSwitch {
    private Integer id;
    private Integer mConfigId;//对应massage_config表id
    private Integer mUserId;//用户id
    private Integer mRoleId;//角色id
    private Integer mAppId;//app标识id
    private Integer mStatus;//状体
    private Integer mTypeId;//类型
    private String mName;//类型名称
    private Integer mProId;//项目id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "m_config_id")
    public Integer getmConfigId() {
        return mConfigId;
    }

    public void setmConfigId(Integer mConfigId) {
        this.mConfigId = mConfigId;
    }
    @Column(name = "m_user_id")
    public Integer getmUserId() {
        return mUserId;
    }

    public void setmUserId(Integer mUserId) {
        this.mUserId = mUserId;
    }
    @Column(name = "m_role_id")
    public Integer getmRoleId() {
        return mRoleId;
    }

    public void setmRoleId(Integer mRoleId) {
        this.mRoleId = mRoleId;
    }
    @Column(name = "m_app_id")
    public Integer getmAppId() {
        return mAppId;
    }

    public void setmAppId(Integer mAppId) {
        this.mAppId = mAppId;
    }
    @Column(name = "m_status")
    public Integer getmStatus() {
        return mStatus;
    }

    public void setmStatus(Integer mStatus) {
        this.mStatus = mStatus;
    }
    @Column(name = "m_type_id")
    public Integer getmTypeId() {
        return mTypeId;
    }

    public void setmTypeId(Integer mTypeId) {
        this.mTypeId = mTypeId;
    }
    @Column(name = "m_name")
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
    @Column(name = "m_pro_id")
    public Integer getmProId() {
        return mProId;
    }

    public void setmProId(Integer mProId) {
        this.mProId = mProId;
    }

    public MassageSwitch() {
    }
}
