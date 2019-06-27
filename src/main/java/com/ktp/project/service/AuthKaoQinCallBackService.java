package com.ktp.project.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.dao.ModifyChannelDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.entity.AuthKaoQinCallBack;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.DESUtil;
import com.zm.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Transactional
public class AuthKaoQinCallBackService {
    private static Logger log = LoggerFactory.getLogger(AuthKaoQinCallBackService.class);
    @Autowired
    private ModifyChannelDao modifyChannelDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private AuthRealNameLogService logService;
    @Autowired
    private QueryChannelDao queryChannelDao;

    public Map<String, Integer> saveOrUpdate(AuthKaoQinCallBack callBack){
        try {
            ProjectEnum projectEnum = caseItemName(callBack.getItemName());
            callBack.setIdentityCode(DESUtil.DESDecrypt(callBack.getIdentityCode(), projectEnum.getKey()));
            UserInfo userInfo = userInfoDao.getUserInfoByCardId(callBack.getIdentityCode());
            if (userInfo == null){
                throw new BusinessException(String.format("通过身份证号码:%s,查询不到结果", callBack.getIdentityCode()));
            }
            Integer userId = userInfo.getUser_id();
            String url = RealNameConfig.JMP_AND_GSX_KAOQIN_URL;
            AuthRealNameLog log = logService.queryOneByUserIdAndUrl(userId, url);
            if (log == null){
                throw new BusinessException(String.format("通过用户ID:%s, 请求路径:%s,查询AuthRealNameLog失败", userId, url));
            }
            callBack.setLogId(log.getId());
            callBack.setCreateTime(new Date());
            modifyChannelDao.saveOrUpdate(callBack);
            return ImmutableMap.of("rstCode",0);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ImmutableMap.of("rstCode",-1);
        }
    }

    private ProjectEnum caseItemName(String itemName){
        log.error("接收参数: " + itemName);
        String sql = "select id from project where p_name like ? ";
        Integer pId = (Integer)queryChannelDao.selectOne(sql, Lists.newArrayList("%" + itemName + "%"));
        if (pId == null){
            throw new BusinessException(String.format("根据项目名:%s查询不到结果", itemName));
        }
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(pId);
        if (projectEnum == null){
            throw new BusinessException(String.format("根据项目名:%s查询出来的结果与需求不匹配", itemName));
        }
        return projectEnum;
    }

}
