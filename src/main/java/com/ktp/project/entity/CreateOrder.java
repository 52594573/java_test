package com.ktp.project.entity;

import java.io.Serializable;

/**
 * @author djcken
 * @date 2018/5/30
 */
public class CreateOrder implements Serializable {

    private String outTradeNo;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
