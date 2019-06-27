package com.ktp.project.dao;

import com.ktp.project.entity.KtpErrorLogEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 记录项目上传报错日志
 * @Author: xiaokai
 * @Date: 2019-01-03 上午10:51
 */
@Repository("ktpErrorLogDao")
public class KtpErrorLogDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    @Transactional
    public void saveOrUpdateGmProject(KtpErrorLogEntity ktpErrorLogEntity) {
        getCurrentSession().saveOrUpdate(ktpErrorLogEntity);
    }


}
