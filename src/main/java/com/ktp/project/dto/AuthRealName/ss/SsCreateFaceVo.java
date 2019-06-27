package com.ktp.project.dto.AuthRealName.ss;


public class SsCreateFaceVo {
    private String pass;//	接口安全校验秘钥	String	Y
    private String idcardNum;//	身份证号码	String	Y	用于标识该照片属于某个人员
    private String faceId;//	照片序号	String	Y	若faceId传入内容为空，则系统会默认为1，最多只能设置5张
    private String imgBase64;//	照片的base64编码	String	Y	不加头部，如：data:image/jpg;base64,
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

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
}
