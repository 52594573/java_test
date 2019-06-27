package com.ktp.project.dto.clockin;

import com.ktp.project.entity.ProxyClockIn;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 代理打卡记录
 * Created by LinHon 2018/12/4
 */
public class ProxyClockInDetailsDto {

    //上班还是下班卡  1该上班 2该下班
    private Integer onOroff;

    //服务器时间
    private Date serverTime;
    //项目经度
    private String pLongitude;

    //项目纬度
    private String pLatitude;

    //项目打卡范围
    private String projectFW;

    List<ProxyClockIn> clockIns;

    public Integer getOnOroff() {
        return onOroff;
    }

    public void setOnOroff(Integer onOroff) {
        this.onOroff = onOroff;
    }

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public String getProjectFW() {
        return projectFW;
    }

    public void setProjectFW(String projectFW) {
        this.projectFW = projectFW;
    }

    public List<ProxyClockIn> getClockIns() {
        return clockIns;
    }

    public void setClockIns(List<ProxyClockIn> clockIns) {
        this.clockIns = clockIns;
    }

    public String getpLongitude() {
        return pLongitude;
    }

    public void setpLongitude(String pLongitude) {
        this.pLongitude = pLongitude;
    }

    public String getpLatitude() {
        return pLatitude;
    }

    public void setpLatitude(String pLatitude) {
        this.pLatitude = pLatitude;
    }

    public ProxyClockInDetailsDto() {
    }
}
