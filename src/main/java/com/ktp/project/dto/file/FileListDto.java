package com.ktp.project.dto.file;

import java.util.Date;

/**
 * @Author: wuyeming
 * @Date: 2018-12-17 上午 11:15
 */
public class FileListDto {
    private Integer id;
    private Integer fs_id;
    private String fl_filetype;
    private Date fl_intime;
    private String fl_url;
    private Double fl_size;
    private String fl_info;
    private Integer fl_uid;
    private String u_name;
    private String u_realname;
    private String fs_name;
    private Integer fs_type;
    private String fs_type_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFs_id() {
        return fs_id;
    }

    public void setFs_id(Integer fs_id) {
        this.fs_id = fs_id;
    }

    public String getFl_filetype() {
        return fl_filetype;
    }

    public void setFl_filetype(String fl_filetype) {
        this.fl_filetype = fl_filetype;
    }

    public Date getFl_intime() {
        return fl_intime;
    }

    public void setFl_intime(Date fl_intime) {
        this.fl_intime = fl_intime;
    }

    public String getFl_url() {
        return fl_url;
    }

    public void setFl_url(String fl_url) {
        this.fl_url = fl_url;
    }

    public Double getFl_size() {
        return fl_size;
    }

    public void setFl_size(Double fl_size) {
        this.fl_size = fl_size;
    }

    public String getFl_info() {
        return fl_info;
    }

    public void setFl_info(String fl_info) {
        this.fl_info = fl_info;
    }

    public Integer getFl_uid() {
        return fl_uid;
    }

    public void setFl_uid(Integer fl_uid) {
        this.fl_uid = fl_uid;
    }

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

    public String getFs_name() {
        return fs_name;
    }

    public void setFs_name(String fs_name) {
        this.fs_name = fs_name;
    }

    public Integer getFs_type() {
        return fs_type;
    }

    public void setFs_type(Integer fs_type) {
        this.fs_type = fs_type;
    }

    public String getFs_type_name() {
        return fs_type_name;
    }

    public void setFs_type_name(String fs_type_name) {
        this.fs_type_name = fs_type_name;
    }
}
