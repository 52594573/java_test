package com.ktp.project.web;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.common.collect.ImmutableMap;
import com.ktp.project.entity.Sms;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.exception.TokenException;
import com.ktp.project.service.SmsService;
import com.ktp.project.util.Md5Util;
import com.ktp.project.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by LinHon 2018/8/3
 */
@Controller
@RequestMapping(value = "sms", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class SmsController {

    private final Logger log = LoggerFactory.getLogger(SmsController.class);

    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    private final String PRODUCT = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    private final String DOMAIN = "dysmsapi.aliyuncs.com";
    /**
     * 访问控制台ACCESS_KEY_ID
     */
    private final String ACCESS_KEY_ID = "LTAIchPrRJE6NkZw";
    /**
     * 访问控制台ACCESS_KEY_SECRET
     */
    private final String ACCESS_KEY_SECRET = "AlvQNP5x1uzMACaTTzYpu7T6ZQ95Z1";
    /**
     * 短信模板签名名称
     */
    private final String SIGN_NAME = "开太平";
    /**
     * 约定密码
     */
    private final String PASSWORD = "FAFJJeremf@#$&245";

    @Autowired
    private SmsService smsService;


    /**
     * 发送短信
     *
     * @param mobile
     * @param type
     * @param code
     * @param signKey
     * @return
     */
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    @ResponseBody
    public String sendSms(@RequestParam("mobile") String mobile, @RequestParam("type") int type, @RequestParam("code") String code, @RequestParam("time") String time, @RequestParam("signKey") String signKey) {
        try {
            check(mobile, time, signKey);

            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsResponse response = acsClient.getAcsResponse(buildSendSmsRequest(mobile, type, code));
            if ("ok".equalsIgnoreCase(response.getCode())) {
                smsService.create(new Sms(mobile, code, type));
            } else {
                return ResponseUtil.createBussniessErrorJson(500, "获取短信失败");
            }

        } catch (Exception e) {
            log.error("发送短信异常", e);
            if (e instanceof TokenException) {
                return ResponseUtil.createBussniessErrorJson(401, e.getMessage());
            } else if (e instanceof BusinessException) {
                return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
            } else {
                return ResponseUtil.createBussniessErrorJson(500, "获取短信异常");
            }
        }
        return ResponseUtil.createBussniessJson("成功");
    }

    private void check(String mobile, String time, String signKey) throws Exception {
        if (!signKey.equalsIgnoreCase(Md5Util.MD5Encode(mobile + time + PASSWORD))) {
            throw new TokenException("验证错误");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long checkTime = dateFormat.parse(time).getTime();
        long thisTime = new Date().getTime();
        if ((thisTime - checkTime) < 0 || (thisTime - checkTime) > (1000 * 60 * 60)) {
            throw new TokenException("已失效");
        }

        if (smsService.countByMobile(mobile) != 0) {
            throw new BusinessException("短信接收间隔为5分钟");
        }
    }

    /**
     * 构建请求阿里短信接口参数
     *
     * @param mobile
     * @param type
     * @param code
     * @return
     * @throws Exception
     */
    private SendSmsRequest buildSendSmsRequest(String mobile, int type, String code) throws Exception {
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(SIGN_NAME);
        //必填:短信模板-可在短信控制台中找到
        switch (type) {
            case 1:
                request.setTemplateCode("SMS_81040049");
                break;
            case 2:
                request.setTemplateCode("SMS_81040047");
                break;
            case 3:
                request.setTemplateCode("SMS_81040046");
                break;
            case 4:
                request.setTemplateCode("SMS_81040045");
                break;
            case 5:
                request.setTemplateCode("SMS_81040044");
                break;
            case 6:
                request.setTemplateCode("SMS_81040043");
                break;
            default:
                throw new BusinessException("短信模板类型错误");
        }
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(JSONObject.fromObject(ImmutableMap.of("code", code)).toString());
        request.setMethod(MethodType.POST);
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        return request;
    }

}
