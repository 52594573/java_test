package com.ktp.project.dto.AuthRealName;

import com.ktp.project.constant.RealNameConfig;

import java.math.BigDecimal;

/**
 * 进场工人信息
 */
public class JoinWorkerInfoDto {

    private String sessionToken;//  ${token} 令牌 *
    private String code = RealNameConfig.PROJECT_CODE;//  string 项⺫编号 *
    private String name;//  string ⼈员姓名 *
    private String idNumber;//  string ⾝份证 *
    private String groupName;//  string 班组名称,相同班组名视为同⼀班组 *
    private Integer job;//  Integer ⼯种${job} *
    private Integer role;//  Integer 项⺫中的⾓⾊${role} *
    private String photo="";//  string ⾝份证中的⼤头照,base64编码的字符串,100KB⼤⼩以内 *
    private String facePhoto="";//  string 进场⼈员⼈证识别认证后的正脸清晰照⽚,base64编码的字符串,200KB⼤⼩以内 *
    private BigDecimal faceSimilarity=new BigDecimal(0.0);//  BigDecimal ⾝份证中的证件照和进场⼈员⼈证识别认证后的正脸清晰图⽚对⽐相识度,取值0-1之间,数值越⼤相识度越⾼ *
    private String phone;//  string 联系电话

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFacePhoto() {
        return facePhoto;
    }

    public void setFacePhoto(String facePhoto) {
        this.facePhoto = facePhoto;
    }

    public BigDecimal getFaceSimilarity() {
        return faceSimilarity;
    }

    public void setFaceSimilarity(BigDecimal faceSimilarity) {
        this.faceSimilarity = faceSimilarity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
