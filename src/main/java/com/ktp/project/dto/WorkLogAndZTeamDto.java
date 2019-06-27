package com.ktp.project.dto;


import java.math.BigDecimal;

public class WorkLogAndZTeamDto {

    private Integer id;
    private String teamname;
    private BigDecimal qualitynum;
    private BigDecimal seriounum;
    private Integer wlgid;
    private Integer wlid;
    private Integer wl_yzcd;
    private String wl_content;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public BigDecimal getQualitynum() {
        return qualitynum;
    }

    public void setQualitynum(BigDecimal qualitynum) {
        this.qualitynum = qualitynum;
    }

    public BigDecimal getSeriounum() {
        return seriounum;
    }

    public void setSeriounum(BigDecimal seriounum) {
        this.seriounum = seriounum;
    }

    public Integer getWlgid() {
        return wlgid;
    }

    public void setWlgid(Integer wlgid) {
        this.wlgid = wlgid;
    }

    public Integer getWlid() {
        return wlid;
    }

    public void setWlid(Integer wlid) {
        this.wlid = wlid;
    }

    public Integer getWl_yzcd() {
        return wl_yzcd;
    }

    public void setWl_yzcd(Integer wl_yzcd) {
        this.wl_yzcd = wl_yzcd;
    }

    public String getWl_content() {
        return wl_content;
    }

    public void setWl_content(String wl_content) {
        this.wl_content = wl_content;
    }
}
