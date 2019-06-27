package com.ktp.project.dto.AuthRealName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JmDivisionRecordDto {

    private Integer dataType;
    private String payBankCardCode;
    private String receiveCompanySocialCode;
    private BigDecimal receiveBankCardCode;
    private String payMoney;
    private String tradeDate;
    private String tradeCode;
    private List<String> tradePicUrl = new ArrayList<>(0);

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getPayBankCardCode() {
        return payBankCardCode;
    }

    public void setPayBankCardCode(String payBankCardCode) {
        this.payBankCardCode = payBankCardCode;
    }

    public String getReceiveCompanySocialCode() {
        return receiveCompanySocialCode;
    }

    public void setReceiveCompanySocialCode(String receiveCompanySocialCode) {
        this.receiveCompanySocialCode = receiveCompanySocialCode;
    }

    public BigDecimal getReceiveBankCardCode() {
        return receiveBankCardCode;
    }

    public void setReceiveBankCardCode(BigDecimal receiveBankCardCode) {
        this.receiveBankCardCode = receiveBankCardCode;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public List<String> getTradePicUrl() {
        return tradePicUrl;
    }

    public void setTradePicUrl(List<String> tradePicUrl) {
        this.tradePicUrl = tradePicUrl;
    }
}
