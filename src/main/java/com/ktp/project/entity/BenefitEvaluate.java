package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;

/**
*公益捐赠评论
* @Author: liaosh
* @Date: 2018/8/22 0022
*/
@Entity
@Table(name = "benefit_eva")
public class BenefitEvaluate {
    private Integer id;
    private Integer evaRecId;     //关联到受赠表denefit_rec中rec_id
    private String evaDescribe;    //评论描述
    private String evaPicture;     //图片
    private Date evaTime;          //评论时间
    private int evaIsDel;         //是否删除
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "eva_rec_id")
    public Integer getEvaRecId() {
        return evaRecId;
    }

    public void setEvaRecId(Integer evaRecId) {
        this.evaRecId = evaRecId;
    }
    @Column(name = "eva_describe")
    public String getEvaDescribe() {
        return evaDescribe;
    }

    public void setEvaDescribe(String evaDescribe) {
        this.evaDescribe = evaDescribe;
    }
    @Column(name = "eva_picture")
    public String getEvaPicture() {
        return evaPicture;
    }

    public void setEvaPicture(String evaPicture) {
        this.evaPicture = evaPicture;
    }
    @Column(name = "eva_time")
    public Date getEvaTime() {
        return evaTime;
    }

    public void setEvaTime(Date evaTime) {
        this.evaTime = evaTime;
    }
    @Column(name = "eva_is_del")
    public int getEvaIsDel() {
        return evaIsDel;
    }

    public void setEvaIsDel(int evaIsDel) {
        this.evaIsDel = evaIsDel;
    }
}
