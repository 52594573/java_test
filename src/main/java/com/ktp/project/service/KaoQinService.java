package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import com.ktp.project.entity.KaoQinLocation;
import com.ktp.project.entity.WorkLogGather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author djcken
 * @date 2018/6/20
 */
@Service
public class KaoQinService {

    @Autowired
    private DataBaseDao dataBaseDao;

    public KaoQinLocation queryKaoQinLocation(int userId, int proId, String time) {
        return dataBaseDao.queryKaoQinLocation(userId, proId, time);
    }

    public boolean saveOrUpdateKaoQinLocation(KaoQinLocation transientInstance) {
        return dataBaseDao.saveOrUpdateKaoQinLocation(transientInstance);
    }

    public Double countByUserIdAndProjectId(WorkLogGather bo) {
        return dataBaseDao.countByUserIdAndProjectId(bo);
    }
}
