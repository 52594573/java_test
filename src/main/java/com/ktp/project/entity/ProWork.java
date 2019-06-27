package com.ktp.project.entity;

import javax.persistence.*;

@Entity
@Table(name = "pro_work")
public class ProWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//int(11) NOT NULL

    @Column(name = "pro_id")
    private Integer proId;//int(11) NULL项目id

    @Column(name = "pw_name")
    private String pwName;//varchar(255) NULL名称

    @Column(name = "pw_child")
    private Integer pwChild;//int(255) NULL子项数

    @Column(name = "pw_pid")
    private Integer pwPid;//int(11) NULL父层ID

    @Column(name = "pw_ceng")
    private Integer pwCeng;//int(255) NULL处在几层

    @Column(name = "in_uid")
    private Integer inUid;//int(11) NULL

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getPwName() {
        return pwName;
    }

    public void setPwName(String pwName) {
        this.pwName = pwName;
    }

    public Integer getPwChild() {
        return pwChild;
    }

    public void setPwChild(Integer pwChild) {
        this.pwChild = pwChild;
    }

    public Integer getPwPid() {
        return pwPid;
    }

    public void setPwPid(Integer pwPid) {
        this.pwPid = pwPid;
    }

    public Integer getPwCeng() {
        return pwCeng;
    }

    public void setPwCeng(Integer pwCeng) {
        this.pwCeng = pwCeng;
    }

    public Integer getInUid() {
        return inUid;
    }

    public void setInUid(Integer inUid) {
        this.inUid = inUid;
    }
}
