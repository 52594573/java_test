package com.ktp.project.entity;


import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by LinHon 2018/8/3
 */
@Entity
@Table(name = "sms")
public class Sms {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "code")
    private String code;

    @Column(name = "type")
    private int type;

    @Column(name = "createTime")
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());

    public Sms() {
    }

    public Sms(String mobile, String code, int type) {
        this.mobile = mobile;
        this.code = code;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
