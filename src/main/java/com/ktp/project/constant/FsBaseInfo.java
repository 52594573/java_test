package com.ktp.project.constant;

public enum FsBaseInfo {
    SHUN_DE("SD", RealNameConfig.SD_BASE_PATH, RealNameConfig.SD_API_TOKEN, RealNameConfig.SD_DEVICEKEY, "佛山顺德项目基本信息"),
    SHAN_SHUI("SS", RealNameConfig.SS_BASE_REQUEST, RealNameConfig.SS_TOKEN, RealNameConfig.SS_DEVICEKEY, "佛山山水项目基本信息")
    ;

    private String pSent;
    private String reqIp;
    private String token;
    private String deviceKey;
    private String remark;

    FsBaseInfo(String pSent, String reqIp, String token, String deviceKey, String remark) {
        this.pSent = pSent;
        this.reqIp = reqIp;
        this.token = token;
        this.deviceKey = deviceKey;
        this.remark = remark;
    }

    public String getpSent() {
        return pSent;
    }

    public void setpSent(String pSent) {
        this.pSent = pSent;
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static FsBaseInfo caseEnumByType(String pSent) {
        FsBaseInfo[] values = FsBaseInfo.values();
        for (FsBaseInfo jobType : values) {
            if (pSent.equals(jobType.getpSent())) {
                return jobType;
            }
        }
        throw new RuntimeException(String.format("根据TYPE: %s 找不到对应的枚举值", pSent));
    }
}
