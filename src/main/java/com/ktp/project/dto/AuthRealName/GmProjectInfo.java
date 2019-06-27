package com.ktp.project.dto.AuthRealName;

import java.util.Date;

/**
 * 高明项目对开太平项目
 */
public class GmProjectInfo {
    //主键
    private Integer id;
    //企业code
    private String corpCode;
    //企业名称
    private String corpName;
    //项目code
    private String projectCode;
    //项目id
    private Integer projectId;
    //班组Code
    private String teamCode;
    //类型
    private String regionCode;
    //进场时间年月日 时分秒
    private String corpIntime;

    //进场时间年月日
    private String corpIntimeDay;
    //进场时间年月日
    private Date corp_intime_date;

    public Date getCorp_intime_date() {
        return corp_intime_date;
    }

    public void setCorp_intime_date(Date corp_intime_date) {
        this.corp_intime_date = corp_intime_date;
    }

    public String getCorpIntimeDay() {
        return corpIntimeDay;
    }

    public void setCorpIntimeDay(String corpIntimeDay) {
        this.corpIntimeDay = corpIntimeDay;
    }

    public String getCorpIntime() {
        return corpIntime;
    }

    public void setCorpIntime(String corpIntime) {
        this.corpIntime = corpIntime;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
