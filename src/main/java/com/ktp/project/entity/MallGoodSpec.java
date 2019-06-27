package com.ktp.project.entity;

/**
 * @author djcken
 * @date 2018/5/28
 */
public class MallGoodSpec {

    private int goodId;//商品id
    private double originPrice;//商品原价
    private double price;//商品价格
    private int specId;//规格id
    private String spec;//规格描述

    public MallGoodSpec(int goodId, double originPrice, double price, int specId, String spec) {
        this.goodId = goodId;
        this.originPrice = originPrice;
        this.price = price;
        this.specId = specId;
        this.spec = spec;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}
