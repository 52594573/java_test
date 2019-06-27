package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 购物车表
 *
 * @author djcken
 * @date 2018/5/29
 */
@Entity
@Table(name = "mall_car")
public class MallCar {

    private int id;
    private int userId;
    private int goodSpecId;
    private int goodId;
    private int count;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mc_uid")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "ma_id")
    public int getGoodSpecId() {
        return goodSpecId;
    }

    public void setGoodSpecId(int goodSpecId) {
        this.goodSpecId = goodSpecId;
    }

    @Column(name = "mg_id")
    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    @Column(name = "mc_count")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
