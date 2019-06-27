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
 * 中山项目接口
 *
 * @author lsh
 * @date 2019年1月12日10:43:18
 */
@Controller
@RequestMapping(value = "api/zsService", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class ZsServiceController {
    private static final Logger log = LoggerFactory.getLogger(ZsServiceController.class);

    @Resource(name = "zsAuthRealNameService")
    private AuthRealNameApi zsAuthRealNameService;
    @Autowired
    private com.ktp.project.service.realName.ZsAuthRealNameApi zsAuthRealNameApi;

    /**
     * 获取数据字典
     *
     * @param type -数据类型
     * @return list  符合条件的list
     */
    @RequestMapping(value = "getBaseDataDictionary", method = RequestMethod.POST)
    @ResponseBody
    public String getBaseDataDictionary(int type) {
        try {
            // List baseDataDictionaryList =  zsAuthRealNameApi.getBaseDataDictionary(type);
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            return ResponseUtil.createApiErrorJson(999, "失败");
        }
    }

    /**
     * 同步企业信息
     *
     * @param corpCode     企业code
     * @param corpName     企业名称
     * @param areaCode     企业所在地区code
     * @param registerDate 注册日期  YYYY-MM-DD
     */
    @RequestMapping(value = "synCompanyInfo", method = RequestMethod.POST)
    @ResponseBody
    public String synCompanyInfo(String corpCode, String corpName, String areaCode, String registerDate) {
        try {
            zsAuthRealNameApi.synCompanyInfo(corpCode, corpName, areaCode, registerDate);
            return ResponseUtil.createBussniessJson("同步企业成功");
        } catch (Exception e) {
            log.error("同步企业异常", e);
            return ResponseUtil.createApiErrorJson(999, "同步企业失败");
        }
    }

    /**
     * 上传项目信息
     *
     * @param projectId 项目id
     */
    @RequestMapping(value = "synWorkinfo", method = RequestMethod.POST)
    @ResponseBody
    public String synWorkinfo(Integer projectId) {
        try {
            zsAuthRealNameApi.synWorkinfo(projectId);
            return ResponseUtil.createBussniessJson("上传项目信息成功");
        } catch (Exception e) {
            log.error("上传项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传项目失败");
        }
    }

    /**
     * 修改上传项目信息
     *
     * @param projectId 项目id
     */
    @RequestMapping(value = "sgsynWorkinfo", method = RequestMethod.POST)
    @ResponseBody
    public String sgsynWorkinfo(Integer projectId) {
        try {
            zsAuthRealNameApi.sgsynWorkinfo(projectId);
            return ResponseUtil.createBussniessJson("上传项目信息成功");
        } catch (Exception e) {
            log.error("上传项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传项目失败");
        }
    }

    /*
     *查询项目信息成功
     */
    @RequestMapping(value = "projectquery", method = RequestMethod.GET)
    @ResponseBody
    public String projectquery(String contractorCorpCode) {
        try {
            zsAuthRealNameApi.projectquery(contractorCorpCode);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }

    /*
     *查询班组信息
     */
    @RequestMapping(value = "poquery", method = RequestMethod.GET)
    @ResponseBody
    public String poquery(String projectCode,Integer pageIndex,Integer pageSize) {
        try {
            zsAuthRealNameApi.poquery(projectCode,pageIndex,pageSize);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }

    /*
     *查询人员信息
     */
    @RequestMapping(value = "queryuser", method = RequestMethod.GET)
    @ResponseBody
    public String queryuser(String projectCode) {
        try {
            zsAuthRealNameApi.queryuser(projectCode);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }


/**
 *查询考情
 */
    @RequestMapping(value = "workercontract", method = RequestMethod.GET)
    @ResponseBody
    public String workercontract(String projectCode,Integer pageIndex,Integer pageSize,String teamSysNo,String date) {
        try {
            zsAuthRealNameApi.workercontract(projectCode,pageIndex,pageSize,teamSysNo,date);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }
/**
 *批量考勤
 */
    @RequestMapping(value = "kaoqingAll", method = RequestMethod.POST)
    @ResponseBody
    public String kaoqingAll(Integer projectId) {
        try {
            zsAuthRealNameApi.kaoqingAll(projectId);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }



    /*
     *查询参建信息
     */
    @RequestMapping(value = "querycanjian", method = RequestMethod.GET)
    @ResponseBody
    public String querycanjian(String projectCode) {
        try {
            zsAuthRealNameApi.querycanjian(projectCode);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }

    /**
     * 查询上传企业信息
     */
    @RequestMapping(value = "corp", method = RequestMethod.GET)
    @ResponseBody
    public String corp(String corpCode) {
        try {
            zsAuthRealNameApi.corpQuery(corpCode);
            return ResponseUtil.createBussniessJson("查询上传企业信息成功");
        } catch (Exception e) {
            log.error("查询上传企业信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询上传企业信息失败");
        }
    }

    /**
     * 上传和修改班组信息
     *
     * @param projectId 项目id
     * @param teamId    班组id
     * @param type      类型（POSAVE-新增，POUPDATE-修改）
     */
    @RequestMapping(value = "uploadTeamInfo", method = RequestMethod.POST)
    @ResponseBody
    public String uploadTeamInfo(Integer projectId, Integer teamId, String teamName, String type) {
        try {
            zsAuthRealNameApi.uploadTeamInfo(projectId, teamId, teamName, type);
            return ResponseUtil.createBussniessJson("上传和修改班组信息成功");
        } catch (Exception e) {
            log.error("上传和修改班组信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传和修改班组信息失败");
        }
    }

    /**
    *
    * @Description: 通过项目id批量上传班组
    * @Author: liaosh
    * @Date: 2019/1/15 0015
    */
    @RequestMapping(value = "uploadTeamAll", method = RequestMethod.POST)
    @ResponseBody
    public String uploadTeamAll(Integer projectId) {
        try {
            zsAuthRealNameApi.uploadTeamAll(projectId);
            return ResponseUtil.createBussniessJson("通过项目id批量上传班组成功");
        } catch (Exception e) {
            log.error("通过项目id批量上传班组信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "通过项目id批量上传班组信息失败");
        }
    }

    /**
     *
     * @Description: 通过项目id批量上传整个项目工人
     * @Author: liaosh
     * @Date: 2019/1/15 0015
     */
    @RequestMapping(value = "uploadProjectUserAll", method = RequestMethod.POST)
    @ResponseBody
    public String uploadProjectUserAll(Integer projectId) {
        try {
            zsAuthRealNameApi.uploadProjectUserAll(projectId);
            return ResponseUtil.createBussniessJson("通过项目id批量上传整个项目工人成功");
        } catch (Exception e) {
            log.error("通过项目id批量上传整个项目工人信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "通过项目id批量上传整个项目工人信息失败");
        }
    }


    /**
     * Doing=================================
     * <p>
     * 上传项目人员信息
     *
     * @param projectId 项目id
     * @param teamId    班组id
     */
    @RequestMapping(value = "uploadRosterInfo", method = RequestMethod.POST)
    @ResponseBody
    public String uploadRosterInfo(Integer projectId, Integer teamId, Integer uerId) {
        try {
            zsAuthRealNameApi.uploadRosterInfo(projectId, teamId, uerId);
            return ResponseUtil.createBussniessJson("上传项目人员信息成功");
        } catch (Exception e) {
            log.error("上传项目人员信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传项目人员信息失败");
        }
    }

    /**
     * 上传人员进退场
     *
     * @param projectId 项目id
     * @param userId    用户id
     * @param teamId    班组id
     */
    @RequestMapping(value = "synBuildPoUserJT", method = RequestMethod.POST)
    @ResponseBody
    public String synBuildPoUserJT(Integer projectId, Integer userId, Integer teamId) {
        try {
            zsAuthRealNameApi.synBuildPoUserJT(projectId, teamId, "", userId);
            return ResponseUtil.createBussniessJson("上传人员进退场成功");
        } catch (Exception e) {
            log.error("上传人员进退场异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传人员进退场失败");
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
            zsAuthRealNameService.synWorkerAttendance(projectId, kaoQinId);
            return ResponseUtil.createBussniessJson("同步考勤信息成功");
        } catch (Exception e) {
            log.error("同步考勤信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "同步考勤信息失败");
        }
    }

    /**
    *
    * @Description: 查询考勤信息
    * @Author: liaosh
    * @Date: 2019/1/19 0019
    */
    @RequestMapping(value = "querysynWorkerAttendance", method = RequestMethod.GET)
    @ResponseBody
    public String querysynWorkerAttendance(String projectCode,Integer pageIndex,Integer pageSize,String date) {
        try {
            zsAuthRealNameApi.querysynWorkerAttendance(projectCode,pageIndex,pageSize,date);
            return ResponseUtil.createBussniessJson("查询项目信息成功");
        } catch (Exception e) {
            log.error("查询项目异常", e);
            return ResponseUtil.createApiErrorJson(999, "查询项目失败");
        }
    }


    /**
     * 根据项目id上传所有的项目相关的信息
     *
     * @param projectId 项目id
     */
    @RequestMapping(value = "synAllInfoByProjectId", method = RequestMethod.POST)
    @ResponseBody
    public String synAllInfoByProjectId(Integer projectId) {
        try {
            zsAuthRealNameApi.synAllInfoByProjectId(projectId);
            return ResponseUtil.createBussniessJson("根据项目id上传所有的项目相关的信息成功");
        } catch (Exception e) {
            log.error("根据项目id上传所有的项目相关的信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "根据项目id上传所有的项目相关的信息失败");
        }
    }

    /**
     * @Description: 上传参建单位
     * @Author: liaosh
     * @Date: 2019/1/12 0012
     */
    @RequestMapping(value = "uploadParticipateInfo", method = RequestMethod.POST)
    @ResponseBody
    public String uploadParticipateInfo(Integer projectId) {
        try {
            zsAuthRealNameApi.uploadParticipateInfo(projectId);
            return ResponseUtil.createBussniessJson("上传参建单位信息成功");
        } catch (Exception e) {
            log.error("上传参建单位信息异常", e);
            return ResponseUtil.createApiErrorJson(999, "上传参建单位信息失败");
        }
    }

}
