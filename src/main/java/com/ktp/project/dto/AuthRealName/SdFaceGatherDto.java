package com.ktp.project.dto.AuthRealName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SdFaceGatherDto {

    @JsonProperty(value = "Token")
    private String Token;//	String	是	Token应采用平台对接时发放的Token

    @JsonProperty(value = "IDCardNum")
    private String IDCardNum;//	String	是	身份证号

    @JsonProperty(value = "PhotoID")
    private Integer FaceID;//	Int	否	人脸图片序号，最多支持5张图片。留空默认为1。

    @JsonProperty(value = "imgBase64")
    private String imgBase64;//	String	是	照片的base64编码, 不加头部，如：data:image/jpg;base64,

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    @JsonIgnore
    public void setToken(String token) {
        Token = token;
    }

    @JsonIgnore
    public String getIDCardNum() {
        return IDCardNum;
    }

    @JsonIgnore
    public void setIDCardNum(String IDCardNum) {
        this.IDCardNum = IDCardNum;
    }

    @JsonIgnore
    public Integer getFaceID() {
        return FaceID;
    }

    @JsonIgnore
    public void setFaceID(Integer faceID) {
        FaceID = faceID;
    }

    @JsonIgnore
    public String getImgBase64() {
        return imgBase64;
    }

    @JsonIgnore
    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
}
