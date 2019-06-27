package com.ktp.project.logic.schedule;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ktp.project.constant.CheckManagerEnum;
import com.ktp.project.dao.RiskWarningDao;
import com.ktp.project.dto.examineWarn.CheckManagerInfoDto;
import com.ktp.project.dto.examineWarn.ManagerCountInfoDto;
import com.ktp.project.entity.MassageSwitch;
import com.ktp.project.service.CheckManagerService;
import com.ktp.project.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 管理人员考核预警定时推送
 */
@Component
public class CheckManagerTask {

    private static Logger log = LoggerFactory.getLogger(CheckManagerTask.class);

    @Autowired
    private CheckManagerService checkManagerService;
    @Autowired
    private RiskWarningDao riskWarningDao;

    private static final String baseFromId = "project-";
    private static final Integer mAppId = 1;//建信开太平
    private static final Integer mTypeId = 4;//管理人员考核预警信息


    /**
     * 每月1号9点推送管理人员考核预警信息
     */
    @Scheduled(cron = "0 0 9 1 * ?")
    public void checkManagerInfo() {
         log.info("推送管理人员考核统计预警信息, 开始时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
        String yyyy_mm_dd = NumberUtil.formatDate(new Date());
        CheckManagerEnum[] values = CheckManagerEnum.values();
        //查询所有关闭的用户
        Map<Integer, List<Integer>> map = riskWarningDao.mappingUserIdWithStatus(mTypeId, mAppId);
        for (CheckManagerEnum value : values) {
            List<CheckManagerInfoDto> dtos = checkManagerService.queryManagerCheckInfoByMonth(value);
            if (!CollectionUtils.isEmpty(dtos)){
                for (CheckManagerInfoDto dto : dtos) {
                    Integer projectId = dto.getProjectId();
                    Integer userId = dto.getUserId();
                    dto.setDate(yyyy_mm_dd);
//                    dto.setUserId(47211);
                    String projectName = dto.getProjectName();
                    String fromUserId = baseFromId + dto.getProjectId();
                    Map<String, Object> extMap = createBaseMap(dto, 1, projectName);
                    if (map.containsKey(projectId) && map.get(projectId).contains(userId)){
                        HuanXinRequestUtils.sendMessage(fromUserId,Lists.newArrayList(userId),extMap,HuanXinRequestUtils.DEFAULT_MSG,true);
                    }else {
                        HuanXinRequestUtils.sendMessage(fromUserId, Lists.newArrayList(userId), extMap, HuanXinRequestUtils.DEFAULT_MSG);
                    }
                }
            }
        }
        log.info("推送管理人员考核统计预警信息, 结束时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
    }

    /**
     * 每月1号9点推送管理人员考核统计预警信息
     */
    @Scheduled(cron = "0 0 9 1 * ?")
    public void checkManagerCountInfo() {
        log.info("推送管理人员考核统计预警信息, 开始时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
        String yyyy_mm_dd = NumberUtil.formatDate(new Date());
        Map<Integer, List<Integer>> map = riskWarningDao.mappingUserIdWithStatus(mTypeId, mAppId);
        List<ManagerCountInfoDto> countInfoDtos = checkManagerService.queryManagerCountInfoByMonth();
//        ArrayList<Object> list = Lists.newArrayList(29009);
        for (ManagerCountInfoDto dto : countInfoDtos) {
            Integer projectId = dto.getProjectId();
            List<Object> pushIdList = dto.getPushIdList();

            Double flag = (dto.getFailNum().doubleValue() / dto.getFailCale());
            flag = NumberUtil.keepTwoDecimalDou(flag) * 100;
            dto.setFailCale( flag.intValue() );
            if (dto.getFailCale() >= 30) {
                dto.setDate(yyyy_mm_dd);
//                dto.setPushIdList(list);
                String fromUserId = baseFromId + dto.getProjectId();
                String projectName = dto.getProjectInfo().replaceAll("项目考核统计结果：", "");
                for (Object o : pushIdList) {
                    int userId = Integer.parseInt(String.valueOf(o));
                    if (map.containsKey(projectId) && map.get(projectId).contains(userId)) {
                        pushIdList.remove(o);
                        HuanXinRequestUtils.sendMessage(fromUserId, Lists.newArrayList(userId), createBaseMap(dto, 2, projectName), HuanXinRequestUtils.DEFAULT_MSG, true);
                    }
                }
                if (CollectionUtils.isEmpty(pushIdList)) {
                    continue;
                }
                HuanXinRequestUtils.sendMessage(fromUserId, pushIdList, createBaseMap(dto, 2, projectName), HuanXinRequestUtils.DEFAULT_MSG);
            }
        }
        log.info("推送管理人员考核统计预警信息, 结束时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
    }

    private Map<String, Object> createBaseMap(Object obj, Integer type, String projectName){
        Map<String, Object> ext = Maps.newHashMap();
        if (type.equals(2)){
            ext.put("type","projectWarning_manage_count");
        }else {
            ext.put("type","projectWarning_manage");
        }
        ext.put("myUserName", projectName);
        ext.put("data",obj);
        return ext;
    }
}
