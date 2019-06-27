package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 公益受赠表
*@Author: liaosh
* @Date: 2018/8/22 0022
*/
@Entity
@Table(name = "benefit_rec")
public class BenefitRecipient {
    private Integer id;         //
    private Integer recUserId;    //受赠者id
    private Integer recDonId;     //捐赠id
    private Date recTime;          //申请时间
    private Date recDealTime;       //受理时间
    private Integer recStatus;     //-2:拒绝 -1:同意超时 0:等待同意 1:已同意 2:已发货 3:已评论
    private String recReason;      //申请原因
    private String recAddress;     //收货地址
    private String recWay;         //受赠方式
    private Integer recSum;        //申请数量
    private String  recConsignee;  //收货人
    private String recTel;         //收货电话
    private Integer recActualSum;//实际捐赠数量
    private Date recGetTime;//确认收货时间
    private int recIsDel;         //是否删除

    private Integer donId;//捐赠id


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "rec_user_id")
    public Integer getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(Integer recUserId) {
        this.recUserId = recUserId;
    }
    @Column(name = "rec_don_id")
    public Integer getRecDonId() {
        return recDonId;
    }

    public void setRecDonId(Integer recDonId) {
        this.recDonId = recDonId;
    }
    @Column(name = "rec_time")
    public Date getRecTime() {
        return recTime;
    }

    public void setRecTime(Date recTime) {
        this.recTime = recTime;
    }

    @Column(name = "rec_deal_time")
    public Date getRecDealTime() {
        return recDealTime;
    }

    public void setRecDealTime(Date recDealTime) {
        this.recDealTime = recDealTime;
    }

    @Column(name = "rec_status")
    public Integer getRecStatus() {
        return recStatus;
    }

    public void setRecStatus(Integer recStatus) {
        this.recStatus = recStatus;
    }
    @Column(name = "rec_reason")
    public String getRecReason() {
        return recReason;
    }

    public void setRecReason(String recReason) {
        this.recReason = recReason;
    }
    @Column(name = "rec_address")
    public String getRecAddress() {
        return recAddress;
    }

    public void setRecAddress(String recAddress) {
        this.recAddress = recAddress;
    }
    @Column(name = "rec_way")
    public String getRecWay() {
        return recWay;
    }

    public void setRecWay(String recWay) {
        this.recWay = recWay;
    }
    @Column(name = "rec_sum")
    public Integer getRecSum() {
        return recSum;
    }

    public void setRecSum(Integer recSum) {
        this.recSum = recSum;
    }
    @Column(name = "rec_consignee")
    public String getRecConsignee() {
        return recConsignee;
    }

    public void setRecConsignee(String recConsignee) {
        this.recConsignee = recConsignee;
    }
    @Column(name = "rec_tel")
    public String getRecTel() {
        return recTel;
    }

    public void setRecTel(String recTel) {
        this.recTel = recTel;
    }

    @Column(name = "rec_actual_sum")
    public Integer getRecActualSum() {
        return recActualSum;
    }

    public void setRecActualSum(Integer recActualSum) {
        this.recActualSum = recActualSum;
    }

    @Column(name = "rec_get_time")
    public Date getRecGetTime() {
        return recGetTime;
    }

    public void setRecGetTime(Date recGetTime) {
        this.recGetTime = recGetTime;
    }

    public Integer getDonId() {
        return donId;
    }

    public void setDonId(Integer donId) {
        this.donId = donId;
    }

    @Column(name = "rec_is_del")

    public int getRecIsDel() {
        return recIsDel;
    }

    public void setRecIsDel(int recIsDel) {
        this.recIsDel = recIsDel;
    }
}
