package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mall_good_attr")
public class MallGoodAttr {

    private int id;
    private int goodId;//商品id
    private int goodSpecId;//商品属性id
    private double originPrice;//原价
    private double price;//现价

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

    @Column(name = "ma_id")
    public int getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(int goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    @Column(name = "mga_price_yuan")
    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    @Column(name = "mga_price_now")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
