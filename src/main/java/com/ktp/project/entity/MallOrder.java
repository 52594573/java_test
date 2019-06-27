package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mall_order")
public class MallOrder {

    private int id;
    private String outTradeNo;//商户订单号
    private String tradeNo;//支付宝交易号
    private int buyerId;//用户id
    private String subject;//在线支付商品标题
    private String body;//在线支付商品描述
    private int payType;//支付类型 付款方式1.在线支付2.货到付款3.积分兑换4.混合在线支付
    private int payOnlineType;//在线支付类型
    private int status;//支付状态 订单状态1提交订单成功。2.付款成功3.商品出库4等待收货5.完成6.已取消7.已退款
    private Date createTime;//创建时间
    private Date finishTime;//完成时间
    private Date closeTime;//关闭时间
    private Date expireTime;//过期时间
    private double totalAmount;//订单金额
    private String remark;//备注
    private int sendId;//运单号
    private double sendPrice;//运费
    private String customerName;//客户姓名
    private String customerMobile;//客户电话
    private String customerAddress;//客户收货地址
    private int isDelete;//是否删除

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mo_price")
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Column(name = "mo_no")
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Column(name = "mo_trade_no")
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Column(name = "mo_subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "mo_body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Column(name = "mo_uid")
    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "mo_pay_type")
    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    @Column(name = "mo_online_pay_type")
    public int getPayOnlineType() {
        return payOnlineType;
    }

    public void setPayOnlineType(int payOnlineType) {
        this.payOnlineType = payOnlineType;
    }

    @Column(name = "mo_state")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "mo_create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "mo_finish_time")
    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Column(name = "mo_close_time")
    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    @Column(name = "mo_expire_time")
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Column(name = "mo_mark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "mo_send_id")
    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    @Column(name = "mo_send_price")
    public double getSendPrice() {
        return sendPrice;
    }

    public void setSendPrice(double sendPrice) {
        this.sendPrice = sendPrice;
    }

    @Column(name = "mo_get_name")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "mo_get_tel")
    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    @Column(name = "mo_get_address")
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Column(name = "mo_isdel")
    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}