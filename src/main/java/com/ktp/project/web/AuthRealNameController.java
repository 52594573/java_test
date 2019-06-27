package com.ktp.project.web;


import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.dao.ProOrganPerDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dao.RiskWarningDao;
import com.ktp.project.dto.AuthRealName.SynKaoQinIfFalseDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.CheckManagerService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.impl.GZProjectService;
import com.ktp.project.service.realName.impl.JmpAuthRealNameService;
import com.ktp.project.service.realName.impl.SdAuthRealNameService;
import com.ktp.project.service.realName.impl.ZhzhAuthRealNameService;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.ThreadPoolManager;
import com.ktp.project.util.redis.RedisClientTemplate;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganDAO;
import com.zm.entity.ProOrganPer;
import javafx.scene.input.InputMethodTextRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping(value = "api/authRealName", produces = "application/json;charset=UTF-8;")
@SuppressWarnings("all")
public class AuthRealNameController {

    private static final Logger log = LoggerFactory.getLogger(AuthRealNameController.class);

    @Autowired
    private CheckManagerService checkManagerService;
    @Autowired
    private RiskWarningDao riskWarningDao;
    @Autowired
    private GZProjectService gzProjectService;
    @Autowired
    private QueryChannelDao queryChannelDao;

    private static final String baseFromId = "project-";
    private static final Integer mAppId = 1;//建信开太平
    private static final Integer mTypeId = 4;//管理人员考核预警信息

    @Resource(name="sdAuthRealNameService")
    private AuthRealNameApi authRealNameApi;

    @Autowired
    private JmpAuthRealNameService jmService;

    @Autowired
    private SdAuthRealNameService adService;

    @Autowired
    private ProOrganDAO proOrganDAO;

    @Autowired
    private ProOrganPerDao proOrganPerDao;
    @Autowired
    private ZhzhAuthRealNameService authRealNameService;

