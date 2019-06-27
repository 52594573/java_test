package com.ktp.project.dao;

import com.ktp.project.entity.PushLogCircle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: wuyeming
 * @Date: 2018-10-28 下午 16:19
 */
@Repository
public class PushLogCircleDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
      * 保存推送记录
      *
      * @return void
      * @Author: wuyeming
      * @params: [pushLogCircle]
      * @Date: 2018-10-28 下午 16:21
      */
    public void savePushLogCircle(PushLogCircle pushLogCircle){
        try {
            this.getCurrentSession().saveOrUpdate(pushLogCircle);
        } catch (Exception e) {
            throw e;
        }
    }
}
