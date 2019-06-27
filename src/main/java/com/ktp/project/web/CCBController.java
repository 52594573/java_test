package com.ktp.project.web;

import COM.CCB.EnDecryptAlgorithm.MCipherEncryptor;
import com.ktp.project.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
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
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-10-11 下午 16:22
 */
@RestController
@RequestMapping(value = "api/ccb", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class CCBController {
    private static final Logger log = LoggerFactory.getLogger(CCBController.class);

    /**
     * 测试环境参数
     */
    //    直销银行场景化专用网关地址
    /*private static final String CCB_SERVER_URL = "http://juhetest.ccb.com";
    //    密钥
    //    private static final String strKey = "7a46f90f1a4acc75ad1ccb4d020111";
    private static final String strKey = "30819c300d06092a864886f70d010101050003818a0030818602818053499d537c990421af33e0d57e9b0d9e4d54d54a808b935efcee8e26460530d351600d00e16b58cb6006545cbdf23f357c5301706fb3921cf0478f62ab07fa3df8b9690aca7db9c7dc7a1a74dc9da22d19e54fb67f8e0755d31aeca392914f2c8c449c54b9aa0b5abd4cb02c851aa0ee7291b1517a46f90f1a4acc75ad1ccb4d020111";
    //    商户代码 由建行统一分配
    private static final String MERCHANTID = "DSB000000000083";
    //    商户柜台代码 由建行统一分配
    private static final String POSID = "100000083";
    //    分行代码 由建行统一指定
    private static final String BRANCHID = "441000000";
    //    第三方APP平台号 由建行统一分配
    private static final String APPID = "10690";
    //    第三方APP平台名字 由建行统一分配
    private static final String APPNAME = "建信开太平";
    //    交易码 由建行统一分配为DS0000
    private static final String TXCODE = "DS0000";*/

    /**
     * 生产环境参数
     */
    //    直销银行场景化专用网关地址
    /*private static final String CCB_SERVER_URL = "https://ibsbjstar.ccb.com.cn";
    //    密钥
    //    private static final String strKey = "47059cd21f42aaf0a31f84ef020111";
    private static final String strKey = "30819d300d06092a864886f70d010101050003818b003081870281810095dcf940bf4ecb37874858c4872bceb2c26f8a1578361a2b4be681106893638486daa3697c95b1b80cbd8df2d3008ddfbb36b877e761c1f40d4252e927bf5dce27c9b68a5745b002a54981179f59e9f9145229f079ab9db8d6f544801993a183c965780298c89b97e1dca09df36ad478f7fd02a047059cd21f42aaf0a31f84ef020111";
    //    商户代码 由建行统一分配
    private static final String MERCHANTID = "DSB000000000083";
    //    商户柜台代码 由建行统一分配
    private static final String POSID = "100000083";
    //    分行代码 由建行统一指定
    private static final String BRANCHID = "441000000";
    //    第三方APP平台号 由建行统一分配
    private static final String APPID = "10691";
    //    第三方APP平台名字 由建行统一分配
    private static final String APPNAME = "建信开太平";
    //    交易码 由建行统一分配为DS0000
    private static final String TXCODE = "DS0000";*/

    //    直销银行场景化专用网关地址
    @Value("${ccb.serverurl}")
    private String CCB_SERVER_URL;
    //    密钥
    //    private static final String strKey = "7a46f90f1a4acc75ad1ccb4d020111";
    @Value("${ccb.strKey}")
    private String strKey;
    //    商户代码 由建行统一分配
    @Value("${ccb.merchantid}")
    private String MERCHANTID;
    //    商户柜台代码 由建行统一分配
    @Value("${ccb.posid}")
    private String POSID;
    //    分行代码 由建行统一指定
    @Value("${ccb.branchid}")
    private String BRANCHID;
    //    第三方APP平台号 由建行统一分配
    @Value("${ccb.appid}")
    private String APPID;
    //    第三方APP平台名字 由建行统一分配
    @Value("${ccb.appname}")
    private String APPNAME ;
    //    交易码 由建行统一分配为DS0000
    @Value("${ccb.txcode}")
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
    public String doEncrypt(String NUSERID, String NAME, String USERID) {
        log.info("NUSERID:{}", NUSERID);
        log.info("NAME:{}", NAME);
        log.info("USERID:{}", USERID);
        //    证件类型 目前仅支持二代身份证，固定1010
        String IDTYPE = "1010";
        try {
            if (StringUtils.isBlank(NUSERID)) {
                log.error("NUSERID不能为空");
                return ResponseUtil.createBussniessErrorJson(500, "NUSERID不能为空");
            }
            if (StringUtils.isBlank(NAME) && StringUtils.isBlank(USERID)) {
                IDTYPE = "";
            }
            log.info("IDTYPE:{}", IDTYPE);
            //NAME，IDTYPE，USERID要么同时为空，要么同时有值
            if ((StringUtils.isNotBlank(NAME) && StringUtils.isNotBlank(IDTYPE) && StringUtils.isNotBlank(USERID))
                    || (StringUtils.isBlank(NAME) && StringUtils.isBlank(IDTYPE) && StringUtils.isBlank(USERID))) {
                Long seconds = System.currentTimeMillis();
                //需要加密的参数
                String params = "MERCHANTID=" + MERCHANTID + "&BRANCHID=" + BRANCHID + "&APPID=" + APPID +
                        "&APPNAME=" + APPNAME + "&NUSERID=" + NUSERID + "&NAME=" + NAME + "&IDTYPE=" + IDTYPE +
                        "&USERID=" + USERID + "&TXCODE=" + TXCODE + "&TIMESTAMP=" + seconds;
                //创建加密对象
                String key = this.subKey(strKey);
                MCipherEncryptor encryptor = new MCipherEncryptor(key);
                //执行加密
                String ccbParam = encryptor.doEncrypt(params);
                //加密后组成最终的请求URL
                String url = CCB_SERVER_URL + "/CCBIS/CCBETradReqServlet?MERCHANTID=" + MERCHANTID +
                        "&POSID=" + POSID + "&BRANCHID=" + BRANCHID + "&CHARSET=utf-8" + "&ccbParam=" + ccbParam;
                Map<String, Object> map = new HashMap<>();
                map.put("url", url);
                return ResponseUtil.createNormalJson(map);
            } else {
                log.error("NAME，IDTYPE，USERID要么同时为空，要么同时有值");
                return ResponseUtil.createBussniessErrorJson(500, "NAME，IDTYPE，USERID要么同时为空，要么同时有值");
            }
        } catch (InvalidKeyException
                | NoSuchAlgorithmException
                | NoSuchPaddingException
                | ShortBufferException
                | IllegalBlockSizeException
                | BadPaddingException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 截取公钥后30位
     *
     * @return java.lang.String
     * @Author: wuyeming
     * @params: [strKey]
     * @Date: 2018-10-12 下午 14:35
     */
    private String subKey(String strKey) {
        String key = strKey.substring(strKey.length() - 30, strKey.length());
        return key;
    }

}
