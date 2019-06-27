package com.ktp.project.web;

import COM.CCB.EnDecryptAlgorithm.MCipherEncryptor;
import com.ktp.project.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wuyeming
 * @Date: 2018-10-11 下午 16:22
 * 龙支付
 */
@RestController
@RequestMapping(value = "api/ccbDragonPay", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class CCBDragonPayController {
    private static final Logger log = LoggerFactory.getLogger(CCBDragonPayController.class);


    //    直销银行场景化专用网关地址inshi
    @Value("${ccb.dp.serverurl}")
    private String CCB_SERVER_URL;
    //    密钥
    //    private static final String strKey = "7a46f90f1a4acc75ad1ccb4d020111";
    @Value("${ccb.dp.strKey}")
    private String strKey;
    //    商户代码 由建行统一分配
    @Value("${ccb.dp.merchantid}")
    private String MERCHANTID;
    //    商户柜台代码 由建行统一分配
    @Value("${ccb.dp.posid}")
    private String POSID;
    //    分行代码 由建行统一指定
    @Value("${ccb.dp.branchid}")
    private String BRANCHID;
    //    第三方APP平台号 由建行统一分配
    @Value("${ccb.dp.appid}")
    private String APPID;
    //    第三方APP平台名字 由建行统一分配
    @Value("${ccb.dp.appname}")
    private String APPNAME;
    //    交易码 由建行统一分配为DS0000
    @Value("${ccb.dp.txcode}")
    private String TXCODE;

    /**
     * @param NUSERID 客户在第三方APP平台的唯一标识 必输字段
     * @param NAME    客户姓名 由第三方APP提供
     * @param USERID  证件号码 由第三方APP提供，目前仅支持18位二代身份证
     * @return java.lang.String
     * @Author: wuyeming
     * @Date: 2018-10-12 下午 13:46
     */
    @RequestMapping(value = "doEncrypt", method = RequestMethod.POST)
    public String doEncrypt(String NUSERID, String NAME, String USERID, String ACCNO, String FUNCODE) {
        log.info("NUSERID:{}", NUSERID);
        log.info("NAME:{}", NAME);
        log.info("USERID:{}", USERID);
        log.info("ACCNO:{}", ACCNO);
        log.info("FUNCODE:{}", FUNCODE);
        //    证件类型 目前仅支持二代身份证，固定1010
        String IDTYPE = "1010";
        try {
            Assert.assertTrue("NUSERID不能为空", StringUtils.isNotBlank(NUSERID));
            Assert.assertTrue("FUNCODE不能为空", StringUtils.isNotBlank(FUNCODE));
            //NAME，IDTYPE，USERID要么同时为空，要么同时有值
            Assert.assertTrue("NAME，IDTYPE，USERID要么同时为空，要么同时有值", (StringUtils.isNotBlank(NAME) && StringUtils.isNotBlank(IDTYPE) && StringUtils.isNotBlank(USERID))
                    || (StringUtils.isBlank(NAME) && StringUtils.isBlank(IDTYPE) && StringUtils.isBlank(USERID)));
            if (StringUtils.isBlank(NAME) && StringUtils.isBlank(USERID)) {
                IDTYPE = "";
            }
            log.info("IDTYPE:{}", IDTYPE);
            String url = getUrl(NUSERID, NAME, USERID, IDTYPE, ACCNO, FUNCODE);
            Map<String, Object> map = new HashMap<>();
            map.put("url", url);
            return ResponseUtil.createNormalJson(map);
        } catch (InvalidKeyException
                | NoSuchAlgorithmException
                | NoSuchPaddingException
                | ShortBufferException
                | IllegalBlockSizeException
                | BadPaddingException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | UnsupportedEncodingException
                | AssertionError e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 生成Url
     *
     * @return java.lang.String
     * @params: [NUSERID, NAME, USERID, IDTYPE, ACCNO, FUNCODE]
     * @Author: wuyeming
     * @Date: 19-3-4 下午2:46
     */
    private String getUrl(String NUSERID, String NAME, String USERID, String IDTYPE, String ACCNO, String FUNCODE) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, UnsupportedEncodingException {
        Long seconds = System.currentTimeMillis();
        //要加密的参数
        String params = doEncryptParams(NUSERID, NAME, USERID, IDTYPE, ACCNO, FUNCODE, seconds);
        String key = this.subKey(strKey, strKey.length() - 30);//截取公钥后30位
        //创建加密对象
        MCipherEncryptor encryptor = new MCipherEncryptor(key);
        //执行加密
        String ccbParam = encryptor.doEncrypt(params);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("MERCHANTID", MERCHANTID);
        map.put("POSID", POSID);
        map.put("BRANCHID", BRANCHID);
        map.put("CHARSET", "utf-8");
        map.put("ccbParam", ccbParam);
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(CCB_SERVER_URL + "/CCBIS/CCBETradReqServlet?");
        urlBuffer.append(appendParams(map));
        //加密后组成最终的请求URL
        return urlBuffer.toString();
    }


    /**
     * 拼接需要加密的参数
     *
     * @return java.lang.String
     * @params: [NUSERID, NAME, USERID, IDTYPE, ACCNO, FUNCODE, seconds]
     * @Author: wuyeming
     * @Date: 19-3-4 下午2:46
     */
    private String doEncryptParams(String NUSERID, String NAME, String USERID, String IDTYPE, String ACCNO, String FUNCODE, Long seconds) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("MERCHANTID", MERCHANTID);
        map.put("POSID", POSID);
        map.put("BRANCHID", BRANCHID);
        map.put("APPID", APPID);
        map.put("APPNAME", APPNAME);
        map.put("NUSERID", NUSERID);
        map.put("NAME", NAME);
        map.put("IDTYPE", IDTYPE);
        map.put("USERID", USERID);
        map.put("TXCODE", TXCODE);
        map.put("ACCNO", ACCNO);
        map.put("FUNCODE", FUNCODE);
        map.put("TIMESTAMP", seconds);
        return appendParams(map);
    }

    /**
     * 截取公钥
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [strKey]
     * @Date: 2018-10-12 下午 14:35
     */
    private static String subKey(String strKey, int beginIndex) {
        String key = strKey.substring(beginIndex);
        return key;
    }

    /**
     * 拼接参数
     *
     * @return java.lang.String
     * @params: [map]
     * @Author: wuyeming
     * @Date: 19-3-4 下午2:45
     */
    private String appendParams(LinkedHashMap<String, Object> map) {
        StringBuffer paramsBuffer = new StringBuffer();
        Set<String> set = map.keySet();
        int i = 0;
        for (String key : set) {
            if (i == 0) {
                paramsBuffer.append(key + "=" + map.get(key));
            } else {
                paramsBuffer.append("&" + key + "=" + map.get(key));
            }
            i++;
        }
        return paramsBuffer.toString();
    }

}
