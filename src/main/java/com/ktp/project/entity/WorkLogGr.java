package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "work_log_gr")
public class WorkLogGr {

    private Integer id;
    private Integer wl_id;
    private Integer user_id;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "wl_id")
    public Integer getWl_id() {
        return wl_id;
    }

    public void setWl_id(Integer wl_id) {
        this.wl_id = wl_id;
    }

    @Column(name = "user_id")
    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
