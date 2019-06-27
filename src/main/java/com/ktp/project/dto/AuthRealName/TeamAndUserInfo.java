package com.ktp.project.dto.AuthRealName;

import java.util.Date;

/**
 * 班组、用户id
 */
public class TeamAndUserInfo {
    //班组id
    private Integer teamId;
    //用户id
    private Integer userId;

    //班组名称
    private String poName;

    public String getPoName() {
        return poName;
    }

    public void setPoName(String poName) {
        this.poName = poName;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
