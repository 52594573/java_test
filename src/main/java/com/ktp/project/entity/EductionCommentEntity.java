package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class EductionCommentEntity {
    private Integer id;
    private Integer vId;
    private Integer userId;
    private String cContent;
    private Date cTime;
    private Integer cStatus;
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "v_id")
    public Integer getvId() {
        return vId;
    }

    public void setvId(Integer vId) {
        this.vId = vId;
    }
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    @Column(name = "c_content")
    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }
    @Column(name = "c_time")
    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }
    @Column(name = "c_status")
    public Integer getcStatus() {
        return cStatus;
    }

    public void setcStatus(Integer cStatus) {
        this.cStatus = cStatus;
    }

    public EductionCommentEntity() {
    }
}
