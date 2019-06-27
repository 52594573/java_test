package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthKaoQinCallBackService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.JmAndGsxProAttendanceDto;
import com.ktp.project.dto.AuthRealName.JmAndGsxProjectRequestBody;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;

@Service("jmpAuthRealNameService")
@Transactional
@SuppressWarnings("all")
public class JmpAuthRealNameService extends JmAuthRealNameAbstractService {

//    private static final Logger log = LoggerFactory.getLogger(JmpAuthRealNameService.class);
//
//
//    @Autowired
//    private AuthRealNameLogService logService;
//    @Autowired
//    private QueryChannelDao queryChannelDao;
//    @Autowired
//    private AuthKaoQinCallBackService authKaoQinCallBackService;

    private List<JmAndGsxProAttendanceDto> queryWorkerAttendanceInfo() {
        String sql = "SELECT CASE kq.`k_state` " +
                "WHEN 3 THEN 1 " +
                "WHEN 4 THEN 0 " +
                "END AS type, ui.`u_sfz` AS identityCode, DATE_FORMAT(kq.`k_time`,'%Y-%c-%d %h:%i:%s') AS checkDate, " +
                "kq.k_pic AS faceUrl, ui.`id` AS userId, kq.pro_id projectId, '同步考勤信息' AS other " +
                "FROM `kaoqin174` kq " +
                "LEFT JOIN user_info ui ON kq.`user_id` = ui.`id` " +
                "WHERE kq.k_state IN (3, 4) " +
                "AND k_time BETWEEN '2018-09-01 00:00:00' AND '2018-11-24 00:00:00' ";

        List<Object> params = Lists.newArrayList();
        List<JmAndGsxProAttendanceDto> result = null;
        try {
            result = queryChannelDao.selectManyAndTransformer(sql, params, JmAndGsxProAttendanceDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException("查询考勤信息失败");
        }
        return result;
    }

    public void synWorkerAttendance(Integer projectId, Integer kaoQinId) {
        JmAndGsxProAttendanceDto dto = queryWorkerAttendanceInfoByKaoQinId(projectId, kaoQinId);
        if (dto == null){
            throw new BusinessException(String.format("通过项目ID:%s,考勤ID:%s,查询不到考勤记录!", projectId, kaoQinId));
        }
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(projectId);
        String url = projectEnum.getBaseRequest() + RealNameConfig.JMP_AND_GSX_KAOQIN_URL;
        AuthRealNameLog realNameLog = logService.createBaseBean(projectId, kaoQinId, url, RealNameConfig.KAOQIN);
        try {
            RealNameUtil.checkRequestParams(dto);
            dto.setIdentityCode(DESUtil.DESEncode(dto.getIdentityCode(), projectEnum.getKey()));
//                            dto.setFaceUrl(DESUtil.DESEncode(dto.getFaceUrl()));
            dto.setFaceUrl("");
            JmAndGsxProjectRequestBody body = JmAndGsxProjectRequestBody.newInstance(dto);
            realNameLog.setReqBody(GsonUtil.toJson(body));
            String result = HttpClientUtils.post(url, GsonUtil.toJson(body), "application/json", new HashMap<>());
            RealNameUtil.JMproResIfSussess(result);
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
        return "JM";
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }


    private JmAndGsxProAttendanceDto queryWorkerAttendanceInfoByKaoQinId(Integer projectId, Integer kaoQinId) {
        String sql = "SELECT CASE kq.`k_state` " +
                "WHEN 3 THEN 1 " +
                "WHEN 4 THEN 0 " +
                "END AS type, ui.`u_sfz` AS identityCode, DATE_FORMAT(kq.`k_time`, '%Y-%c-%d %h:%i:%s') AS checkDate " +
                ", kq.k_pic AS faceUrl, ui.`id` AS userId, kq.pro_id AS projectId, '同步考勤信息' AS other " +
                "FROM kaoqin" + projectId + "  kq " +
                "LEFT JOIN user_info ui ON kq.`user_id` = ui.`id` " +
                "WHERE kq.k_state IN (3, 4)  " +
                "AND kq.id = " + kaoQinId;
        JmAndGsxProAttendanceDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(), JmAndGsxProAttendanceDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(String.format("通过项目ID:%s,考勤ID:%s查询考勤信息失败", projectId, kaoQinId));
        }
        return dto;
    }

}

