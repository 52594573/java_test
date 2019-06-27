package com.ktp.project.pay.weixin.request;

import com.ktp.project.pay.weixin.WeixinpayCfg;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.RandomUtil;
import com.ktp.project.util.SignUtil;
import org.apache.http.util.TextUtils;

import java.util.Date;
import java.util.Map;

public class WeixinpayCreateRequest extends WeixinpayRequest {

    /**
     * @param outTradeNo  商户订单号
     * @param body        商品描述
     * @param detail      商品详情
     * @param totalFee    总金额，单位元
     * @param userIp      客户终端IP
     * @param otherParams 额外参数，用于替换 notify.url 中 {.*} 格式的参数值
     */
    public WeixinpayCreateRequest(
            String outTradeNo,
            String body,
            String detail,
            double totalFee,
            String userIp,
            Map<String, String> otherParams
    ) {
        this.apiUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        Date now = new Date();

        params.put("appid", WeixinpayCfg.app_id);
        if (!TextUtils.isEmpty(body)) {
            params.put("body", body);
        }
        if (!TextUtils.isEmpty(detail)) {
            params.put("detail", detail);
        }
        params.put("mch_id", "" + WeixinpayCfg.merchant_id);
        params.put("nonce_str", RandomUtil.generateStr(10, 20));

        // 设置异步回调地址
        String notifyUrl = WeixinpayCfg.notify_url;
        if (otherParams != null && otherParams.size() > 0) {
            for (Map.Entry<String, String> entry : otherParams.entrySet()) {
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                notifyUrl = notifyUrl.replaceAll("\\{" + entry.getKey() + "\\}", value);
            }
        }
        params.put("notify_url", notifyUrl);
        params.put("out_trade_no", outTradeNo);
        params.put("spbill_create_ip", userIp);
        //配置中的超时时间的单位为：分钟
        params.put("time_expire", DateUtil.format(new Date(now.getTime() + WeixinpayCfg.timeout * 60000), DateUtil.FORMAT_DATE_TIME_MINS));
        params.put("time_start", DateUtil.format(now, DateUtil.FORMAT_DATE_TIME_MINS));
        // 将单位转换为 分，并将结果转换为整数
        params.put("total_fee", WeixinpayCfg.debug ? "1" : (int) (totalFee * 100) + "");
        params.put("trade_type", WeixinpayCfg.trade_type);
        //指定不能使用信用卡支付
        if (!"".equals(WeixinpayCfg.limit_pay)) {
            params.put("limit_pay", WeixinpayCfg.limit_pay);
        }
        params.put("sign", SignUtil.sign(params, WeixinpayCfg.merchant_key));
    }

}
