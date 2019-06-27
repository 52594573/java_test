package com.ktp.project.entity;

public class WlPwInfoBean {


    /**
     * pw_id : 1012
     * pw_content : 节点描述
     * pw_name : 地下车库
     * pw_pid : 1007
     * p_pw_name : 2#楼
     */

    private int pw_id;
    private String pw_content;
    private String pw_name;
    private int pw_pid;
    private String p_pw_name;

    public int getPw_id() {
        return pw_id;
    }

    public void setPw_id(int pw_id) {
        this.pw_id = pw_id;
    }

    public String getPw_content() {
        return pw_content;
    }

    public void setPw_content(String pw_content) {
        this.pw_content = pw_content;
    }

    public String getPw_name() {
        return pw_name;
    }

    public void setPw_name(String pw_name) {
        this.pw_name = pw_name;
    }

    public int getPw_pid() {
        return pw_pid;
    }

    public void setPw_pid(int pw_pid) {
        this.pw_pid = pw_pid;
    }

    public String getP_pw_name() {
        return p_pw_name;
    }

    public void setP_pw_name(String p_pw_name) {
        this.p_pw_name = p_pw_name;
    }
}
