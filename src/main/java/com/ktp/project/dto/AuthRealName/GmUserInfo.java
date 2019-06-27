package com.ktp.project.dto.AuthRealName;

import java.util.Date;

/**
 * 高明项目对开太平项目
 */
public class GmUserInfo {
    //主键
    private Integer id;
    //用户名称
    private String u_realname;
    //身份证
    private String u_sfz;
    //手机号
    private String u_name;
    //头像
    private String u_pic;
    //民族
    private String key_name;
    //地址
    private String w_resi;
    //教育
    private Integer w_edu;
    //工种
    private Integer w_gzid;
    //证书
    private String w_zs;

    //进场时间
    private Date pop_intime;

    //出场时间
    private Date pop_endtime;

    public Date getPop_endtime() {
        return pop_endtime;
    }

    public void setPop_endtime(Date pop_endtime) {
        this.pop_endtime = pop_endtime;
    }

    //状态
    private Integer pop_state;

    public Integer getPop_state() {
        return pop_state;
    }

    public void setPop_state(Integer pop_state) {
        this.pop_state = pop_state;
    }

    public Date getPop_intime() {
        return pop_intime;
    }

    public void setPop_intime(Date pop_intime) {
        this.pop_intime = pop_intime;
    }



    public String getW_zs() {
        return w_zs;
    }

    public void setW_zs(String w_zs) {
        this.w_zs = w_zs;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getW_resi() {
        return w_resi;
    }

    public void setW_resi(String w_resi) {
        this.w_resi = w_resi;
    }

    public Integer getW_edu() {
        return w_edu;
    }

    public void setW_edu(Integer w_edu) {
        this.w_edu = w_edu;
    }

    public Integer getW_gzid() {
        return w_gzid;
    }

    public void setW_gzid(Integer w_gzid) {
        this.w_gzid = w_gzid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getU_realname() {
        return u_realname;
    }

    public void setU_realname(String u_realname) {
        this.u_realname = u_realname;
    }

    public String getU_sfz() {
        return u_sfz;
    }

    public void setU_sfz(String u_sfz) {
        this.u_sfz = u_sfz;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_pic() {
        return u_pic;
    }

    public void setU_pic(String u_pic) {
        this.u_pic = u_pic;
    }
}
