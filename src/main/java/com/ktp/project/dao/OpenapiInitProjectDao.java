package com.ktp.project.dao;

import com.google.common.collect.Lists;
import com.ktp.project.entity.OpenapiInitProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class OpenapiInitProjectDao {

    @Autowired
    private QueryChannelDao queryChannelDao;

    public OpenapiInitProject queryByProjectId(Integer projectId){
        String hql = " from OpenapiInitProject where projectId = ? ";
        return queryChannelDao.queryOne(hql, Lists.newArrayList(projectId), OpenapiInitProject.class);
    }


}
