package com.ktp.project.dao.impl;

import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.util.Page;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LinHon 2018/11/2
 */
@Repository
@Transactional
public class QueryChannelDaoImpl implements QueryChannelDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public <T> T queryOne(Class<T> clazz, Integer primaryKey) {
        return (T) getCurrentSession().get(clazz, primaryKey);
    }

    @Override
    public <T> T queryOne(String hql, Class<T> clazz) {
        return queryOne(hql, Arrays.asList(), clazz);
    }

    @Override
    public <T> T queryOne(String hql, List<Object> params, Class<T> clazz) {
        Query query = getCurrentSession().createQuery(hql);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return (T) query.uniqueResult();
    }

    @Override
    public Long queryCount(String hql) {
        return queryCount(hql, Arrays.asList());
    }

    @Override
    public Long queryCount(String hql, List<Object> params) {
        Query query = getCurrentSession().createQuery(hql);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return (Long) query.uniqueResult();
    }

    @Override
    public Double querySum(String hql) {
        return querySum(hql, Arrays.asList());
    }

    @Override
    public Double querySum(String hql, List<Object> params) {
        Query query = getCurrentSession().createQuery(hql);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        Double total = (Double) query.uniqueResult();
        return total == null ? 0.0 : total;
    }

    @Override
    public <T> T queryOneAndTransformer(String hql, Class<T> clazz) {
        return queryOneAndTransformer(hql, Arrays.asList(), clazz);
    }

    @Override
    public <T> T queryOneAndTransformer(String hql, List<Object> params, Class<T> clazz) {
        Query query = getCurrentSession().createQuery(hql);
        query.setResultTransformer(new AliasToBeanResultTransformer(clazz));
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return (T) query.uniqueResult();
    }

    @Override
    public <T> List<T> queryMany(String hql, Class<T> clazz) {
        return queryMany(hql, Arrays.asList(), clazz);
    }

    @Override
    public <T> List<T> queryMany(String hql, List<Object> params, Class<T> clazz) {
        Query query = getCurrentSession().createQuery(hql);
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return query.list();
    }

    @Override
    public <T> List<T> queryManyAndTransformer(String hql, Class<T> clazz) {
        return queryManyAndTransformer(hql, Arrays.asList(), clazz);
    }

    @Override
    public <T> List<T> queryManyAndTransformer(String hql, List<Object> params, Class<T> clazz) {
        Query query = getCurrentSession().createQuery(hql);
        query.setResultTransformer(new AliasToBeanResultTransformer(clazz));
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        return query.list();
    }

    @Override
    public <T> Page<T> queryPage(String hql, Page page, Class<T> clazz) {
        return queryPage(hql, page, Arrays.asList(), clazz);
    }

    @Override
    public <T> Page<T> queryPage(String hql, Page page, List<Object> params, Class<T> clazz) {
        Query query = getCurrentSession().createQuery(hql);
        query.setFirstResult(page.getStartLine());
        query.setMaxResults(page.getPageSize());
        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        page.setResult(query.list());
        page.setTotalCount(queryCount(conversion(hql), params));
        return page.builderPage();
    }


    @Override
    public <T> T selectOne(String sql) {
        return selectOne(sql, Arrays.asList());
    }

    @Override
    public <T> T selectOne(String sql, List<Object> params) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return (T) sqlQuery.uniqueResult();
    }

    @Override
    public <T> T selectOne(String sql, Class<T> clazz) {
        return selectOne(sql, Arrays.asList(), clazz);
    }

    @Override
    public <T> T selectOne(String sql, List<Object> params, Class<T> clazz) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.addEntity(clazz);
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return (T) sqlQuery.uniqueResult();
    }

    @Override
    public Long selectCount(String sql) {
        return selectCount(sql, Arrays.asList());
    }

    @Override
    public Long selectCount(String sql, List<Object> params) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        BigInteger total = (BigInteger) sqlQuery.uniqueResult();
        return total.longValue();
    }

    @Override
    public Double selectSum(String sql) {
        return selectSum(sql, Arrays.asList());
    }

    @Override
    public Double selectSum(String sql, List<Object> params) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return (Double) sqlQuery.uniqueResult();
    }

    @Override
    public <T> T selectOneAndTransformer(String sql, Class<T> clazz) {
        return selectOneAndTransformer(sql, Arrays.asList(), clazz);
    }

    @Override
    public <T> T selectOneAndTransformer(String sql, List<Object> params, Class<T> clazz) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(clazz));
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return (T) sqlQuery.uniqueResult();
    }

    @Override
    public List selectMany(String sql) {
        return selectMany(sql, Arrays.asList());
    }

    @Override
    public List selectMany(String sql, List<Object> params) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return sqlQuery.list();
    }

    @Override
    public <T> List<T> selectMany(String sql, Class<T> clazz) {
        return selectMany(sql, Arrays.asList(), clazz);
    }

    @Override
    public <T> List<T> selectMany(String sql, List<Object> params, Class<T> clazz) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.addEntity(clazz);
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return sqlQuery.list();
    }

    @Override
    public <T> List<T> selectManyAndTransformer(String sql, Class<T> clazz) {
        return selectManyAndTransformer(sql, Arrays.asList(), clazz);
    }

    @Override
    public <T> List<T> selectManyAndTransformer(String sql, List<Object> params, Class<T> clazz) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(clazz));
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        return sqlQuery.list();
    }

    @Override
    public <T> Page<T> selectPage(String sql, Page page, Class<T> clazz) {
        return selectPage(sql, page, Arrays.asList(), clazz);
    }

    @Override
    public <T> Page<T> selectPage(String sql, Page page, List<Object> params, Class<T> clazz) {
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.addEntity(clazz);
        sqlQuery.setFirstResult(page.getStartLine());
        sqlQuery.setMaxResults(page.getPageSize());
        for (int i = 0; i < params.size(); i++) {
            sqlQuery.setParameter(i, params.get(i));
        }
        page.setResult(sqlQuery.list());
        page.setTotalCount(selectCount(conversion(sql), params));
        return page.builderPage();
    }

    private String conversion(String str) {
        if (str.startsWith("from") || str.startsWith("FROM")) {
            return "select count(1) " + str;
        }
        return str.replaceFirst("select.* from|SELECT.* FROM", "select count(1) from");
    }


}
