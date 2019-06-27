package com.zm.entity;


import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * 推荐码记录
 *
 * @author tb
 * @date 2019/1/3
 */
@Entity
public class CcbPush implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getCcbid() {
        return ccbid;
    }

    public void setCcbid(String ccbid) {
        this.ccbid = ccbid;
    }
    public Date getInitime() {
        return initime;
    }

    public void setInitime(Date initime) {
        this.initime = initime;
    }

    private int uid;//用户id
    private String ccbid;//推荐码
    private Date initime;//初始化时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
