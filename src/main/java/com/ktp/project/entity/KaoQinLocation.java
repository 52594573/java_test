package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author djcken
 * @date 2018/6/20
 */
@Entity
@Table(name = "kaoqin_location")
public class KaoQinLocation {

    private int id;
    private int userId;//用户id
    private int proId;//项目id
    private String location;//位置信息（json字符串，例如"[{"x":23.148234,"y":113.291483,"time":"2017-07-01 00:00:00"}]"）
    private String time;//某天（格式2018-08-09）

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "u_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "pro_id")
    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    @Column(name = "kl_location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "kl_time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
