package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 退款
 *
 * @author djcken
 * @date 2018/5/21
 */
@Entity
@Table(name = "mall_order_refund")
public class MallOrderRefund {

    private int id;
    private String outTradeNo;//支付订单号
    private int payOnlineState;//支付方式
    private String outRefundNo;//退款单号
    private String refundNo;//微信或支付宝退款单号
    private int refundState;//退款状态
    private double refundAmount;//退款金额
    private String refundMessage;//退款信息

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "mo_no")
    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Column(name = "mo_online_pay_type")
    public int getPayOnlineState() {
        return payOnlineState;
    }

    public void setPayOnlineState(int payOnlineState) {
        this.payOnlineState = payOnlineState;
    }

    @Column(name = "mor_no")
    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    @Column(name = "mor_refund_no")
    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    @Column(name = "mor_state")
    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    @Column(name = "mor_amount")
    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Column(name = "mor_refund_message")
    public String getRefundMessage() {
        return refundMessage;
    }

    public void setRefundMessage(String refundMessage) {
        this.refundMessage = refundMessage;
    }
}
