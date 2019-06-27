package com.ktp.project.dto.education;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "edu_like")
public class EductionLikeDto {
    private Integer id;
    private Integer userId;
    private Integer vId;
    private Integer lStatus;
    private Long vLike;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getvId() {
        return vId;
    }

    public void setvId(Integer vId) {
        this.vId = vId;
    }

    public Integer getlStatus() {
        return lStatus;
    }

    public void setlStatus(Integer lStatus) {
        this.lStatus = lStatus;
    }

    public Long getvLike() {
        return vLike;
    }

    public void setvLike(Long vLike) {
        this.vLike = vLike;
    }

    public EductionLikeDto() {
    }
}
