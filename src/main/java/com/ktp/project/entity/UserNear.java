package com.ktp.project.entity;

import java.util.Date;

/**
 * 附近的人
 *
 * @author djcken
 * @date 2018/7/25
 */
public class UserNear {

    private Integer user_id;
    private String u_realname;
    private String u_nicheng;
    private String u_pic;
    private Integer u_sex;
    private Integer u_cert;
    private String u_city;
    private Double u_lbs_x;
    private Double u_lbs_y;
    private Double u_star;
    private Integer u_proid;
    private String zhiwu;
    private Date lastime;
    private long interval;//间隔时间

    public UserNear(Integer user_id, String u_realname, String u_nicheng, String u_pic, Integer u_sex, Integer u_cert, String u_city, Double u_lbs_x, Double u_lbs_y, Double u_star, Integer u_proid, Date lasttime) {
        this.user_id = user_id;
        this.u_realname = u_realname;
        this.u_nicheng = u_nicheng;
        this.u_pic = u_pic;
        this.u_sex = u_sex;
        this.u_cert = u_cert;
        this.u_city = u_city;
        this.u_lbs_x = u_lbs_x;
        this.u_lbs_y = u_lbs_y;
        this.u_star = u_star;
        this.u_proid = u_proid;
        this.lastime = lasttime;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public Integer getU_sex() {
        return u_sex;
    }

    public void setU_sex(Integer u_sex) {
        this.u_sex = u_sex;
    }

    public Integer getU_cert() {
        return u_cert;
    }

    public void setU_cert(Integer u_cert) {
        this.u_cert = u_cert;
    }

    public String getU_city() {
        return u_city;
    }

    public void setU_city(String u_city) {
        this.u_city = u_city;
    }

    public Double getU_lbs_x() {
        return u_lbs_x;
    }

    public void setU_lbs_x(Double u_lbs_x) {
        this.u_lbs_x = u_lbs_x;
    }

    public Double getU_lbs_y() {
        return u_lbs_y;
    }

    public void setU_lbs_y(Double u_lbs_y) {
        this.u_lbs_y = u_lbs_y;
    }

    public Double getU_star() {
        return u_star;
    }

    public void setU_star(Double u_star) {
        this.u_star = u_star;
    }

    public Integer getU_proid() {
        return u_proid;
    }

    public void setU_proid(Integer u_proid) {
        this.u_proid = u_proid;
    }

    public String getZhiwu() {
        return zhiwu;
    }

    public void setZhiwu(String zhiwu) {
        this.zhiwu = zhiwu;
    }

    public Date getLastime() {
        return lastime;
    }

    public void setLastime(Date lastime) {
        this.lastime = lastime;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
