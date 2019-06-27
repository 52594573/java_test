package com.ktp.project.constant;

public enum JobTypeEnum {
    CONSTRUCT_WORKER(117, "施工员"), QUALITY_WORKER(180, "质量员"), SAFETY_WORKER(181, "安全员"),
    ;

    JobTypeEnum(Integer type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    private Integer type;
    private String remark;

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

    public static JobTypeEnum caseEnumByType(Integer type){
        JobTypeEnum[] values = JobTypeEnum.values();
        for (JobTypeEnum jobType : values) {
            if (type.equals(jobType.getType())){
                return jobType;
            }
        }
        throw new RuntimeException(String.format("根据TYPE: %s 找不到对应的枚举值", type));
    }
}
