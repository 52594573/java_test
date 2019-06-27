package com.ktp.project.dto.project;

import java.util.Date;

public class ChangeUser {
    private Integer uId;//人员编号
    private String uName;//人员名称
    private Date uTime;//操作时间
    private Integer uType;//0加入1离开

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Date getuTime() {
        return uTime;
    }

    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    public Integer getuType() {
        return uType;
    }

    public void setuType(Integer uType) {
        this.uType = uType;
    }

    public ChangeUser() {
    }
}
