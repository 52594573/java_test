package com.ktp.project.service;

import com.ktp.project.dao.ProjectCommonDao;
import com.ktp.project.dto.project.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectCommonService {
    private static final Logger log = LoggerFactory.getLogger(ProjectCommonService.class);

    @Autowired
    private ProjectCommonDao projectCommonDao;

    public ProMessage getProjectMessage(Integer proId){

        return projectCommonDao.getProjectMessage(proId);
    }

    public PoMessage getPojectMessage(Integer proId){

        return projectCommonDao.getPojectMessage(proId);
    }

    public PoUser getPoUser(Integer proId,Integer poId){

        return projectCommonDao.getPoUser(proId,poId);
    }

    public KaoqinUser getClocking(Integer proId,Integer uId){
        String biaoming = "kaoqin"+proId;

        return projectCommonDao.getClocking(biaoming,proId,uId);
    }

    public WageUser getWageUser(Integer proId, Integer poId, Integer uId){

        return projectCommonDao.getWageUser(proId,poId,uId);
    }

}
