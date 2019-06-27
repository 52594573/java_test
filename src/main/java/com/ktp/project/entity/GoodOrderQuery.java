package com.ktp.project.entity;

import java.io.Serializable;
import java.util.List;

/**
 * select * from (select mg.id,mg.ms_id,mg.mg_pic,mg.mg_name,mg.mg_content,mga.ma_id,mga.mga_price_yuan,mga.mga_price_now from mall_good mg,mall_good_attr mga where mg.id=122 and mg.id=mga.mg_id) m
 */
public class GoodOrderQuery implements Serializable {

    private int goodId;//商品ID
    private int sortId;//分类id
    private String goodName;//商品名称
    private int goodSpecId;//商品规格Id
    private String goodDec;//商品描述
    private String goodPic;//商品图片
    private String goodAdPic;//商品广告图片
    private String goodPrePic;//商品预览图
    private double goodOriginPrice;//商品原价
    private double goodPrice;//该商品订单价格
    private String goodSpecName;//商品属性名称
    private List<MallGoodSpec> goodSpecList;//商品规格列表
    private int count;//商品数量，用于返回商品列表里面的购买数量
    private long buyCount;//购买总数量

    public GoodOrderQuery() {
    }

    public GoodOrderQuery(int goodId, int sortId, String goodName, int goodSpecId, String goodDec, String goodPic, String goodAdPic, String goodPrePic, double goodOriginPrice, double goodPrice, String goodSpecName) {
        this.goodId = goodId;
        this.sortId = sortId;
        this.goodName = goodName;
        this.goodSpecId = goodSpecId;
        this.goodDec = goodDec;
        this.goodPic = goodPic;
        this.goodAdPic = goodAdPic;
        this.goodPrePic = goodPrePic;
        this.goodOriginPrice = goodOriginPrice;
        this.goodPrice = goodPrice;
        this.goodSpecName = goodSpecName;
    }

    public GoodOrderQuery(int goodId, String goodName, String goodDec, String goodPic, String goodAdPic, String goodPrePic) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.goodDec = goodDec;
        this.goodPic = goodPic;
        this.goodAdPic = goodAdPic;
        this.goodPrePic = goodPrePic;
    }

    public GoodOrderQuery(int goodId, String goodName, String goodDec, String goodPic, String goodAdPic, String goodPrePic, double goodOriginPrice, double goodPrice, String goodSpecName, int count) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.goodDec = goodDec;
        this.goodPic = goodPic;
        this.goodAdPic = goodAdPic;
        this.goodPrePic = goodPrePic;
        this.goodOriginPrice = goodOriginPrice;
        this.goodPrice = goodPrice;
        this.goodSpecName = goodSpecName;
        this.count = count;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(int goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    public String getGoodDec() {
        return goodDec;
    }

    public void setGoodDec(String goodDec) {
        this.goodDec = goodDec;
    }

    public String getGoodPic() {
        return goodPic;
    }

    public void setGoodPic(String goodPic) {
        this.goodPic = goodPic;
    }

    public String getGoodAdPic() {
        return goodAdPic;
    }

    public void setGoodAdPic(String goodAdPic) {
        this.goodAdPic = goodAdPic;
    }

    public String getGoodPrePic() {
        return goodPrePic;
    }

    public void setGoodPrePic(String goodPrePic) {
        this.goodPrePic = goodPrePic;
    }

    public double getGoodOriginPrice() {
        return goodOriginPrice;
    }

    public void setGoodOriginPrice(double goodOriginPrice) {
        this.goodOriginPrice = goodOriginPrice;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getGoodSpecName() {
        return goodSpecName;
    }

    public void setGoodSpecName(String goodSpecName) {
        this.goodSpecName = goodSpecName;
    }

    public List<MallGoodSpec> getGoodSpecList() {
        return goodSpecList;
    }

    public void setGoodSpecList(List<MallGoodSpec> goodSpecList) {
        this.goodSpecList = goodSpecList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(long buyCount) {
        this.buyCount = buyCount;
    }
}
