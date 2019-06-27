package com.ktp.project.service;

import com.ktp.project.dao.DataBaseDao;
import com.zm.entity.KeyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统信息
 *
 * @author djcken
 * @date 2018/6/11
 */
@Service
public class SystemService {

    @Autowired
    private DataBaseDao dataBaseDao;

    @Transactional
    public KeyContent getKeyContent() {
        return dataBaseDao.queryAppstoreAudit();
    }
}
