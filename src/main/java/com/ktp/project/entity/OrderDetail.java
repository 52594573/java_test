package com.ktp.project.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author djcken
 * @date 2018/5/29
 */
public class OrderDetail implements Serializable {

    private String outTradeNo;//商户订单号
    private String createTime;//创建时间
    private long leftTime;//过期时间
    private String customerName;//客户姓名
    private String customerMobile;//客户电话
    private String customerAddress;//客户收货地址
    private String remark;//备注
    private double totalAmount;//订单金额
    private double sendPrice;//运费
    private int status;//支付状态 订单状态1提交订单成功。2.付款成功3.商品出库4等待收货5.完成6.已取消7.已退款
    private long buyCount;//购买数量
    private List<GoodOrderQuery> goodList;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(double sendPrice) {
        this.sendPrice = sendPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(long buyCount) {
        this.buyCount = buyCount;
    }

    public List<GoodOrderQuery> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodOrderQuery> goodList) {
        this.goodList = goodList;
    }

}
