package com.ktp.project.dto.GZProject;

public class GzJoinOrOutWorkerDto {

    private String accessNo;//	M	FL32	接入编号	“信息平台”为施工企业项目分配的接入编号
    private String teamCode;//	M	VL320	班组编码	工程内部的班组唯一编码，如果班组编码已存在则修改班组，使用DES加密
    private String idcard;//	M	VL150	工人身份证号	检查工人是否存在的依据，使用DES加密
    private String type;//	M	FL1	类型	0：进场；1：退场
    private String doDate;//	M	FL8	进退场日期	yyyyMMdd
    private String timestamp;//	M	FL17	时间戳	北京时间精确到毫秒(yyyyMMddHHmmssSSS)
    private String sign;//	M	FL32	签名

    public String getAccessNo() {
        return accessNo;
    }

    public void setAccessNo(String accessNo) {
        this.accessNo = accessNo;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
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
