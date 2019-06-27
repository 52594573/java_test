package com.ktp.project.service;

import com.google.common.collect.Lists;
import com.ktp.project.dao.ModifyChannelDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.entity.AuthRealName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class AuthRealNameService {
    
    @Autowired
    private ModifyChannelDao modifyChannelDao;
    @Autowired
    private QueryChannelDao queryChannelDao;
    
    public void saveOrUpdate(AuthRealName entiry){
        AuthRealName bean = findOneByUserId(entiry.getUserId());
        if (bean == null){
            entiry.setCreateTime(new Date());
            if (entiry.getId() != null){
                entiry.setUpdateTime(new Date());
            }
            modifyChannelDao.save(entiry);
        }else {
//            modifyChannelDao.evict(bean);
            bean.setIsSynOut(entiry.getIsSynOut());
            bean.setUpdateTime(new Date());
            modifyChannelDao.update(bean);
        }
//        modifyChannelDao.saveOrUpdate(entiry);
    }
    
    public AuthRealName findOneByUserId(Integer userId){
        String hql = "from AuthRealName where userId = ?";
        return queryChannelDao.queryOne(hql, Lists.newArrayList(userId), AuthRealName.class);
    }

    public AuthRealName createBaseBean(Integer projectId, Integer userId){
        AuthRealName authRealName = new AuthRealName();
        authRealName.setUpdateTime(new Date());
        authRealName.setCreateTime(new Date());
        authRealName.setProjectId(projectId);
        authRealName.setUserId(userId);
        return authRealName;
    }

}
