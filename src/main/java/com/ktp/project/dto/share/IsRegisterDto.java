package com.ktp.project.dto.share;

public class IsRegisterDto {

    private String mobile;//手机号
    private Integer isRegister;//0 未注册 1 已注册
    private Integer isJoinOrgan;//是否已经加入班组
    private Integer userId;//用户ID

    public Integer getUserId() {
        return userId;
    }

    public IsRegisterDto setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getIsJoinOrgan() {
        return isJoinOrgan;
    }

    public IsRegisterDto setIsJoinOrgan(Integer isJoinOrgan) {
        this.isJoinOrgan = isJoinOrgan;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public IsRegisterDto setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public Integer getIsRegister() {
        return isRegister;
    }

    public IsRegisterDto setIsRegister(Integer isRegister) {
        this.isRegister = isRegister;
        return this;
    }
}
