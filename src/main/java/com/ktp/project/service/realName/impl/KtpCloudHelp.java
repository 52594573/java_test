package com.ktp.project.service.realName.impl;

import com.alibaba.fastjson.JSONObject;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.redis.RedisClientTemplate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * http请求调Cloud项目
 */
public class KtpCloudHelp {
    private static Logger logger = LoggerFactory.getLogger(KtpCloudHelp.class);

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        String token = RedisClientTemplate.get(RealNameConfig.CLOUD_TOKEN_KEY);
        if (StringUtils.isBlank(token)){
            String url = RealNameConfig.KTP_CLOUD_REQ_IP + "/auth/app/token/login?grant_type=app_mobile&app_mobile=APP_SMS@eyJ1c2VySW1lYSI6IjEyMzQ1NiIsInBob25lIjoiMTcwMzQ2NDI4ODgiLCJ2ZXJzaW9uIjoidjEuMCJ9";
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Basic " + RealNameConfig.OPENAPI_SECRET);
            header.put("TENANT_ID", "1");
            Map<String, Object> params = new HashMap<>();
            String data = HttpClientUtils.post(url, GsonUtil.toJson(params), "application/json", header);
            if (StringUtils.isBlank(data)){
                throw new BusinessException("远程调用KTP_CLOUD失败");
            }
            JSONObject jsonObject = JSONObject.parseObject(data);
            token = jsonObject.getString("access_token");
            RedisClientTemplate.set(RealNameConfig.CLOUD_TOKEN_KEY, token, 3600 * 24 * 10);
        }
        return token ;
    }

    /**
     * 远程调用实名同步系统
     * @param url
     * @param data
     * @param method
     * @return
     */
    public static String sendKtpCloud(String url, Map<String, Object> data, RequestMethod method){
        url =  RealNameConfig.KTP_CLOUD_REQ_IP + url;
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "bearer " + getToken());
        header.put("TENANT_ID", "1");
        String result = null;
        switch (method) {
            case GET:
                result = HttpClientUtils.get(createFullRequest(url, data), header);
                break;
            case POST:
                result = HttpClientUtils.post(url, GsonUtil.toJson(data), "application/json", header);
                break;
            default:
                throw new BusinessException("目前只支持GET和POST请求");
        }
        logger.info("远程请求ktp_cloud --- 请求Url: {}, 请求参数: {}, 请求方法: {}， 返回结果： {}", url, GsonUtil.toJson(data), method, result);
        return result;
    }

    /**
     * 解析返回结果
     * @param result
     * @return
     */
    public static boolean analyzeResult(String result){
        if (StringUtils.isBlank(result)){
            throw new BusinessException("传入字符串不能为空");
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer code = Integer.parseInt(String.valueOf(jsonObject.get("code")));
        String msg = String.valueOf(jsonObject.get("msg"));
        String data = String.valueOf(jsonObject.get("data"));
        if (!code.equals(0)){
            logger.error("远程请求ktp_cloud系统失败,返回错误消息: {}", msg);
            return false;
        }
        logger.info("远程请求ktp_cloud系统成功,返回消息: {}, 返回body: {}", msg, data);
        return true;

    }

    /**
     * 凭借get请求url
     * @param url
     * @param data
     * @return
     */
    private static String createFullRequest(String url, Map<String,Object> data) {
        StringBuilder builder = new StringBuilder(url);
        Integer index = 0;
        for (String key : data.keySet()) {
            if (index.equals(0)){
                builder.append("?");
            }else {
                builder.append("&");
            }
            builder.append(key).append("=").append(data.get(key));
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", 993307);
        data.put("poId", 912904);
        data.put("type", "POUSERSAV");
        String s = KtpCloudHelp.sendKtpCloud(RealNameConfig.SYN_POP, data, RequestMethod.POST);
        System.out.println(s);
    }
}
