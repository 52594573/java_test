package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品属性表
 *
 * @author djcken
 * @date 2018/5/23
 */
@Entity
@Table(name = "mall_attr")
public class MallAttr {

    private int id;
    private String name;

    public MallAttr() {
    }

    public MallAttr(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ma_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
