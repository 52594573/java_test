package com.ktp.project.dao.impl;

import com.ktp.project.dao.ModifyChannelDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by LinHon 2018/11/2
 */
@Repository
public class ModifyChannelDaoImpl implements ModifyChannelDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    @Override
    public void save(Object object) {
        getCurrentSession().save(object);
    }

    @Override
    public void update(Object object) {
        getCurrentSession().update(object);
    }

    @Override
    public void delete(Object object) {
        getCurrentSession().delete(object);
    }

    @Override
    public void saveOrUpdate(Object object) {
        getCurrentSession().saveOrUpdate(object);
    }

    @Override
    public void delete(Class clazz, int primaryKey) {
        Object object = getCurrentSession().get(clazz, primaryKey);
        delete(object);
    }

    @Override
    public void executeUpdate(String sql, Object... params) {
        getCurrentSession().createSQLQuery(String.format(sql, params)).executeUpdate();
    }

    @Override
    public void evict(Object object) {
        getCurrentSession().evict(object);
    }

}
