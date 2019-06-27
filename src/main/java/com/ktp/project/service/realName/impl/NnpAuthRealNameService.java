package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.constant.*;
import com.ktp.project.entity.AuthRealName;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.JoinWorkerInfoDto;
import com.ktp.project.dto.AuthRealName.OutWorkerDto;
import com.ktp.project.dto.AuthRealName.WorkAttendanceDto;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.service.AuthRealNameService;
import com.ktp.project.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("nnpAuthRealNameService")
@Transactional
public class NnpAuthRealNameService implements AuthRealNameApi {

    private static Logger log = LoggerFactory.getLogger(NnpAuthRealNameService.class);

    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private AuthRealNameLogService logService;
    @Autowired
    private AuthRealNameService authRealNameService;

    private String synLogin() {
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("username", RealNameConfig.LOGIN_USER_NAME);
        paranms.put("password", RealNameConfig.LOGIN_PASSWORD);
        String result = HttpClientUtils.post(RealNameConfig.SYN_LOGIN_URL, GsonUtil.toJson(paranms), "application/json", new HashMap<>());
        JSONObject jsonObject = JSONObject.fromObject(result);
        String code = String.valueOf(jsonObject.get("code"));
        if ("200".equals(code)) {
            JSONObject data = JSONObject.fromObject(String.valueOf(jsonObject.get("d")));
            return String.valueOf(data.get("token"));
        } else {
            throw new BusinessException("登录到实名制系统失败");
        }
    }


    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {
        AuthRealNameLog authRealNameLog = logService.createBaseBean(projectId, unknownId, realNameEnum.getRemark());
        String url = null;
        String reqBody = null;
        try {
            switch (realNameEnum) {
                case WORKER_JOIN:
                    url = RealNameConfig.SYN_JOIN_WORKER_INFO_URL;
                    JoinWorkerInfoDto join = queryJoinWorkerInfo(projectId, unknownId);
                    reqBody = GsonUtil.toJson(join);
                    break;
                case WORKER_OUT:
                    url = RealNameConfig.SYN_LEAVE_PROJECT_WORKER_URL;
                    OutWorkerDto out = queryLeaveProjectWorker(unknownId);
                    reqBody = GsonUtil.toJson(out);
                    break;
                default:
                    break;
            }
            authRealNameSendPost(url, reqBody);
        } catch (Exception e) {
            e.printStackTrace();
            authRealNameLog.setIsSuccess(0);
            authRealNameLog.setResMsg(e.getMessage());
            log.error(e.getMessage());
        }finally {
            authRealNameLog.setReqUrl(url);
            authRealNameLog.setReqBody(reqBody);
            logService.saveOrUpdate(authRealNameLog);
        }
    }

    @Override
    public void synWorkerAttendance(Integer projectId, Integer kaoQinId) {
        WorkAttendanceDto dto = queryKaoQinInfoById(projectId, kaoQinId);
        if (dto == null){
            throw new BusinessException(String.format("通过项目ID:%s,考勤ID:%s,查询不到考勤记录!", projectId, kaoQinId));
        }
        dto = setQueryWorkAttendanceDto(dto);
        String url = RealNameConfig.SYN_JOIN_WORKER_ATTENDANCE_URL;
        Integer userId = dto.getUserId();
        AuthRealNameLog realNameLog = logService.createBaseBean(projectId, userId, url, RealNameConfig.KAOQIN);
        try {
            realNameLog.setReqBody(GsonUtil.toJson(dto));
            authRealNameSendPost(url, GsonUtil.toJson(dto));
        } catch (Exception e) {
            e.printStackTrace();
            realNameLog.setIsSuccess(0);
            realNameLog.setResMsg(e.getMessage());
            log.error(e.getMessage());
        }
        logService.saveOrUpdate(realNameLog);
    }

    @Override
    public void synBuildPo(Integer projectId, Integer userId, String type) {

    }

