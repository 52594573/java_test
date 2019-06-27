package com.ktp.project.dto.AuthRealName;

import java.math.BigInteger;

/**
 * 江门项目考勤dto
 */
public class JmAndGsxProAttendanceDto {
    private Integer projectId;
    private Integer userId;
    private String identityCode;//工人身份证号
    private BigInteger type;//刷卡进出方向。1：进；0：出
    private String checkDate;//考勤时间
    private String other;//备注
    private String faceUrl;//打卡照片

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public BigInteger getType() {
        return type;
    }

    public void setType(BigInteger type) {
        this.type = type;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }
}
