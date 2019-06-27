package com.ktp.project.dto.AuthRealName.ss;

public class SsFindPersonVo {
    private String pass;//	接口安全校验秘钥	String	Y
    private String idcardNum;//	身份证号	String	Y	查询身份证的人员的信息录入情况
    private Integer length;//	每页返回的长度	Int	N	身份证号为空时必填，最少20个
    private Integer index;//	查看第几页	int	N	身份证号为空时必填，从0开始
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

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
}
