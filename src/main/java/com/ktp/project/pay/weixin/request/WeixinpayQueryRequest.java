package com.ktp.project.pay.weixin.request;

import com.ktp.project.pay.weixin.WeixinpayCfg;
import com.ktp.project.util.RandomUtil;
import com.ktp.project.util.SignUtil;

public class WeixinpayQueryRequest extends WeixinpayRequest {

    /**
     * @param outTradeNo 商户订单号
     */
    public WeixinpayQueryRequest(
            String outTradeNo
    ) {
        this.apiUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

        params.put("appid", WeixinpayCfg.app_id);
        params.put("mch_id", "" + WeixinpayCfg.merchant_id);
        params.put("nonce_str", RandomUtil.generateStr(10, 20));
        params.put("out_trade_no", outTradeNo);
        params.put("sign", SignUtil.sign(params, WeixinpayCfg.merchant_key));
    }

}
