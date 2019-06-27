package com.ktp.project.web;

import com.ktp.project.exception.ParamException;
import com.ktp.project.util.SecurityUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 码上花商场
 *
 * @author djcken
 * @date 2018/11/12
 */
@Controller
@RequestMapping(value = "api/mshmall", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class MaShangHuaMallController {
    @Autowired
    private SecurityUtil securityUtil;

//    private static final String MSH_MALL_URL = "http://tmsh-m.z-code.cn:8383/";//测试服域名
//    private static final String MSH_MALL_URL = "http://msh-m.z-code.cn/";//正式服域名
    @Value("${msh.mallurl}")
    private String MSH_MALL_URL;

    @RequestMapping(value = "url", method = RequestMethod.GET)
    public String url(@Param(value = "mobile") String mobile, HttpServletRequest request) {
        String decodeMobile = "";
        try {
            decodeMobile = securityUtil.encryptAES(mobile);
        } catch (ParamException e) {
            e.printStackTrace();
        }
        String redirectUrl = MSH_MALL_URL + "?KTPTelephone=" + decodeMobile;
        return "redirect:" + redirectUrl;
    }
}
