package com.ktp.project.entity;

import java.io.Serializable;

/**
 * 计算并返回运费实体类
 *
 * @author djcken
 * @date 2018/5/28
 */
public class MallCalculate implements Serializable {

    private double sendPrice;

    public double getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(double sendPrice) {
        this.sendPrice = sendPrice;
    }
}
