package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;

/**
 *公益捐赠表
 * * @Author: liaosh
 * @Date: 2018-8-22 13:52:18
 */
@Entity
@Table(name = "benefit_donate")
public class BenefitDonate {
    private Integer id;         //捐赠id
    private Integer donActId;        //活动id
    private Integer donUserId;    //捐赠者id
    private Date donTime;          //创建时间
    private Double donPrince;      //物品单价
    private Integer donSum;        //捐赠总数
    private String donUnit;        //物品单位
    private Float donPercent;      //新旧程度
    private String donAddress;     //捐赠物地址
    private String donWay;         //捐赠方式
    private String donPostage;     //邮费支付方式
    private Integer donInventory;  //剩余数量
    private Integer donApplySum;  //申请未查看数
    private Integer donCommentSum;//未查看评论数
    private Integer donStatus;     //状态-2:删除 -1:活动过期 0:审核中 1:正常进行
    private String donDescribe;    //物品描述
    private String donPicture;     //物品图片
    private String donGoodsSort;  //捐赠物品分类
    private String donRejectReason; //拒绝原因


    private String donHearPicture;//头像
    private String donName;//捐赠者姓名
    private String actTop;//活动主题
    private Integer actId;//和上面donId同一个字段
    private Integer donId;

    public Integer getDonId() {
        return donId;
    }

    public void setDonId(Integer donId) {
        this.donId = donId;
    }

    public String getDonHearPicture() {
        return donHearPicture;
    }

    public void setDonHearPicture(String donHearPicture) {
        this.donHearPicture = donHearPicture;
    }

    public String getDonName() {
        return donName;
    }

    public void setDonName(String donName) {
        this.donName = donName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name ="don_act_id" )
    public Integer getDonActId() {
        return donActId;
    }

    public void setDonActId(Integer donActId) {
        this.donActId = donActId;
    }
    @Column(name = "don_user_id")
    public Integer getDonUserId() {
        return donUserId;
    }

    public void setDonUserId(Integer donUserId) {
        this.donUserId = donUserId;
    }

    @Column(name = "don_time")
    public Date getDonTime() {
        return donTime;
    }

    public void setDonTime(Date donTime) {
        this.donTime = donTime;
    }
    @Column(name = "don_prince")
    public Double getDonPrince() {
        return donPrince;
    }

    public void setDonPrince(Double donPrince) {
        this.donPrince = donPrince;
    }
    @Column(name = "don_sum")
    public Integer getDonSum() {
        return donSum;
    }

    public void setDonSum(Integer donSum) {
        this.donSum = donSum;
    }
    @Column(name = "don_unit")
    public String getDonUnit() {
        return donUnit;
    }

    public void setDonUnit(String donUnit) {
        this.donUnit = donUnit;
    }
    @Column(name = "don_percent")
    public Float getDonPercent() {
        return donPercent;
    }

    public void setDonPercent(Float donPercent) {
        this.donPercent = donPercent;
    }
    @Column(name = "don_address")
    public String getDonAddress() {
        return donAddress;
    }

    public void setDonAddress(String donAddress) {
        this.donAddress = donAddress;
    }
    @Column(name = "don_way")
    public String getDonWay() {
        return donWay;
    }

    public void setDonWay(String donWay) {
        this.donWay = donWay;
    }
    @Column(name = "don_postage")
    public String getDonPostage() {
        return donPostage;
    }

    public void setDonPostage(String donPostage) {
        this.donPostage = donPostage;
    }
    @Column(name = "don_inventory")
    public Integer getDonInventory() {
        return donInventory;
    }

    public void setDonInventory(Integer donInventory) {
        this.donInventory = donInventory;
    }
    @Column(name = "don_apply_sum")
    public Integer getDonApplySum() {
        return donApplySum;
    }

    public void setDonApplySum(Integer donApplySum) {
        this.donApplySum = donApplySum;
    }
    @Column(name = "don_comment_sum")
    public Integer getDonCommentSum() {
        return donCommentSum;
    }

    public void setDonCommentSum(Integer donCommentSum) {
        this.donCommentSum = donCommentSum;
    }
    @Column(name = "don_status")
    public Integer getDonStatus() {
        return donStatus;
    }

    public void setDonStatus(Integer donStatus) {
        this.donStatus = donStatus;
    }
    @Column(name = "don_describe")
    public String getDonDescribe() {
        return donDescribe;
    }

    public void setDonDescribe(String donDescribe) {
        this.donDescribe = donDescribe;
    }
    @Column(name = "don_picture")
    public String getDonPicture() {
        return donPicture;
    }

    public void setDonPicture(String donPicture) {
        this.donPicture = donPicture;
    }
    @Column(name = "don_goods_sort")
    public String getDonGoodsSort() {
        return donGoodsSort;
    }

    public void setDonGoodsSort(String donGoodsSort) {
        this.donGoodsSort = donGoodsSort;
    }

    public String getActTop() {
        return actTop;
    }

    public void setActTop(String actTop) {
        this.actTop = actTop;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }

    @Column(name = "don_reject_reason")
    public String getDonRejectReason() {
        return donRejectReason;
    }

    public void setDonRejectReason(String donRejectReason) {
        this.donRejectReason = donRejectReason;
    }
}
