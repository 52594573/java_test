package com.ktp.project.service;

import com.google.common.collect.Lists;
import com.ktp.project.constant.CheckManagerEnum;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.examineWarn.CheckManagerInfoDto;
import com.ktp.project.dto.examineWarn.ManagerCountInfoDto;
import com.ktp.project.dto.examineWarn.ProjectManagersDto;
import com.ktp.project.dto.examineWarn.TotalNumDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.NumberUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Transactional
public class CheckManagerService {

    @Autowired
    private QueryChannelDao queryChannelDao;

    public List<CheckManagerInfoDto> queryManagerCheckInfoByMonth(CheckManagerEnum managerEnum) {
        Integer year = NumberUtil.getLastYearNumCurrentTime();
        Integer month = NumberUtil.getLastMonthNumCurrentTime() + 1;
        String sql = "SELECT CONCAT(kc.`key_name`, ui.`u_realname`) AS managerInfo " +
                ", CASE wl.`job_rate` " +
                "WHEN 3 THEN '合格' " +
                "WHEN 4 THEN '不合格' " +
                "END AS checkRate, wl.`rate_score` AS checkScore, wl.user_id AS userId, wl.project_id AS projectId, p.p_name AS projectName " +
                "FROM `work_log_gather` wl " +
                "LEFT JOIN user_info ui ON wl.`user_id` = ui.`id` " +
                "LEFT JOIN key_content kc ON wl.`job_type` = kc.`id` " +
                "LEFT JOIN project p ON p.id = wl.project_id " +
                "WHERE ( %s  and wl.`year` = ? " +
                "AND wl.month = ? ) ";
        String querySql = String.format(sql, managerEnum.getSqlCondition());
        List<CheckManagerInfoDto> checkManagerDtos = null;
        try {
            checkManagerDtos = queryChannelDao.selectManyAndTransformer(querySql, Lists.newArrayList(year, month), CheckManagerInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("通过类型查询管理人员预警信息失败");
        }
        return checkManagerDtos;
    }

    public List<ManagerCountInfoDto> queryManagerCountInfoByMonth() {
        //查询所有不合格的管理人员
        List<CheckManagerInfoDto> checkManagerDtos = queryManagerCheckInfoByMonth(CheckManagerEnum.INFO);
        Map<Integer, Integer> map = queryManagerNumGroupByProjectId();
        Map<Integer, List<Object>> pushMap = queryPushUserIdsGroupByProjectId();
        Map<Integer, ManagerCountInfoDto> projectMap = new HashMap<>();
        for (CheckManagerInfoDto dto : checkManagerDtos) {
            String name = dto.getManagerInfo();
            if (StringUtils.isNotBlank(name)){
                dto.setManagerInfo(name.substring(3, name.length()));
            }
            if (!projectMap.containsKey(dto.getProjectId())) {
                ManagerCountInfoDto value = new ManagerCountInfoDto();
                value.setDate(NumberUtil.formatDate(new Date()));
                value.setFailNum(1);value.setFailCale(map.get(dto.getProjectId()));
                value.setProjectInfo(dto.getProjectName() + "项目考核统计结果：");
                value.setPushIdList(pushMap.get(dto.getProjectId()));
                value.setProjectId(dto.getProjectId());
                List<CheckManagerInfoDto> managerInfos = value.getManagerInfos();
                managerInfos.add(dto);
                projectMap.put(dto.getProjectId(), value);
            } else {
                ManagerCountInfoDto value = projectMap.get(dto.getProjectId());
                value.setFailNum(value.getFailNum() + 1);
                value.getManagerInfos().add(dto);
                projectMap.put(dto.getProjectId(), value);
            }
        }
        return new ArrayList<>(projectMap.values());
    }

    public Map<Integer, Integer> queryManagerNumGroupByProjectId(){
        String sql = "SELECT p.`id` AS tableId, COUNT(1) AS total " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON pop.`po_id` = po.`id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE pop.p_type IN (117, 180, 181)   " +//and p.`is_del` = 0
                "GROUP BY p.`id`";
        List<TotalNumDto> totalNumDtos = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), TotalNumDto.class);
        Map<Integer, Integer> P_M_NUM = new HashMap<>();
        if (!CollectionUtils.isEmpty(totalNumDtos)){
            for (TotalNumDto dto : totalNumDtos) {
                P_M_NUM.put(dto.getTableId(), dto.getTotal().intValue());
            }
        }
        return P_M_NUM;
    }

    public Map<Integer, List<Object>> queryPushUserIdsGroupByProjectId(){
        String sql = "SELECT p.`id` projectId, GROUP_CONCAT(pop.`user_id`,'') pushUserIds " +
                "FROM `pro_organ_per` pop  " +
                "LEFT JOIN `key_content` kc ON pop.`p_type` = kc.`id` " +
                "LEFT JOIN pro_organ po ON pop.`po_id` = po.`id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE kc.`id` IN (120,142) and p.`is_del` = 0 " +
                "GROUP BY p.`id` ";
        List<ProjectManagersDto> dtos = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), ProjectManagersDto.class);
        Map<Integer, List<Object>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(dtos)){
            for (ProjectManagersDto dto : dtos) {
                if (StringUtils.isNotBlank(dto.getPushUserIds()) && dto.getProjectId() != null){
                    String[] split = dto.getPushUserIds().split(",");
                    map.put(dto.getProjectId(), Arrays.asList(split));
                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        String  a = "施工员aaaa";
        String s = a.replaceAll("施工员||质量员||安全员", "");
        String substring = a.substring(3, a.length());
        System.out.println(substring);
        System.out.println(s);
    }
}
