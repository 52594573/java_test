package com.ktp.project.dto.AuthRealName.ss;


import java.util.Date;

public class SsFindRecordsVo {
    private String pass;//	接口安全校验秘钥	String	Y
    private String idcardNum;//	身份证号	String	Y	查询指定人员识别记录
    private Integer length;//	每页显示数据数量	Int	Y	传入-1为不分页
    private Integer index;//	查询页码	Int	Y	页码，从0开始
    private Date startTime;//	记录开始时间	Date	Y	不按时间查询，请分别传入0
    private Date endTime;//	记录结束时间	Date	Y
    private String deviceKey;//	设备的唯一表示号	String	Y	deviceKey由区平台考勤设备添加接口返回，为设备的唯一标识。

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIdcardNum() {
        return idcardNum;
    }

    public void setIdcardNum(String idcardNum) {
        this.idcardNum = idcardNum;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
}
