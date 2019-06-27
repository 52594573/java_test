package com.ktp.project.dto.AuthRealName.ss;

public class SsFaceIcCardRegistVo {
    private String pass;//	接口安全校验秘钥	String	Y
    private String idcardNum;//	身份证号	String	Y	给指定人员id注册卡号
    private String iccardNumber;//		IC卡卡号	String	Y	IC卡的卡号
    private String deviceKey;//		设备的唯一表示号	String	Y	deviceKey由区平台考勤设备添加接口返回，为设备的唯一标识。

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

    public String getIccardNumber() {
        return iccardNumber;
    }

    public void setIccardNumber(String iccardNumber) {
        this.iccardNumber = iccardNumber;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
}
