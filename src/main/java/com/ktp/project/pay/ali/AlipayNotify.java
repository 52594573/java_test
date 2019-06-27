package com.ktp.project.pay.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author djcken
 * @date 2018/5/18
 */
public class AlipayNotify {

    /**
     * 验证消息是否是支付宝发出的合法消息
     *
     * @param requestParams request.getParameterMap()
     * @return 验证结果
     */
    public static boolean verifyRequest(Map requestParams) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(params, AlipayCfg.alipay_public_key, AlipayCfg.data_charset, AlipayCfg.data_sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
