package com.ktp.project.web;

import com.icbc.api.IcbcApiException;
import com.icbc.api.IcbcConstants;
import com.icbc.api.ThirdPartyIIAccountRequestV1;
import com.icbc.api.ThirdPartyIIAccountRequestV1.ThirdPartyIIAccountRequestBizV1;
import com.icbc.api.UiIcbcClient;
import com.ktp.project.exception.BusinessException;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;

/**
 * @author djcken
 * @date 2018/6/13
 */
@Controller
@RequestMapping(value = "api/icbc", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class IcbcController {

    private Logger logger = LoggerFactory.getLogger("IcbcController");

    //开太平appid 10000000000000237501
    private static final String APP_ID = "10000000000000237501";
    //测试appid 10000000000000004950  10000000000000051503 10000000000000053003 10000000000000056003
//    private static final String APP_ID = "10000000000000056003";
    //开太平密钥
    private static final String MY_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDX57QJ2ayXVkWqvtX27YyCrYY4q4mHT7CvpvysSs5XkC0DdHxJIda4aPiA+hoZpE0KvCXHk7loL1DxRz2PccZlNsLTt/bJ+yf4Eo8+9QiEnVJyahrP+toOynOjuk3j8w+VveZPMpD+sB0fscQd9vyQ/1z6rFsn2GcrNNosh0x2SxoxbT4DO+z2WoRvgKr0M1zHD9lAOy3QnNAXNjly4C3CE6Q6/LDIR/w6VY40cmTMg+CVymECKsyNHsTMjnjQ2GAp4q/2z/1Jet/SvZQq30gIOQ0Yld2lxp+x/U4d4zzM6UYNOAMiV8U86RF3KpkSPkJAmnZmtt0ZT8Hg11BS5rqlAgMBAAECggEAZDB1b4y/dormUVG1YJzjc1Cq1GllrJDiYOCQqDMehau198B6XEWDqpermc51hiikR7L/pRl8FEjAuvZgLsyRUC6VQkJAF4KhDZEvP/vdNAz9UXLDdnfl8K8p8nRl6L3GIaq50U6Z0lZPbYHEjFMs6OxXbUvGETtuxLIF/Fvcs5dUFMNAdOHKDJROdpn7pRHN+khwXfEFYrKjn9ckMcaLD0zTJ/R9+2F6EPm3s2Db870/dXGzfY8EMxFXdHSpYCu3PRIhLVxcTAJL+aOwc2ikqxP12JjN39ri+y9cw7TtvqYvUnhhWVdetC5n2s02Zy15Apf9aFRiytraFRejp5ouAQKBgQDwT68lUULT+df8Qyry+pJgIIVGOdXBWXGeMdSHJMHSpzjEk+Udw7PKFSyHwqRaLfiw8w4S/4ySSSIjeQ7kaJlrD1TfLdhisb0qQUa/yuf9ITfjLBeqWsw8X6bOq+aovROIYL8uUx3u4WohQvp6oweT0s3o4awRtxTf+pxCpiLGhQKBgQDmAB6NqrcCtb9Zg7UFqXI53RFe0KVNl3V8sSJ+8wQEp/iEXUE1eywj+5QXzzyn9V7zAV/wAdsw2pV5lFBbkE4dRDof2UBAc9wUnyvLDjzJ6VKSgECYb012xBeZ2eF+ppSJlEx8+zGwR4UITVTGwIeoiTLg9BuArKD/mPmf4WCtoQKBgQCINBHIK+Ods5kIYKWCLCU0mJoRMIyH1wM/r2yO4HV+yMqzGEQe96H6Yr/nfpmahz3SRSBwAimRbCQOSPn42ZRDCuCG0jw3zBe0UGUiJYYRgXdkqcQMCQSDH4+yydmlP+1HOdaCEhgAIgD+MzE2uxp7XVrZqLbXH5aUUD2qxPEm5QKBgDpc0WP1a9ceM14Nk8PKCZgxRpUesLONuVxYvn4kQGLGFMPRvhhKFxtSXXPlPGkKVHupX6TRc8Nj1AYz4mg5p/i5NV6bjDhOrgv1j/FIvZgmwrM2JC4KIEgA3lRF+3hd4NdAcGBOhX9LxrAin16HwGS929FFYC04oOn68WzmjdfBAoGBALJVvvFzQih1xMShhLL5MAz0rNVV6c3x54dfnEayDES5G50zpQmBjTbKmkOTeH/JQImXniWpNhjm4pvJozF/HNl+LXJa+to0BTGp9BXfIlIPC93ucXRLzjAzK8lneKppNRhOZGzultF+d7AcXkr46roR1vuNLT0iZwC8RTADWyO0";
    //测试环境密钥
//    protected static String MY_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJCoQe/O+uqpOtyE/2CUjD7wosZw8jI1AlLNJCllOmlX+obA6h97b0JsEL0SjMCAR3xJyw7MKLqkcy5qQ/bBgw2XSrodmjzOVqfT1OXRii0xw0HiVkHR4cEWnEAAdfo0lDc4iuzCIQrnT5gM0+U+qeSV6JFfwVRjgYzBdHQRPCf1AgMBAAECgYAdQUoEe7GXH5591o/nMmOinvvscg8pRDsyD7bOgGBtyZMrCXzP3SDFKCHCeyvoColqg2oDlhpulK+OpYMVNlGQcO4eubOJp9MUc3m4A9RxkVr3dsVrmygM5czPfWHAPVQ4dECDruBJn9zoo1ci0myRTh4KSCq6SxpCE0Pbf3j7iQJBAOdMPFRfOkSQ1hsN9Mg4jd9on8r7+mF8gFPhkNI6qvEKw/prmU3obUWvZL42vRlLOQyyB92mBF24PStR/B6CjR8CQQCgGz0VoqCplurSCapGdgX3D7bNSDtMUmaLJYJxih8v+zghP0YtVgDeV3NjogVjOlz8/9Rebo0PoFcqyJnNA1RrAkEAul3dBoasZm7ldWsrXuDiv66HgoDB4Cb3J59Kl3oaHpp0CqUEI5gx48JNRE7K00SfNTGF0Pxh7Dn1X6Bxqwu6NQJADrdyPfLc4bnFi9jnleJzWepP2z6wdKt+UXv5KYaQp1BoMGYohTJKkiVnrdjOtfg/Y+IAG03+GVmbqYsW2AleUQJBANYQklohKtsmq8ptX3as6hjcuXcTpc2DSpVeOnCueNv107+dDJ+K14tbiVYzF3tkHBNMRkGtr6EuGKJ+A63sJ00=";
    //开太平AES_KEY
    private static final String AES_KEY = "R+GtdYvdQbncSWvr40vC0g==";
    //测试环境AES_KEY
//    private static final String AES_KEY = "xMh0xFsG7G80ziePFdnT8g==";
    //工行正式地址
    private static final String ICBC_SERVER_URL = "https://gw.open.icbc.com.cn";
    //工行测试地址 http://122.20.173.105:8081
//    private static final String ICBC_SERVER_URL = "https://apipcs.dccnet.com.cn";

    @RequestMapping(value = "account", method = RequestMethod.GET)
    @ResponseBody
    public String openAccount(@Param(value = "u_id") int u_id, @Param(value = "mobile") String mobile, @Param(value = "idCode") String idCode, @Param(value = "name") String name) {
        if (u_id <= 0 || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(idCode) || TextUtils.isEmpty(name)) {
            throw new BusinessException("缺少必要参数");
        }
        //  public UiIcbcClient(String appId, String signType, String privateKey, String charset, String encryptType, String encryptKey)
        UiIcbcClient client = new UiIcbcClient(APP_ID, IcbcConstants.SIGN_TYPE_RSA, MY_PRIVATE_KEY, IcbcConstants.CHARSET_UTF8, IcbcConstants.ENCRYPT_TYPE_AES, AES_KEY);
        ThirdPartyIIAccountRequestV1 request = new ThirdPartyIIAccountRequestV1();
        request.setServiceUrl(ICBC_SERVER_URL + "/ui/thirdParty/IIAccount/servlet/homePage/V1");///thirdParty/IIAccount/homePage/V1   thirdParty/IIAccount/V1/homePage
        //request.setServiceUrl("http://122.19.77.102:8081/ui/thirdParty/IIAccount/homePage/V1");

        ThirdPartyIIAccountRequestBizV1 bizContent = new ThirdPartyIIAccountRequestBizV1();
        bizContent.setMobileNo(mobile);//实际调用时需控制手机号不能为空,请自行添加
        bizContent.setIdcode(idCode);//实际调用时需控制身份证号不能为空，请自行添加
        bizContent.setName(URLEncoder.encode(name));//实际调用时需控制姓名不能为空，请自行添加
        request.setBizContent(bizContent);
        String result = null;// 实际调用时的相关返回结果及异常处理，请自行添加
        try {
            result = client.buildPostForm(request);
        } catch (IcbcApiException e) {
            e.printStackTrace();
        }
        logger.debug("icbc callback is " + result);
        return result;
    }

}
