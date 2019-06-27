package com.ktp.project.constant;

public enum CheckManagerEnum {
    WARN(1, "  wl.rate_score BETWEEN 70 AND 75 ", "考核合格，但分数过低，请注意提高您的出勤率以及工作质量和效率。"),
    INFO(2, " wl.rate_score < 70 ", "考核不合格，处于预警状态，请您注意。");

    private Integer type;
    private String sqlCondition;
    private String remark;

    CheckManagerEnum(Integer type, String sqlCondition, String remark) {
        this.type = type;
        this.sqlCondition = sqlCondition;
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public static CheckManagerEnum caseEnumByType(Integer type){
        CheckManagerEnum[] values = CheckManagerEnum.values();
        for (CheckManagerEnum managerEnum : values) {
            if (type.equals(managerEnum.getType())){
                return managerEnum;
            }
        }
        throw new RuntimeException(String.format("根据TYPE: %s 找不到对应的枚举值", type));
    }

}
