package com.ktp.project.dao;


import com.ktp.project.entity.GmProjectInfoEntity;
import com.ktp.project.entity.KtpPoOutEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 中山对接班组code
 *
 * @Author: lsh
 * @Date: 2018-10-18 下午 15:30
 */
@Repository("KtpPoOutDao")
public class KtpPoOutDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }



    @Transactional
    public void saveOrUpdateKtpPoOut(KtpPoOutEntity entity) {
        getCurrentSession().saveOrUpdate(entity);
    }


}
