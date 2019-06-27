package com.ktp.project.web;

import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 珠海整合国际广场项目 接口
 *
 * @author lsh
 * @date 2019年1月12日10:43:18
 */
@Controller
@RequestMapping(value = "api/zhzhService", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class ZhzhServiceController {
    private static final Logger log = LoggerFactory.getLogger(ZhzhServiceController.class);

    @Resource(name = "zhzhAuthRealNameService")
    private AuthRealNameApi zhzhAuthRealNameService;
    @Autowired
    private com.ktp.project.service.realName.ZhzhAuthRealNameApi zhzhAuthRealNameApi;

    /**
     *批量采集
     */
    @RequestMapping(value = "CaijiAll", method = RequestMethod.POST)
    @ResponseBody
    public String CaijiAll(Integer projectId) {
        try {
            zhzhAuthRealNameApi.CaijiAll(projectId);
            return ResponseUtil.createBussniessJson("批量采集信息成功");
        } catch (Exception e) {
            log.error("批量采集异常", e);
            return ResponseUtil.createApiErrorJson(999, "批量采集失败");
        }
    }

    /**
    *采集人员信息
    * @Description:
    * @Author: liaosh
    * @Date: 2019/1/18 0018
    */
    @RequestMapping(value = "uploadWorkerFeature", method = RequestMethod.POST)
    @ResponseBody
    public String uploadWorkerFeature(Integer userId,String tp) {
        try {
            zhzhAuthRealNameApi.uploadWorkerFeature(userId,tp);
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            return ResponseUtil.createApiErrorJson(999, "失败");
        }
    }

/**
 *批量考勤
 */
    @RequestMapping(value = "kaoqingAll", method = RequestMethod.POST)
    @ResponseBody
    public String kaoqingAll(Integer projectId) {
        try {
            zhzhAuthRealNameApi.kaoqingAll(projectId);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }

    /**
     * 同步考勤信息
     *
     * @param projectId 项目id
     * @param kaoQinId  考勤id
     */
    @RequestMapping(value = "synWorkerAttendance", method = RequestMethod.POST)
    @ResponseBody
    public String synWorkerAttendance(Integer projectId, Integer kaoQinId) {
        try {
            zhzhAuthRealNameService.synWorkerAttendance(projectId, kaoQinId);
            return ResponseUtil.createBussniessJson("同步考勤信息成功");
        } catch (Exception e) {
            log.error("同步考勤信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "同步考勤信息失败");
        }
    }


}
