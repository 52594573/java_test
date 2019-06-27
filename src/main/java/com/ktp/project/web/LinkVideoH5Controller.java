package com.ktp.project.web;

import com.ktp.project.service.BenefitService;
import com.ktp.project.util.LoanUtils;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import com.ktp.project.util.redis.RedisClientTemplate;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 获取视频地址
 */
@Controller
@RequestMapping(value = "api/linkVideo/h5", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class LinkVideoH5Controller {

    /**
     * 获取缓存中jiuniu视频地址
     */
    @RequestMapping(value = "videoAddress", method = RequestMethod.GET)
    @ResponseBody
    public String getDonateShare() {
        String videoAddress =RedisClientTemplate.get("ktp_linkVideo_videoAddress");
        //删除打不开的地址
        if("http://file.ktpis.com/video_2018KTP%20advertising%20video.mp4".equals(videoAddress)){
            videoAddress="";
        }
        //初始化
        if(StringUtil.isEmpty(videoAddress)){
            RedisClientTemplate.set("ktp_linkVideo_videoAddress","https://file.ktpis.com/video_9_20181217182622781_4798.mp4");
           // RedisClientTemplate.set("ktp_linkVideo_videoAddress","http://file.ktpis.com/video_2018KTP%20advertising%20video.mp4");
            videoAddress =RedisClientTemplate.get("ktp_linkVideo_videoAddress");
        }
        return  ResponseUtil.createNormalJson(videoAddress);
    }


}
