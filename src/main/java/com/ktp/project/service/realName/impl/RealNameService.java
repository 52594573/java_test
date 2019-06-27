package com.ktp.project.service.realName.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.SdProjectInfoDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.redis.RedisClientTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RealNameService {

    private static final String REAL_NAME_REDIS_KEY = "REAL_NAME_PROJECT_";

    @Autowired
    private QueryChannelDao queryChannelDao;

    public SdProjectInfoDto querySdProjectInfoByProjectId(Integer projectId) {
        SdProjectInfoDto sdProjectInfoDto = null;
        String key = REAL_NAME_REDIS_KEY + projectId;
        String json = RedisClientTemplate.get(key);
        if (StringUtils.isBlank(json)) {
            String sql = "SELECT p.`project_area` AS projectArea, p.`project_code` AS projectCode, p.`project_id` AS projectId, p.`project_name` AS projectName, p.`company_id` AS companyId " +
                    " , c.`company_name` AS companyNme, c.`credit_code` AS creditCode, c.`company_type` AS companyType " +
                    "FROM real_name_project p " +
                    " LEFT JOIN real_name_company c ON p.`company_id` = c.`id` " +
                    "WHERE p.`project_id` = ? ";
            try {
                sdProjectInfoDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(projectId), SdProjectInfoDto.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(String.format("通过项目ID:%s,查询顺德项目信息失败", projectId));
            }
            RedisClientTemplate.set(key, GsonUtil.toJson(sdProjectInfoDto));
        } else {
            sdProjectInfoDto = JSONObject.parseObject(json, SdProjectInfoDto.class);
        }
        return sdProjectInfoDto;
    }
}
