package com.ktp.project.web;

import com.ktp.project.dto.project.*;
import com.ktp.project.service.ProjectCommonService;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
*
* @Description: 项目基本信息，班组基本信息，工作人员考勤，工资基本信息
* @Author: liaosh
* @Date: 2018/12/13 0013
*/
@Controller
@RequestMapping(value = "api/projectCom", produces = "application/json;charset=UTF-8;")
public class ProjectCommonController {
    private static final Logger log = LoggerFactory.getLogger(ProjectCommonController.class);

    @Autowired
    private ProjectCommonService projectCommonService;

    /** 
    *
    * @Description:   项目基本信息
    * @Author: liaosh
    * @Date: 2018/12/13 0013 
    */ 
    @RequestMapping(value = "/getProMessage", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectMessage(String signToken,Integer proId){
        try {
            ProMessage proMessage = projectCommonService.getProjectMessage(proId);
            return ResponseUtil.createNormalJson(proMessage);
        } catch (Exception e) {

            log.error("获取项目基本信息异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }
    
    /** 
    *
    * @Description:   班组信息
    * @Author: liaosh
    * @Date: 2018/12/14 0014 
    */
    @RequestMapping(value = "/getPoMessage", method = RequestMethod.GET)
    @ResponseBody
    public String getProjectMessage(Integer proId){
        try {
            PoMessage poMessage = projectCommonService.getPojectMessage(proId);
            return ResponseUtil.createNormalJson(poMessage);
        } catch (Exception e) {

            log.error("获取项目基本信息异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }

   /** 
   *
   * @Description: 班组人员
   * @Author: liaosh
   * @Date: 2018/12/17 0017 
   */ 
    @RequestMapping(value = "/getPoUser", method = RequestMethod.GET)
    @ResponseBody
    public String getPoUser(Integer proId,Integer poId){
        try {
            PoUser poUser = projectCommonService.getPoUser(proId,poId);
            return ResponseUtil.createNormalJson(poUser);
        } catch (Exception e) {

            log.error("获取班组人员基本信息异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }

     /**
     *
     * @Description: 人员考勤
     * @Author: liaosh
     * @Date: 2018/12/17 0017
    */
    @RequestMapping(value = "/getClocking", method = RequestMethod.GET)
    @ResponseBody
    public String getClocking(Integer proId,Integer uId){
        try {
            KaoqinUser kaoqinUser = projectCommonService.getClocking(proId,uId);
            return ResponseUtil.createNormalJson(kaoqinUser);
        } catch (Exception e) {

            log.error("获取班组人员基本信息异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }

   /** 
   *
   * @Description: 人员工资
   * @Author: liaosh
   * @Date: 2018/12/17 0017 
   */ 
    @RequestMapping(value = "/getWageUser", method = RequestMethod.GET)
    @ResponseBody
    public String getWageUser(Integer proId,Integer poId,Integer uId){
        try {
            WageUser wageUser = projectCommonService.getWageUser(proId,poId,uId);
            return ResponseUtil.createNormalJson(wageUser);
        } catch (Exception e) {

            log.error("获取班组人员基本信息异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }

}
