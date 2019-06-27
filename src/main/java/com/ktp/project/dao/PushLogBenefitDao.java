package com.ktp.project.dao;

import com.ktp.project.entity.PushLogBenefit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @Author: wuyeming
 * @Date: 2018-12-12 下午 14:20
 */
@Repository
public class PushLogBenefitDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void saveOrUpdate(PushLogBenefit pushLogBenefit){
        getCurrentSession().saveOrUpdate(pushLogBenefit);
    }

    public void savePushLogBenefit(int tIndex, int indexId, Integer fromUserId, Integer toUserId, int type, int notify, int status) {
        PushLogBenefit pushLogBenefit = new PushLogBenefit();
        pushLogBenefit.settIndex(tIndex);
        pushLogBenefit.setIndexId(indexId);
        pushLogBenefit.setFromUserId(fromUserId);
        pushLogBenefit.setToUserId(toUserId);
        pushLogBenefit.setType(type);
        pushLogBenefit.setNotify(notify);
        pushLogBenefit.setStatus(status);
        pushLogBenefit.setCreateTime(new Date());
        saveOrUpdate(pushLogBenefit);
    }
}
