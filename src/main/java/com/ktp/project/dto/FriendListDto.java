package com.ktp.project.dto;

/**
 * Created by LinHon 2018/8/7
 */

public class FriendListDto {

    private Integer user_id;
    private Integer u_sex;
    private String u_realname;
    private String u_nicheng;
    private String u_pic;
    private Integer u_cert;
    private Integer relationType;
    private String apply_msg;
    private Integer existReqProject;//是否已经在请求项目下
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getExistReqProject() {
        return existReqProject;
    }

    public void setExistReqProject(Integer existReqProject) {
        this.existReqProject = existReqProject;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getU_sex() {
        return u_sex;
    }

    public void setU_sex(Integer u_sex) {
        this.u_sex = u_sex;
    }

    public String getU_realname() {
        return u_realname;
    }

    public void setU_realname(String u_realname) {
        this.u_realname = u_realname;
    }

    public String getU_nicheng() {
        return u_nicheng;
    }

    public void setU_nicheng(String u_nicheng) {
        this.u_nicheng = u_nicheng;
    }

    public String getU_pic() {
        return u_pic;
    }

    public void setU_pic(String u_pic) {
        this.u_pic = u_pic;
    }

    public Integer getU_cert() {
        return u_cert;
    }

    public void setU_cert(Integer u_cert) {
        this.u_cert = u_cert;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public String getApply_msg() {
        return apply_msg;
    }

    public void setApply_msg(String apply_msg) {
        this.apply_msg = apply_msg;
    }
}
