package com.ktp.project.dto;


import java.math.BigDecimal;

public class WorkLogWeeklyDetailTeamListDto {

    private long id;
    private String teamname;
    private BigDecimal qualitynum;
    private BigDecimal seriounum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
