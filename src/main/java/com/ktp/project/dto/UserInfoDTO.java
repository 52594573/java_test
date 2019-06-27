package com.ktp.project.dto;

/**
 * @Author: tangbin
 * @Date: 2019/1/3 9:55
 * @Version 1.0
 */
public class UserInfoDTO {
    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getOld_psw() {
        return old_psw;
    }

    public void setOld_psw(String old_psw) {
        this.old_psw = old_psw;
    }

    public String getNew_psw() {
        return new_psw;
    }

    public void setNew_psw(String new_psw) {
        this.new_psw = new_psw;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private Integer user_id;
    private String old_psw;
    private String new_psw;
    private String code;

}
