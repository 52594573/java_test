package com.ktp.project.common;

public class Common {

    //支付类型 付款方式1.在线支付2.货到付款3.积分兑换4.混合在线支付
    public static final int PAY_TYPE_ONLINE = 1;
    public static final int PAY_TYPE_RECIVER = 2;
    public static final int PAY_TYPE_JIFEN = 3;
    public static final int PAY_TYPE_MULTI = 4;

    //支付状态 订单状态1提交订单成功。2.付款成功3.商品出库4等待收货5.完成6.已取消7.已退款8.交易关闭9.支付宝退款通道关闭-交易结束
    public static final int ORDER_STATE_CREATE = 1;
    public static final int ORDER_STATE_PAY = 2;
    public static final int ORDER_STATE_SEND = 3;
    public static final int ORDER_STATE_WAIT = 4;
    public static final int ORDER_STATE_FINISH = 5;
    public static final int ORDER_STATE_CANCEL = 6;
    public static final int ORDER_STATE_REFUND = 7;
    public static final int ORDER_STATE_CLOSE = 8;
    public static final int ORDER_STATE_ALI_TRADE_CLOSE = 9;

    //退款状态 1申请退款成功 2 申请退款失败  3退款成功 4 退款失败
    public static final int ORDER_REFUND_STATE_APPLY_SUCCESS = 1;
    public static final int ORDER_REFUND_STATE_APPLY_FAIL = 2;
    public static final int ORDER_REFUND_STATE_SUCCESS = 3;
    public static final int ORDER_REFUND_STATE_FAIL = 4;

}
