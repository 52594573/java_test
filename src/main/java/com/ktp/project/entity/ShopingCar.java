package com.ktp.project.entity;

/**
 * 购物车
 *
 * @author djcken
 * @date 2018/5/29
 */
public class ShopingCar {

    private int userId;
    private int goodSpecId;
    private int goodId;
    private int count;
    private String goodName;
    private String goodSpecName;
    private double goodOriginPrice;
    private double goodPrice;
    private String goodAdPic;

    public ShopingCar(int userId, int specId, int goodId, int count, String goodName, String specName, double originPrice, double price, String pic) {
        this.userId = userId;
        this.goodSpecId = specId;
        this.goodId = goodId;
        this.count = count;
        this.goodName = goodName;
        this.goodSpecName = specName;
        this.goodOriginPrice = originPrice;
        this.goodPrice = price;
        this.goodAdPic = pic;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(int goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodSpecName() {
        return goodSpecName;
    }

    public void setGoodSpecName(String goodSpecName) {
        this.goodSpecName = goodSpecName;
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

    public String getGoodAdPic() {
        return goodAdPic;
    }

    public void setGoodAdPic(String goodAdPic) {
        this.goodAdPic = goodAdPic;
    }
}
