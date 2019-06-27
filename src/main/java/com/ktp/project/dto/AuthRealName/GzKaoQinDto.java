package com.ktp.project.dto.AuthRealName;

import java.math.BigInteger;
import java.util.Date;

public class GzKaoQinDto {

    private Integer projectId;//项目ID
    private String projectName;//项目名称
    private Integer proOrganId;//班组ID
    private String proOrganName;//班组名称
    private Integer userId;
    private String name;
    private BigInteger kState;//进出状态
    private Date kqTime;
    private String kqPic;//考勤图片
    private Integer synState;//同步状态

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProOrganId() {
        return proOrganId;
    }

    public void setProOrganId(Integer proOrganId) {
        this.proOrganId = proOrganId;
    }

    public String getProOrganName() {
        return proOrganName;
    }

    public void setProOrganName(String proOrganName) {
        this.proOrganName = proOrganName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getkState() {
        return kState;
    }

    public void setkState(BigInteger kState) {
        this.kState = kState;
    }

    public Date getKqTime() {
        return kqTime;
    }

    public void setKqTime(Date kqTime) {
        this.kqTime = kqTime;
    }

    public String getKqPic() {
        return kqPic;
    }

    public void setKqPic(String kqPic) {
        this.kqPic = kqPic;
    }

    public Integer getSynState() {
        return synState;
    }

    public void setSynState(Integer synState) {
        this.synState = synState;
    }
}
