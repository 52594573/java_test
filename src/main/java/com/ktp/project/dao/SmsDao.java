package com.ktp.project.dao;

import com.ktp.project.entity.Sms;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Created by LinHon 2018/8/4
 */
@Repository
public class SmsDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public void create(Sms sms) {
        getCurrentSession().save(sms);
    }

    public long countByMobile(String mobile) {
        Query query = getCurrentSession().createQuery("select count(*) from Sms s where s.mobile = ? and s.createTime > ? and s.createTime < ?");
        Timestamp date = new Timestamp(System.currentTimeMillis());
        query.setParameter(0, mobile);
        query.setParameter(1, new Timestamp(date.getTime() - (1000 * 60 * 5)));
        query.setParameter(2, date);
        return (long)query.uniqueResult();
    }

}
