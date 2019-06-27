package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 分享提交电话记录
 *
 * @author djcken
 * @date 2018/6/4
 */
@Entity
@Table(name = "share_commit")
public class ShareCommit {

    private int id;
    private int shareUid;//分享用户id
    private int shareType;//分享类型
    private int shareCannel;//分享渠道
    private String commitTel;//提交的电话号码
    private Date commitTime;//提交时间
    private int commitType;//提交类型 1购物车提交 2立即购买提交
    private int commitRegister;//提交后是否注册 1已注册未开太平用户
    private int commitCert;//是否已认证
    private int commitProId;//项目id
    private int commitOrganid;//班组id
    private Integer ccbBankId;//支行

    @Column(name = "ccb_bank_id")
    public Integer getCcbBankId() {
        return ccbBankId;
    }

    public void setCcbBankId(Integer ccbBankId) {
        this.ccbBankId = ccbBankId;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "sr_uid")
    public int getShareUid() {
        return shareUid;
    }

    public void setShareUid(int shareUid) {
        this.shareUid = shareUid;
    }

    @Column(name = "sr_type")
    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    @Column(name = "sr_channel")
    public int getShareCannel() {
        return shareCannel;
    }

    public void setShareCannel(int shareCannel) {
        this.shareCannel = shareCannel;
    }

    @Column(name = "sc_tel")
    public String getCommitTel() {
        return commitTel;
    }

    public void setCommitTel(String commitTel) {
        this.commitTel = commitTel;
    }

    @Column(name = "sc_time")
    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    @Column(name = "sc_type")
    public int getCommitType() {
        return commitType;
    }

    public void setCommitType(int commitType) {
        this.commitType = commitType;
    }

    @Column(name = "sc_reg")
    public int getCommitRegister() {
        return commitRegister;
    }

    public void setCommitRegister(int commitRegister) {
        this.commitRegister = commitRegister;
    }

    @Column(name = "sc_cert")
    public int getCommitCert() {
        return commitCert;
    }

    public void setCommitCert(int commitCert) {
        this.commitCert = commitCert;
    }

    @Column(name = "sc_pro_id")
    public int getCommitProId() {
        return commitProId;
    }

    public void setCommitProId(int commitProId) {
        this.commitProId = commitProId;
    }

    @Column(name = "sc_organ_id")
    public int getCommitOrganid() {
        return commitOrganid;
    }

    public void setCommitOrganid(int commitOrganid) {
        this.commitOrganid = commitOrganid;
    }
}