    @Override
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type,Integer teamId) {

    }

    @Override
    public String getpSent() {
        return "NN";
    }
    
    private WorkAttendanceDto queryKaoQinInfoById(Integer projectId, Integer kaoQinId){
        String sql = "SELECT CASE kq.`k_state`  " +
                    "WHEN 1 THEN 1  " +
                    "WHEN 2 THEN 2  " +
                    "WHEN 3 THEN 1  " +
                    "WHEN 4 THEN 2  " +
                    "WHEN 5 THEN 1  " +
                    "WHEN 6 THEN 2  " +
                    "END AS status, ui.`u_sfz` AS idNumber, kq.k_pic AS pic, ui.`id` userId, kq.pro_id projectId,  " +
                    "DATE_FORMAT(kq.`k_time`,'%Y-%m-%d %h:%i:%s') AS time  " +
                    "FROM kaoqin"+ projectId +" kq  " +
                    "LEFT JOIN user_info ui ON kq.`user_id` = ui.`id`  " +
                    "WHERE kq.id = ?  group by kq.id ";
        WorkAttendanceDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(kaoQinId), WorkAttendanceDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过考勤ID:%s,查询考勤信息失败,nnp", kaoQinId));
        }
        return dto;
    }

    private WorkAttendanceDto setQueryWorkAttendanceDto(WorkAttendanceDto attendanceDto) {
        attendanceDto.setPic(Base64Util.compressBase64(attendanceDto.getPic(), ImgRatioEnum.FACE_PHOTO));
        attendanceDto.setSessionToken(synLogin());
        attendanceDto.setUserId(null);
        attendanceDto.setProjectId(null);
        return attendanceDto;
    }

    private List<WorkAttendanceDto> queryWorkerAttendanceInfo() {
        String sql = "SELECT CASE kq.`k_state` " +
                "WHEN 1 THEN 1 " +
                "WHEN 2 THEN 2 " +
                "WHEN 3 THEN 1 " +
                "WHEN 4 THEN 2 " +
                "WHEN 5 THEN 1 " +
                "WHEN 6 THEN 2 " +
                "END AS status, ui.`u_sfz` AS idNumber, kq.k_pic AS pic, ui.`id` userId, kq.pro_id projectId, " +
                "DATE_FORMAT(kq.`k_time`,'%Y-%c-%d %h:%i:%s') AS time " +
                "FROM `kaoqin54` kq " +
                "LEFT JOIN user_info ui ON kq.`user_id` = ui.`id` " +
                "WHERE k_time BETWEEN ? AND ? limit 10 ";
        List<Object> params = Lists.newArrayList(NumberUtil.getLastDayCurrentTime(), NumberUtil.getToDayCurrentTime());
        return queryChannelDao.selectManyAndTransformer(sql, params, WorkAttendanceDto.class);
    }


    private List<AuthRealName> findAllUserIsNotSynJoin() {
        String sql = "SELECT DISTINCT  pop.`user_id` userId, p.`id` projectId " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE p.`id` = 54  and po.po_gzid !=0 " +//and pop.pop_state= 0
                "AND pop.`user_id` NOT IN ( " +
                "SELECT a.`user_id` " +
                "FROM auth_real_name a where a.is_syn_join = 1 " +
                ") ";
        List<AuthRealName> authRealNames = null;
        try {
            authRealNames = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), AuthRealName.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException("findAllUserIsNotSyn方法查询失败");
        }
        return authRealNames;
    }

    public JoinWorkerInfoDto queryJoinWorkerInfo(Integer projectId, Integer userId) {
        String sql = "SELECT ui.u_realname AS name, ui.u_sfz AS idNumber, po.`po_name` AS groupName, po.`po_gzid` AS job, pop.`pop_type` AS role " +
                ", us.`us_pic` AS photo, ui.`u_cert_pic` AS facePhoto, ui.u_similarity AS faceSimilarity, ui.`u_name` AS phone " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "LEFT JOIN user_info ui ON pop.`user_id` = ui.`id` " +
                "LEFT JOIN user_sfz us ON ui.`id` = us.`u_id` " +
                "WHERE po.po_gzid != 0 and p.`id` = ? AND pop.`user_id` = ? ";
        JoinWorkerInfoDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(projectId, userId), JoinWorkerInfoDto.class);
            dto.setFacePhoto(Base64Util.compressBase64(dto.getFacePhoto(), ImgRatioEnum.FACE_PHOTO));
            dto.setPhoto(Base64Util.compressBase64(dto.getFacePhoto(), ImgRatioEnum.IDCARD_PHOTO));
            dto.setRole(EnumMap.getValueByKey("ROLE_MAP", dto.getRole()));
            dto.setJob(EnumMap.getValueByKey("GZ_MAP", dto.getJob()));
            dto.setFaceSimilarity(dto.getFaceSimilarity() == null ? new BigDecimal("0.0") : dto.getFaceSimilarity());
            dto.setSessionToken(synLogin());
            RealNameUtil.checkRequestParams(dto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(e.getMessage());
        }
        return dto;

    }

    private String authRealNameSendPost(String url, String data) {
        String result = null;
        try {
            result = HttpClientUtils.post(url, data, "application/json", new HashMap<>());
            log.info("远程调用实名系统url {}, 参数 {}, 返回结果 {}", url, data, GsonUtil.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("远程调用实名系统失败: url %s, 报错信息 %s ", url, e.getMessage()));
        }
        return RealNameUtil.NNproResultSet(result);
    }

    private List<AuthRealName> findAllUserIsNotSynOut() {
        String sql = "SELECT DISTINCT  pop.`user_id` userId, p.`id` projectId " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE p.`id` = 54 and pop.pop_state= 4 " +
                "AND pop.`user_id` NOT IN ( " +
                "SELECT a.`user_id` " +
                "FROM auth_real_name a where a.is_syn_out = 1 " +
                ") ";
        List<AuthRealName> authRealNames = null;
        try {
            authRealNames = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), AuthRealName.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new BusinessException("findAllUserIsNotSyn方法查询失败");
        }
        return authRealNames;
    }

    private OutWorkerDto queryLeaveProjectWorker(Integer userId) {
        String sql = "select u_sfz idNumber from user_info where id = ? ";
        OutWorkerDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(userId), OutWorkerDto.class);
            dto.setSessionToken(synLogin());
            RealNameUtil.checkRequestParams(dto);
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("查询离场人员信息失败!");
        }
        return dto;
    }

}
