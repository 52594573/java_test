package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class EductionLookEntity {
    private Integer id;
    private Integer userId;
    private Integer vId;
    private Integer lNum;
    private Integer lLong;
    private Date lTime;
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    @Column(name = "v_id")
    public Integer getvId() {
        return vId;
    }

    public void setvId(Integer vId) {
        this.vId = vId;
    }
    @Column(name = "l_num")
    public Integer getlNum() {
        return lNum;
    }

    public void setlNum(Integer lNum) {
        this.lNum = lNum;
    }
    @Column(name = "l_long")
    public Integer getlLong() {
        return lLong;
    }

    public void setlLong(Integer lLong) {
        this.lLong = lLong;
    }
    @Column(name = "l_time")
    public Date getlTime() {
        return lTime;
    }

    public void setlTime(Date lTime) {
        this.lTime = lTime;
    }

    public EductionLookEntity() {
    }
}
