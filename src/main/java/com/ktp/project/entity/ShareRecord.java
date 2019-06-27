package com.ktp.project.entity;

import org.apache.ibatis.annotations.Param;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 分享记录
 *
 * @author djcken
 * @date 2018/6/4
 */
@Entity
@Table(name = "share_record")
public class ShareRecord {

    private int id;
    private int shareUid;//分享的用户id
    private int shareType;//分享类型
    private int shareChannel;//分享渠道
    private int shareClick;//分享点击次数
    private Date shareTime;//分享时间
    private Date updateTime;//最后更新时间

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
    public int getShareChannel() {
        return shareChannel;
    }

    public void setShareChannel(int shareChannel) {
        this.shareChannel = shareChannel;
    }

    @Column(name = "sr_click")
    public int getShareClick() {
        return shareClick;
    }

    public void setShareClick(int shareClick) {
        this.shareClick = shareClick;
    }

    @Column(name = "sr_time")
    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    @Column(name = "sr_lasttime")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
