package com.ktp.project.dto.AuthRealName;

import com.ktp.project.constant.RealNameConfig;

public class OutWorkerDto {

    private String sessionToken;// ${token} 令牌 *
    private String idNumber;// string ⾝份证 *
    private String code= RealNameConfig.PROJECT_CODE;;// string 项⺫编号,⼈员进场时填写的项⺫编号

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
}
