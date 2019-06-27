package com.ktp.project.pay.weixin;

import java.util.Properties;

/**
 * @author djcken
 * @date 2018/5/19
 */
public class WeixinpayCfg {

    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(WeixinpayCfg.class.getClassLoader().getResourceAsStream("pay/WeixinpayCfg.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int merchant_id = Integer.parseInt(properties.getProperty("merchant.id"));

    public static final boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));

    public static final String notify_url = properties.getProperty("notify.url");

    public static final String refund_notify_url = properties.getProperty("refound_notify.url");

    public static final String merchant_key = properties.getProperty("merchant.key");

    public static final String trade_type = properties.getProperty("trade.type");

    public static final String limit_pay = properties.getProperty("limit_pay");

    public static final int timeout = Integer.parseInt(properties.getProperty("timeout"));

    public static final String ssl_path = properties.getProperty("ssl.path");

    public static final String app_id = properties.getProperty("app.id");

}