package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author djcken
 * @date 2018/5/24
 */
@Entity
@Table(name = "user_token")
public class UserToken {

    private int id;
    private String myImei;
    private String token;
    private String userId;
    private Date tokenTime;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_imea")
    public String getMyImei() {
        return myImei;
    }

    public void setMyImei(String myImei) {
        this.myImei = myImei;
    }

    @Column(name = "user_token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "token_time")
    public Date getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Date tokenTime) {
        this.tokenTime = tokenTime;
    }
}
