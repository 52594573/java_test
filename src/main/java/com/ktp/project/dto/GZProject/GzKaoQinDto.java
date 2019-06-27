package com.ktp.project.dto.GZProject;


public class GzKaoQinDto {

    private String accessNo;//	M	FL32	接入编号	“信息平台”为施工企业分配的接入编号
    private String atteTime;//	M	FL14	考勤时间	yyyyMMddHHmmss
    private String atteType;//	M	FL4	考勤方式	0：ic卡（默认）1:指纹，2:人脸3:虹膜:4:声纹 5:指静脉
    private String builderIdcard;//	M	VL150	工人身份证号	检查工人是否存在的依据，使用DES加密
    private String builderType;//	M	FL1	人员类型	0=工人1=管理人员（项目经理，总监理工程师，专业监理工程师，劳务员，安全员等项目部人员）
    private String checkChannel;//	M	VL120	考勤通道名称	如有多个考勤机、闸机，上传其考勤通道名称（1号，2号，3号…）
    private String checkType;//	M	FL1	打卡类型	1=入场,2=出场
    private String factoryNum;//	M	FL32	考勤机厂家编号	平台分配给各个厂商的识别编号，区分考勤机上传厂商
    private String timestamp;//	M	FL17	时间戳	北京时间精确到毫秒(yyyyMMddHHmmssSSS)
    private String sign;//	M	FL32	签名	atteImage不参与签名

    public String getAccessNo() {
        return accessNo;
    }

    public void setAccessNo(String accessNo) {
        this.accessNo = accessNo;
    }

    public String getBuilderIdcard() {
        return builderIdcard;
    }

    public void setBuilderIdcard(String builderIdcard) {
        this.builderIdcard = builderIdcard;
    }

    public String getAtteTime() {
        return atteTime;
    }

    public void setAtteTime(String atteTime) {
        this.atteTime = atteTime;
    }

    public String getAtteType() {
        return atteType;
    }

    public void setAtteType(String atteType) {
        this.atteType = atteType;
    }

    public String getCheckChannel() {
        return checkChannel;
    }

    public void setCheckChannel(String checkChannel) {
        this.checkChannel = checkChannel;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getBuilderType() {
        return builderType;
    }

    public void setBuilderType(String builderType) {
        this.builderType = builderType;
    }

    public String getFactoryNum() {
        return factoryNum;
    }

    public void setFactoryNum(String factoryNum) {
        this.factoryNum = factoryNum;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
