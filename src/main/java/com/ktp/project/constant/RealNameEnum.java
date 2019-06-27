package com.ktp.project.constant;

public enum RealNameEnum {
    UPLOAD_COMPANY_INFO(1, "上传企业信息"),
    UPLOAD_PROJECT_INFO(2, "上传项目信息"),
    UPLOAD_PARTICIPATE_INFO(3, "上传参建单位信息"),
    UPDATE_PARTICIPATE_INFO(4, "修改参建单位信息"),
    UPLOAD_PROORGAN_INFO(5, "上传班组信息"),
    UPDATE_PROORGAN_INFO(6, "修改班组信息"),
    UPLOAD_WORKER_INFO(7, "上传项目工人信息"),
    UPDATE_WORKER_INFO(8, "修改项目工人信息"),
    WORKER_JOIN(9, "工人进场"),
    WORKER_OUT(10, "员工进场"),
    UPLOAD_CONTRACT(11, "上传劳动合同"),
    UPLOAD_PAYROLL(12, "上传项目工资单"),
    UPLOAD_PAYROLL_DETAIL(13, "上传项目工资单详细信息"),
    UPLOAD_TRAIN_INFO(14, "上传项目培训课程信息"),
    UPLOAD_TRAIN(15, "上传项目培训课程人员信息"),
    UPLOAD_CREDENTIAL(16, "上传人员资质信息"),
    UPLOAD_REGISTER(17, "上传人员注册信息"),
    UPLOAD_DEVICEINFO(18, "上传设备绑定信息"),
    ;

    RealNameEnum(Integer type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    private Integer type;
    private String url;
    private String remark;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static RealNameEnum caseEnumByType(Integer type) {
        RealNameEnum[] values = RealNameEnum.values();
        for (RealNameEnum jobType : values) {
            if (type.equals(jobType.getType())) {
                return jobType;
            }
        }
        throw new RuntimeException(String.format("根据TYPE: %s 找不到对应的枚举值", type));
    }
}
