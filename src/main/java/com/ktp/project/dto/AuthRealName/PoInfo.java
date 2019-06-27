package com.ktp.project.dto.AuthRealName;
/**
*
* @Description: 班组code
* @Author: liaosh
* @Date: 2019/1/14 0014
*/
public class PoInfo {
    private Integer id;
    private String teamSysNo;
    private Integer poId;
    private String regionCode;

    public PoInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamSysNo() {
        return teamSysNo;
    }

    public void setTeamSysNo(String teamSysNo) {
        this.teamSysNo = teamSysNo;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }
}
