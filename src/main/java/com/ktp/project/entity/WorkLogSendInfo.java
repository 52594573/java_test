package com.ktp.project.entity;

import java.sql.Timestamp;
import java.util.Date;

public class WorkLogSendInfo {


    /**
     * u_realname : 付万年
     * u_name : 18655325685
     * u_pic : /images/pic/2018912105054518617027.jpg
     * u_cert : 2
     * u_cert_type : 2
     * p_type : 0
     * zhiwu : 发送人职务
     * po_id : 312
     * po_name : 管理层
     */

    private String u_realname;
    private String u_name;
    private String u_pic;
    private int u_cert;
    private int u_cert_type;
    private int p_type;
    private String zhiwu;
    private int po_id;
    private String po_name;

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_realname() {
        return u_realname;
    }

    public void setU_realname(String u_realname) {
        this.u_realname = u_realname;
    }

    public String getU_pic() {
        return u_pic;
    }

    public void setU_pic(String u_pic) {
        this.u_pic = u_pic;
    }

    public int getU_cert() {
        return u_cert;
    }

    public void setU_cert(int u_cert) {
        this.u_cert = u_cert;
    }

    public int getU_cert_type() {
        return u_cert_type;
    }

    public void setU_cert_type(int u_cert_type) {
        this.u_cert_type = u_cert_type;
    }

    public int getP_type() {
        return p_type;
    }

    public void setP_type(int p_type) {
        this.p_type = p_type;
    }

    public String getZhiwu() {
        return zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu;
    }

    public int getPo_id() {
        return po_id;
    }

    public void setPo_id(int po_id) {
        this.po_id = po_id;
    }

    public String getPo_name() {
        return po_name;
    }

    public void setPo_name(String po_name) {
        this.po_name = po_name;
    }
}
