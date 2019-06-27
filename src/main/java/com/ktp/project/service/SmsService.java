package com.ktp.project.service;

import com.ktp.project.dao.SmsDao;
import com.ktp.project.entity.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by LinHon 2018/8/4
 */
@Service
@Transactional
public class SmsService {

    @Autowired
    private SmsDao smsDao;

    public void create(Sms sms) {
        smsDao.create(sms);
    }

    public long countByMobile(String mobile) {
        return smsDao.countByMobile(mobile);
    }
}
