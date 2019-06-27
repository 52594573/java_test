package com.ktp.project.web;

import com.ktp.project.service.RiskWarningService;
import com.ktp.project.util.ResponseUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
*
* @Description: 风险预警消息设置
* @Author: liaosh
* @Date: 2018/12/10 0010 
*/
@Controller
@RequestMapping(value = "api/riskWarning", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class RiskWarningController {
    private Logger logger = LoggerFactory.getLogger("RiskWarningController");

    @Autowired
    private RiskWarningService riskWarningService;

    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public String getList(HttpServletRequest request, @Param(value = "userId") int userId,@Param(value = "proId") int proId) {
        try {
            return ResponseUtil.createNormalJson(riskWarningService.getList(userId,proId));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(10,e.getMessage());
        }
    }

    @RequestMapping(value = "setStatus", method = RequestMethod.POST)
    @ResponseBody
    public String setStatus(HttpServletRequest request, @Param(value = "id") int id, @Param(value = "status") int status) {
        try {
            riskWarningService.setStatus(id,status);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.createNormalJson(10,"更新异常");
        }

        return ResponseUtil.createNormalJson(null);
    }


}
