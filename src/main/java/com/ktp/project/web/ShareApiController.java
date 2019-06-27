package com.ktp.project.web;

import com.ktp.project.entity.ShareInfo;
import com.ktp.project.service.ShareService;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "api/share", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class ShareApiController {

    private Logger logger = LoggerFactory.getLogger("ShareApiController");

    @Autowired
    private ShareService shareService;

    @RequestMapping(value = "appShareInfo", method = RequestMethod.GET)
    @ResponseBody
    public String appShareInfo(HttpServletRequest request) {
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setShareTitle("我正在使用“建信开太平”APP，大家快来下载吧。");
        shareInfo.setShareContent("加入建信开太平，未来有保障。帮助农民工尽早脱贫致富，实现向建筑产业工人的华丽转变");
        shareInfo.setSharePic("https://images.ktpis.com/app_logo.png");
        shareInfo.setShareCount(0);
        return ResponseUtil.createNormalJson(shareInfo);
    }

}
