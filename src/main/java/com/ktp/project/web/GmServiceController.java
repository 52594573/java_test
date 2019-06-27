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
import java.util.List;

/**
 * 高明项目接口
 *
 * @author djcken
 * @date 2018/6/11
 */
@Controller
@RequestMapping(value = "api/gmService",produces = {"text/html;charset=UTF-8;", "application/json;"})
public class GmServiceController {
    private static final Logger log = LoggerFactory.getLogger(GmServiceController.class);

    @Resource(name="gmAuthRealNameService")
    private AuthRealNameApi gmAuthRealNameService;
    @Autowired
    private com.ktp.project.service.realName.GmAuthRealNameApi gmAuthRealNameApi;
    /**
     * 获取数据字典
     *
     * @param type -数据类型
     * @return list  符合条件的list
     */
    @RequestMapping(value = "getBaseDataDictionary", method = RequestMethod.POST)
    @ResponseBody
    public String getBaseDataDictionary(int  type) {
        try {
         // List baseDataDictionaryList =  gmAuthRealNameApi.getBaseDataDictionary(type);
            return  ResponseUtil.createNormalJson(null);
        }catch (Exception e){
            return ResponseUtil.createApiErrorJson(999, "失败");
        }
    }

    /**
     *  同步企业信息
     * @param corpCode 企业code
     * @param corpName 企业名称
     * @param areaCode 企业所在地区code
     * @param registerDate 注册日期  YYYY-MM-DD
     */
    @RequestMapping(value = "synCompanyInfo", method = RequestMethod.POST)
    @ResponseBody
    public String synCompanyInfo(String corpCode, String corpName, String areaCode, String registerDate) {
        try {
            gmAuthRealNameApi.synCompanyInfo( corpCode,  corpName,  areaCode,registerDate);
            return  ResponseUtil.createBussniessJson("同步企业成功");
        }catch (Exception e){
            log.error("同步企业异常", e);
            return ResponseUtil.createApiErrorJson(999, "同步企业失败");
        }
    }

    /**
     * 上传项目信息
     * @param projectId 项目id
     */
    @RequestMapping(value = "synWorkinfo", method = RequestMethod.POST)
    @ResponseBody
    public String synWorkinfo(Integer projectId) {
        try {
            gmAuthRealNameApi.synWorkinfo(projectId);
            return  ResponseUtil.createBussniessJson("上传项目信息成功");
        }catch (Exception e){
            log.error("上传项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传项目失败");
        }
    }
    /**
     * 上传和修改班组信息
     * @param projectId 项目id
     * @param teamId  班组id
     * @param type 类型（POSAVE-新增，POUPDATE-修改）
     */
    @RequestMapping(value = "uploadTeamInfo", method = RequestMethod.POST)
    @ResponseBody
    public String uploadTeamInfo(Integer projectId, Integer teamId,String teamName, String type) {
        try {
            gmAuthRealNameApi.uploadTeamInfo(projectId, teamId,  teamName,  type);
            return  ResponseUtil.createBussniessJson("上传和修改班组信息成功");
        }catch (Exception e){
            log.error("上传和修改班组信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传和修改班组信息失败");
        }
    }

    /**
     * 上传项目人员信息
     * @param projectId 项目id
     * @param teamId  班组id
     *
     */
    @RequestMapping(value = "uploadRosterInfo", method = RequestMethod.POST)
    @ResponseBody
    public String uploadRosterInfo(Integer projectId, Integer teamId, Integer uerId) {
        try {
             gmAuthRealNameApi.uploadRosterInfo(projectId,teamId,uerId);
            return  ResponseUtil.createBussniessJson("上传项目人员信息成功");
        }catch (Exception e){
            log.error("上传项目人员信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传项目人员信息失败");
        }
    }

    /**
     * 上传人员进退场
     * @param projectId 项目id
     * @param userId 用户id
     * @param teamId 班组id
     */
    @RequestMapping(value = "synBuildPoUserJT", method = RequestMethod.POST)
    @ResponseBody
    public String synBuildPoUserJT(Integer projectId, Integer userId,Integer teamId) {
        try {
            gmAuthRealNameApi.synBuildPoUserJT(projectId,teamId,"",userId);
            return  ResponseUtil.createBussniessJson("上传人员进退场成功");
        }catch (Exception e){
            log.error("上传人员进退场异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传人员进退场失败");
        }
    }

    /**
     * 同步考勤信息
     * @param projectId  项目id
     * @param kaoQinId   考勤id
     */
    @RequestMapping(value = "synWorkerAttendance", method = RequestMethod.POST)
    @ResponseBody
    public String synWorkerAttendance(Integer projectId, Integer kaoQinId) {
        try {
            gmAuthRealNameService.synWorkerAttendance( projectId,  kaoQinId);
            return  ResponseUtil.createBussniessJson("同步考勤信息成功");
        }catch (Exception e){
            log.error("同步考勤信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "同步考勤信息失败");
        }
    }

    /**
     * 根据项目id上传所有的项目相关的信息
     * @param projectId 项目id
     */
    @RequestMapping(value = "synAllInfoByProjectId", method = RequestMethod.POST)
    @ResponseBody
    public String synAllInfoByProjectId(Integer  projectId) {
        try {
            gmAuthRealNameApi.synAllInfoByProjectId( projectId);
            return  ResponseUtil.createBussniessJson("根据项目id上传所有的项目相关的信息成功");
        }catch (Exception e){
            log.error("根据项目id上传所有的项目相关的信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "根据项目id上传所有的项目相关的信息失败");
        }
    }


}
