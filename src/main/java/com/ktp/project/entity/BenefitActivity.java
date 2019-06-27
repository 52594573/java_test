package com.ktp.project.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;


/**
*公益活动表
 * * @Author: liaosh
* @Date: 2018/8/22 0022
*/
@Entity
@Table(name = "benefit_act")
public class BenefitActivity {

    private Integer id;                   //活动id
    private String actTop;               //活动主题

    private Date actSTime;               //活动开始时间

    private Date actETime;               //活动结束时间
    private Integer  actInventorySum;   //待领取总数
    private Integer actRecipientSum;    //受赠人总数
    private String actDetail;            //活动详情
    private Integer actStatus;           //-1:已结束 0:筹备中  1:活动进行中
    private String actSponsor;          //主办方
    private String actPicture;           //活动图片

    private Long deadline;//距离截止天数

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "act_top")
    public String getActTop() {
        return actTop;
    }

    public void setActTop(String actTop) {
        this.actTop = actTop;
    }
    @Column(name = "act_stime")
    public Date getActSTime() {
        return actSTime;
    }

    public void setActSTime(Date actSTime) {
        this.actSTime = actSTime;
    }

    @Column(name = "act_etime")
    public Date getActETime() {
        return actETime;
    }

    public void setActETime(Date actETime) {
        this.actETime = actETime;
    }

    @Column(name = "act_inv_sum")
    public Integer getActInventorySum() {
        return actInventorySum;
    }

    public void setActInventorySum(Integer actInventorySum) {
        this.actInventorySum = actInventorySum;
    }
    @Column(name = "act_rec_sum")
    public Integer getActRecipientSum() {
        return actRecipientSum;
    }

    public void setActRecipientSum(Integer actRecipientSum) {
        this.actRecipientSum = actRecipientSum;
    }
    @Column(name = "act_detail")
    public String getActDetail() {
        return actDetail;
    }

    public void setActDetail(String actDetail) {
        this.actDetail = actDetail;
    }
    @Column(name = "act_status")
    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }
    @Column(name = "act_sponsor")
    public String getActSponsor() {
        return actSponsor;
    }

    public void setActSponsor(String actSponsor) {
        this.actSponsor = actSponsor;
    }
    @Column(name = "act_picture")
    public String getActPicture() {
        return actPicture;
    }

    public void setActPicture(String actPicture) {
        this.actPicture = actPicture;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

}
