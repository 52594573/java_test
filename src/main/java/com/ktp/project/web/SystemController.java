package com.ktp.project.web;

import com.ktp.project.entity.Config;
import com.ktp.project.service.SystemService;
import com.ktp.project.util.ResponseUtil;
import com.zm.entity.KeyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author djcken
 * @date 2018/7/16
 */
@Controller
@RequestMapping(value = "api/system", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class SystemController {

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "config", method = RequestMethod.GET)
    @ResponseBody
    public String config(HttpServletRequest request) {
        KeyContent keyContent = systemService.getKeyContent();
        Config config = new Config();
        config.setAppstoreAudit(keyContent != null && "1".equals(keyContent.getKeyValue()));
        config.setAuditVersion(keyContent!=null ? keyContent.getKeyValue() : "");
        return ResponseUtil.createNormalJson(config);
    }

}