    @RequestMapping(value = "synJoinWorkerInfo", method = RequestMethod.GET)
        public String synJoinWorkerInfo(@RequestParam("projectId") Integer projectId, @RequestParam("userId") Integer userId){
        try {
//            synAuthRealNameService.synJoinWorkerInfo(projectId, userId);
//            String token = synAuthRealNameService.synLogin();
//            return ResponseUtil.createNormalJson(token);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "synJoinWorkerAttendance", method = RequestMethod.GET)
    public String synJoinWorkerAttendance(@RequestParam("projectId") Integer projectId, @RequestParam("userId") Integer userId){
        try {
//            synAuthRealNameService.synJoinWorkerAttendance();
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "synLeaveProjectWorker", method = RequestMethod.GET)
    public String synLeaveProjectWorker(@RequestParam("userId") Integer userId){
        try {
//            synAuthRealNameService.synLeaveWorkerInfo();
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "synKaoQinWorker", method = RequestMethod.GET)
    public String synKaoQinWorker(@RequestParam("projectId") Integer projectId,
                                  @RequestParam("kaoQinId") Integer kaoQinId){
        try {
//            AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(projectId);
            authRealNameService.synWorkerAttendance(projectId, kaoQinId);
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "synBuildPo", method = RequestMethod.GET)
    public String synBuildPo(Integer poId, Integer userId,String type){
        try {
            ProOrgan byId = proOrganDAO.findById(poId);
            if (byId == null){
                throw new BusinessException(String.format("通过班组ID:%s查不到结果", poId));
            }
            AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(byId.getProId());
            authRealNameApi.synBuildPo(poId, userId, type);
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    /**
     * 进退场
     * @param poId
     * @param userId
     * @param type
     * @return
     */
    @RequestMapping(value = "synBuildPoUserJT", method = RequestMethod.GET)
    public String synBuildPoUserJT(Integer pId, Integer userId, String type, Integer poId){
        try {
            AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(pId);
            authRealNameApi.synBuildPoUserJT(pId, userId, type, poId);
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    /**
     * 同步人脸识别图片
     * @param poId
     * @param userId
     * @param type
     * @return
     */
    @RequestMapping(value = "saveOrUpdateFaceAcquisition", method = RequestMethod.GET)
    public String saveOrUpdateFaceAcquisition(Integer userId){
        try {
            adService.saveOrUpdateFaceAcquisition(userId);
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test(@RequestParam("projectId") Integer projectId){
        try {
            //gmAuthRealNameService.uploadTeamInfo(63, 987,  "照明",  "POSAVE");
            return ResponseUtil.createNormalJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createApiErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "synFalseWorkerInfo", method = RequestMethod.GET)
    public String synFalseWorkerInfo(){
        //查询当天同步考勤失败的用户
        List<SynKaoQinIfFalseDto> dtos = gzProjectService.synKaoQinIfFalse(60);
        for (SynKaoQinIfFalseDto dto : dtos) {
            Integer projectId = dto.getProjectId();
            Integer kaoQinId = dto.getKaoQinId();
            Integer poId = dto.getPoId();
            Integer userId = dto.getUserId();
            if (poId.equals(715) ){
                System.out.println("项目ID: "  + projectId + "班组ID: " + poId + "用户ID: " +userId + "考勤ID: " + kaoQinId);
            gzProjectService.synWorkerAttendance(projectId, kaoQinId);
//                continue;
            }
        }

//        gzProjectService.synBuildPo(715, 27954, "POUSERSAV");

        return "成功";
    }

    /**
     * 实名系统同步班组旧数据
     * @param projectId
     * @return
     */
    @RequestMapping(value = "synProOrganList", method = RequestMethod.GET)
    public String synProOrganList(Integer projectId){
        List<ProOrgan> byProId = proOrganDAO.findByProId(projectId);
        for (ProOrgan proOrgan : byProId) {
//            if (proOrgan.getPoState().equals(1)){
//                continue;
//            }
            synBuildPo(proOrgan.getId(), proOrgan.getId(), "POSAVE");
//            synProOrganPerList(proOrgan.getId());
        }
        return ResponseUtil.createNormalJson("成功");
    }

    @RequestMapping(value = "synProOrganPerList", method = RequestMethod.GET)
    public String synProOrganPerList(Integer poId){
        List<ProOrganPer> list = proOrganPerDao.listProOrganPerByPoId(poId);
        for (ProOrganPer proOrganPer : list) {
            synBuildPo(poId, proOrganPer.getUserId(), "POUSERSAV");
        }
        return ResponseUtil.createNormalJson("成功");
    }

    @RequestMapping(value = "synKaoQinList", method = RequestMethod.GET)
    public String synKaoQinList(Integer pId){
        List<Integer> integers = queryKaoQinIds(pId);
        for (Integer kaoQinId : integers) {
            synKaoQinWorker(pId, kaoQinId);
        }
        return ResponseUtil.createNormalJson("成功");
    }

    private List<Integer> queryKaoQinIds(Integer pId){
        String sql = "select id from kaoqin" + pId + " order by id desc limit 10";
       return queryChannelDao.selectMany(sql, Lists.newArrayList());
    }

    @RequestMapping(value = "synBankCard", method = RequestMethod.GET)
    public String synBankCard(@RequestParam("pId") Integer pId, @RequestParam(value = "uId", defaultValue = "-1")Integer uId){
        try {
            jmService.savePopBankCard(pId, uId);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
        return ResponseUtil.createNormalJson("成功");

    }

    @RequestMapping(value = "synSalaryRecord", method = RequestMethod.GET)
    public String synSalaryRecord(@RequestParam("poId") Integer poId, @RequestParam(value = "uId", defaultValue = "-1")Integer uId){
        try {
            jmService.synSalaryRecord(poId, uId);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
        return ResponseUtil.createNormalJson("成功");

    }

    @RequestMapping(value = "synFace", method = RequestMethod.GET)
    public String synFace(Integer projectId){
//        authRealNameService.CaijiAll(projectId);
        authRealNameService.uploadWorkerFeature(48122, "");
        return ResponseUtil.createNormalJson("成功");
    }

}



