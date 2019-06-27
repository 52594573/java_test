package com.ktp.project.dto.AuthRealName;

import com.ktp.project.constant.RealNameConfig;

import java.math.BigInteger;

/**
 * 工作考勤
 */
public class WorkAttendanceDto {

    private String sessionToken;// ${token} 令牌 *
    private String idNumber;// string ⾝份证 *
    private String code = RealNameConfig.PROJECT_CODE;;// string 项⺫编号,⼈员进场时填写的项⺫编号 *
    private String time;// string 时间${date}: yyyy-MM-dd HH:mm:ss *
    private BigInteger status;// BigInteger 进出⻔状态${atdStatus} *
    private String pic;// string ⼈脸识别认证后正脸清晰图⽚，base64编码的字符串,200KB⼤⼩以内
    private Integer userId;
    private Integer projectId;

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

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
