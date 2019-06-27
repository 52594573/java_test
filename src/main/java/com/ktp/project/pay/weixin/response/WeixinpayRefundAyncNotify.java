package com.ktp.project.pay.weixin.response;

/**
 * 退款实体
 *
 * @author djcken
 * @date 2018/5/21
 * <root>
 * <out_refund_no><![CDATA[201805221445058362053104958]]></out_refund_no>
 * <out_trade_no><![CDATA[201805211003026082053127400]]></out_trade_no>
 * <refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>
 * <refund_fee><![CDATA[1]]></refund_fee>
 * <refund_id><![CDATA[50000706852018052204691981782]]></refund_id>
 * <refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>
 * <refund_request_source><![CDATA[API]]></refund_request_source>
 * <refund_status><![CDATA[SUCCESS]]></refund_status>
 * <settlement_refund_fee><![CDATA[1]]></settlement_refund_fee>
 * <settlement_total_fee><![CDATA[1]]></settlement_total_fee>
 * <success_time><![CDATA[2018-05-22 14:45:04]]></success_time>
 * <total_fee><![CDATA[1]]></total_fee>
 * <transaction_id><![CDATA[4200000128201805222662416775]]></transaction_id>
 * </root>
 */
public class WeixinpayRefundAyncNotify {

    private String out_refund_no;//商户退款单号
    private String out_trade_no;//商户订单号
    private String refund_account;//退款商户
    private Integer refund_fee;//退款金额
    private String refund_id;//微信退款单号
    private String refund_recv_accout;//退款方式-用户零钱等
    private String refund_request_source;//退款请求方式，API等
    private String refund_status;//退款状态
    private String settlement_refund_fee;//已处理退款金额
    private String settlement_total_fee;//已处理总金额
    private String success_time;//处理成功时间
    private Integer total_fee;//总金额
    private String transaction_id;//微信订单号

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getRefund_account() {
        return refund_account;
    }

    public void setRefund_account(String refund_account) {
        this.refund_account = refund_account;
    }

    public Integer getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Integer refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_recv_accout() {
        return refund_recv_accout;
    }

    public void setRefund_recv_accout(String refund_recv_accout) {
        this.refund_recv_accout = refund_recv_accout;
    }

    public String getRefund_request_source() {
        return refund_request_source;
    }

    public void setRefund_request_source(String refund_request_source) {
        this.refund_request_source = refund_request_source;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getSettlement_refund_fee() {
        return settlement_refund_fee;
    }

    public void setSettlement_refund_fee(String settlement_refund_fee) {
        this.settlement_refund_fee = settlement_refund_fee;
    }

    public String getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(String settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getSuccess_time() {
        return success_time;
    }

    public void setSuccess_time(String success_time) {
        this.success_time = success_time;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
