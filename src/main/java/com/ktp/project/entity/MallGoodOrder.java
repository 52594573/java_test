package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "mall_good_order")
public class MallGoodOrder implements Serializable {

    private int id;
    private int goodId;//商品ID
    private String outTradeNo;//订单号
    private int sortId;//分类id
    private String goodName;//商品名称
    private int goodSpecId;//商品规格
    private String goodSpecName;//商品规格名称
    private String goodDec;//商品描述
    private String goodPic;//商品图片
    private String goodAdPic;//商品广告图片
    private String goodPrePic;//商品预览图
    private double goodOriginPrice;//商品原价
    private double goodPrice;//该商品订单价格
    private int goodNum;//该订单商品数量
    private Date createTime;//创建时间

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mg_id")
    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    @Column(name = "mo_no")
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Column(name = "ms_id")
    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Column(name = "mgo_name")
    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    @Column(name = "mgo_spec_id")
    public int getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(int goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    @Column(name = "mgo_spec_name")
    public String getGoodSpecName() {
        return goodSpecName;
    }

    public void setGoodSpecName(String goodSpecName) {
        this.goodSpecName = goodSpecName;
    }

    @Column(name = "mgo_dec")
    public String getGoodDec() {
        return goodDec;
    }

    public void setGoodDec(String goodDec) {
        this.goodDec = goodDec;
    }

    @Column(name = "mgo_pic")
    public String getGoodPic() {
        return goodPic;
    }

    public void setGoodPic(String goodPic) {
        this.goodPic = goodPic;
    }

    @Column(name = "mgo_ad_pic")
    public String getGoodAdPic() {
        return goodAdPic;
    }

    public void setGoodAdPic(String goodAdPic) {
        this.goodAdPic = goodAdPic;
    }

    @Column(name = "mgo_pre_pic")
    public String getGoodPrePic() {
        return goodPrePic;
    }

    public void setGoodPrePic(String goodPrePic) {
        this.goodPrePic = goodPrePic;
    }

    @Column(name = "mgo_origin_price")
    public double getGoodOriginPrice() {
        return goodOriginPrice;
    }

    public void setGoodOriginPrice(double goodOriginPrice) {
        this.goodOriginPrice = goodOriginPrice;
    }

    @Column(name = "mgo_price")
    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

    @Column(name = "mgo_num")
    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    @Column(name = "in_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void fixGoodOrder(GoodOrderQuery goodOrderQuery){
        setGoodId(goodOrderQuery.getGoodId());
        setGoodName(goodOrderQuery.getGoodName());
        setGoodDec(goodOrderQuery.getGoodDec());
        setGoodOriginPrice(goodOrderQuery.getGoodOriginPrice());
        setGoodPrice(goodOrderQuery.getGoodPrice());
        setSortId(goodOrderQuery.getSortId());
        setGoodSpecId(goodOrderQuery.getGoodSpecId());
        setGoodSpecName(goodOrderQuery.getGoodSpecName());
        setGoodPic(goodOrderQuery.getGoodPic());
        setGoodAdPic(goodOrderQuery.getGoodAdPic());
        setGoodPrePic(goodOrderQuery.getGoodPrePic());
    }
}
