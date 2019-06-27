package com.ktp.project.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;

/**
 * ChatFriend entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "chat_friend")
public class ChatFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 主用户ID
     */
    @Column(name = "left_uid")
    private int leftUid;

    /**
     * 副用户ID
     */
    @Column(name = "right_uid")
    private int rightUid;

    /**
     * 上次更新时间
     */
    @Column(name = "last_time")
    private Timestamp lastTime;

    /**
     * 1：待同意
     * 2: 已同意
     * 3: 已删除
     * 4. 拒绝添加
     */
    @Column(name = "relationType")
    private int relationType;

    /**
     * 添加好友备注
     */
    @Column(name = "apply_msg")
    private String applyMsg;


    @Transient
    private Map<String, Object> rightUserInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftUid() {
        return leftUid;
    }

    public void setLeftUid(int leftUid) {
        this.leftUid = leftUid;
    }

    public int getRightUid() {
        return rightUid;
    }

    public void setRightUid(int rightUid) {
        this.rightUid = rightUid;
    }

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public String getApplyMsg() {
        return applyMsg;
    }

    public void setApplyMsg(String applyMsg) {
        this.applyMsg = applyMsg;
    }

    public Map<String, Object> getRightUserInfo() {
        return rightUserInfo;
    }

    public void setRightUserInfo(Map<String, Object> rightUserInfo) {
        this.rightUserInfo = rightUserInfo;
    }

}