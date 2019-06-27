package com.ktp.project.entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "work_log")
public class WorkLogDaoBean {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// int(11) NOT NULL

    @Column(name = "pro_id")
    private String pro_id;// int(11) NULL

    @Column(name = "send_uid")
    private String send_uid;// int(11) NULL

    @Column(name = "wl_type")
    private String wl_type;// int(11) NULL类型 ：1.质量检查2.安全事件3.不当行为。4。工人打卡 5.个人签到

    @Column(name = "wl_content")
    private String wl_content;// varchar(4000) NULL

    @Column(name = "wl_star")//之前   Integer
    private String wl_star;// int(11) NULL评星：1-5星，仅质量检查时有评星

    @Column(name = "wl_yzcd")
    private String wl_yzcd;// int(11) NULL问题严重程度：1.严重2.普通3.警示 ，仅安全和不当行为时有严重程度

    @Column(name = "in_time")
    private Date in_time;// datetime NULL

    @Column(name = "wl_state")
    private String wl_state;// int(11) NULL1 正常显示 2.撤销 4 删除

    @Column(name = "wl_lbs_x")
    private String wl_lbs_x;// decimal(17,7) NULL

    @Column(name = "wl_lbs_y")
    private String wl_lbs_y;// decimal(17,7) NULL

    @Column(name = "wl_lbs_name")
    private String wl_lbs_name;// varchar(255) NULL

    @Column(name = "pw_id")
    private String pw_id;// int(11) NULL

    @Column(name = "pw_content")
    private String pw_content;// varchar(255) NULL节点描述

    @Column(name = "wl_safe_type")
    private String wl_safe_type;// varchar(255) 不安全因素类别

    @Column(name = "works_list")
    private String works_list;

    @Column(name = "send_info")
    private String send_info;

    @Column(name = "wl_pw_info")
    private String wl_pw_info;

    @Column(name = "pic_list")
    private String pic_list;


    public String getWorks_list() {
        return works_list;
    }

    public void setWorks_list(String works_list) {
        this.works_list = works_list;
    }

    public String getSend_info() {
        return send_info;
    }

    public void setSend_info(String send_info) {
        this.send_info = send_info;
    }

    public String getWl_pw_info() {
        return wl_pw_info;
    }

    public void setWl_pw_info(String wl_pw_info) {
        this.wl_pw_info = wl_pw_info;
    }

    public String getPic_list() {
        return pic_list;
    }

    public void setPic_list(String pic_list) {
        this.pic_list = pic_list;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getSend_uid() {
        return send_uid;
    }

    public void setSend_uid(String send_uid) {
        this.send_uid = send_uid;
    }

    public String getWl_type() {
        return wl_type;
    }

    public void setWl_type(String wl_type) {
        this.wl_type = wl_type;
    }

    public String getWl_content() {
        return wl_content;
    }

    public void setWl_content(String wl_content) {
        this.wl_content = wl_content;
    }

    public String getWl_star() {
        return wl_star;
    }

    public void setWl_star(String wl_star) {
        this.wl_star = wl_star;
    }

    public String getWl_yzcd() {
        return wl_yzcd;
    }

    public void setWl_yzcd(String wl_yzcd) {
        this.wl_yzcd = wl_yzcd;
    }

    public Date getIn_time() {
        return in_time;
    }

    public void setIn_time(Date in_time) {
        this.in_time = in_time;
    }

    public String getWl_state() {
        return wl_state;
    }

    public void setWl_state(String wl_state) {
        this.wl_state = wl_state;
    }

    public String getWl_lbs_x() {
        return wl_lbs_x;
    }

    public void setWl_lbs_x(String wl_lbs_x) {
        this.wl_lbs_x = wl_lbs_x;
    }

    public String getWl_lbs_y() {
        return wl_lbs_y;
    }

    public void setWl_lbs_y(String wl_lbs_y) {
        this.wl_lbs_y = wl_lbs_y;
    }

    public String getWl_lbs_name() {
        return wl_lbs_name;
    }

    public void setWl_lbs_name(String wl_lbs_name) {
        this.wl_lbs_name = wl_lbs_name;
    }

    public String getPw_id() {
        return pw_id;
    }

    public void setPw_id(String pw_id) {
        this.pw_id = pw_id;
    }

    public String getPw_content() {
        return pw_content;
    }

    public void setPw_content(String pw_content) {
        this.pw_content = pw_content;
    }

    public String getWl_safe_type() {
        return wl_safe_type;
    }

    public void setWl_safe_type(String wl_safe_type) {
        this.wl_safe_type = wl_safe_type;
    }
}
