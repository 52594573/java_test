package com.ktp.project.entity;

import javax.persistence.*;

@Entity
@Table(name = "work_log_pic")
public class WorkLogPic implements java.io.Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//int(11) NOT NULL


    @Column(name = "wl_id")
    private Integer wl_id;

    @Column(name = "wl_pic")
    private String wl_pic;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWl_id() {
        return wl_id;
    }

    public void setWl_id(Integer wl_id) {
        this.wl_id = wl_id;
    }

    public String getWl_pic() {
        return wl_pic;
    }

    public void setWl_pic(String wl_pic) {
        this.wl_pic = wl_pic;
    }
}
