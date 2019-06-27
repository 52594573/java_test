package com.ktp.project.pay.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author djcken
 * @date 2018/5/18
 */
public class Alipay {

    private static final AlipayClient client = new DefaultAlipayClient(
            AlipayCfg.gateway,
            "" + AlipayCfg.app_id,
            AlipayCfg.merchant_private_key,
            AlipayCfg.data_format,
            AlipayCfg.data_charset,
            AlipayCfg.alipay_public_key,
            AlipayCfg.data_sign_type
    );

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 构建支付信息
     *
     * @param outTradeNo  本地商家订单号
     * @param amount      总金额
     * @param subject     订单名称
     * @param body        商品描述，可空
     * @param otherParams 额外参数，用于替换 notify.url 中 {.*} 格式的参数值
     */
    public static String create(String outTradeNo, double amount, String subject, String body, Map<String, String> otherParams) {
        Map<String, String> infos = new TreeMap<>();
        infos.put("app_id", "" + AlipayCfg.app_id);
        infos.put("biz_content",
                "{" +
                        "\"out_trade_no\":\"" + outTradeNo + "\"," +
                        "\"total_amount\":\"" + (AlipayCfg.debug ? 0.01 : amount) + "\"," +
                        "\"subject\":\"" + subject + "\"," +
                        "\"body\":\"" + body + "\"," +
                        //禁用信用卡渠道
//                ("".equals(AlipayCfg.disable_pay_channels) ? "" : "\"disable_pay_channels\":\""+ AlipayCfg.disable_pay_channels +"\",") +
                        "\"timeout_express\":\"" + AlipayCfg.timeout + "\"," +
                        "\"product_code\":\"QUICK_MSECURITY_PAY\"" +
                        "}"
        );
        infos.put("charset", AlipayCfg.data_charset);
        infos.put("format", AlipayCfg.data_format);
        infos.put("method", "alipay.trade.app.pay");

        // 设置异步回调地址
        String notifyUrl = AlipayCfg.notify_url;
//        if (otherParams != null && otherParams.size() > 0) {
//            for (Map.Entry<String, String> entry : otherParams.entrySet()) {
//                String value = entry.getValue();
//                if (value == null) {
//                    value = "";
//                }
//                notifyUrl = notifyUrl.replaceAll("\\{" + entry.getKey() + "\\}", value);
//            }
//        }
//        infos.put("notify_url", notifyUrl);
        infos.put("sign_type", AlipayCfg.data_sign_type);
        infos.put("timestamp", dateFormat.format(new Date()));
        infos.put("version", "1.0");

        try {
            String sign = AlipaySignature.rsaSign(infos, AlipayCfg.merchant_private_key, AlipayCfg.data_charset);
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                builder.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), AlipayCfg.data_charset)).append('&');
            }
            builder.append("sign=").append(URLEncoder.encode(sign, AlipayCfg.data_charset));
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOrderInfo(String outTradeNo, double amount, String subject, String body) {
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress(AlipayCfg.timeout);
        model.setTotalAmount(AlipayCfg.debug ? "0.01" : String.valueOf(amount));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayCfg.notify_url);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = client.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询订单状态
     *
     * @param outTradeNo 本地商家订单号
     * @return
     */
    public static AlipayTradeQueryResponse query(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" + "\"out_trade_no\":\"" + outTradeNo + "\"" + "}");
        try {
            return client.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询订单是否支付成功
     *
     * @param outTradeNo 本地商家订单号
     * @return
     */
    public static boolean isTradeFinishedOrSuccess(String outTradeNo) {
        AlipayTradeQueryResponse response = query(outTradeNo);
        return isTradeFinishedOrSuccess(response);
    }

    public static boolean isTradeFinishedOrSuccess(AlipayTradeQueryResponse response) {
        if (response == null) {
            return false;
        }
        return "TRADE_FINISHED".equals(response.getTradeStatus())
                || "TRADE_SUCCESS".equals(response.getTradeStatus());
    }

    /**
     * 退款
     *
     * @param outTradeNo   本地商家订单号
     * @param refundAmount 退款金额
     * @param outTradeNo   标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     */
    public static AlipayTradeRefundResponse refund(String outTradeNo, String outRefundNo, double refundAmount) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setOutRequestNo(outRefundNo);
        model.setRefundAmount(AlipayCfg.debug ? "0.01" : "" + refundAmount);
        request.setBizModel(model);
        request.setNotifyUrl(AlipayCfg.notify_url);
        try {
            return client.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean refundSuccess(AlipayTradeRefundResponse response) {
        return response != null && response.isSuccess();
    }

    /**
     * 验证异步通知是否真实
     * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.I2YTLn&treeId=204&articleId=105301&docType=1
     *
     * @param notify      异步通知对象
     * @param outTradeNo  商家订单号
     * @param totalAmount 总金额
     */
    public static boolean checkAsyncNotify(Map<String, String[]> notify, String outTradeNo, double totalAmount) {
        if (notify == null) {
            return false;
        }
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String[]> entry : notify.entrySet()) {
            map.put(entry.getKey(), entry.getValue()[0]);
        }

        try {
            if (AlipaySignature.rsaCheckV1(map, AlipayCfg.alipay_public_key, AlipayCfg.data_charset, AlipayCfg.data_sign_type)) {
                // 验证订单的四个重要信息是否匹配
                return outTradeNo.equals(map.get("out_trade_no")) && (AlipayCfg.debug ? 0.01 : totalAmount) == Double.parseDouble(map.get("total_amount"))
                        && ("" + AlipayCfg.seller_id).equals(map.get("seller_id"))
                        && ("" + AlipayCfg.app_id).equals(map.get("app_id"));
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 异步通知是否为成功支付的通知
     *
     * @param notify
     * @return
     */
    public static boolean isTradeSuccess(Map<String, String[]> notify) {
        String[] trade_status = notify.get("trade_status");
        return trade_status != null && trade_status.length == 1 && "TRADE_SUCCESS".equals(trade_status[0]);
    }

}
