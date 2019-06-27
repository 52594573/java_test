package com.ktp.project.dao;

import com.google.common.collect.Lists;
import com.ktp.project.entity.ProxyClockIn;
import com.zm.entity.ProOrgan;
import com.zm.entity.Project;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 代理打卡
 * Created by LinHon 2018/12/4
 */
@Repository
public class ProxyClockInDao {

    private static final Logger log = LoggerFactory.getLogger(ProxyClockInDao.class);

    @Autowired
    private QueryChannelDao queryChannelDao;

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public void save(Object o) {
        getCurrentSession().save(o);
    }

    public void update(Object o) {
        getCurrentSession().update(o);
    }

    public void saveOrUpdate(Object o) {
        getCurrentSession().saveOrUpdate(o);
    }

    public void executeUpdate(String hql, Object... params) {
        getCurrentSession().createQuery(String.format(hql, params)).executeUpdate();
    }

    public void remove(Object o) {
        getCurrentSession().delete(o);
    }

    public ProOrgan queryOne(int primaryKey) {
        return (ProOrgan) getCurrentSession().get(ProOrgan.class, primaryKey);
    }

    /**
     * 查询我代理打卡的用户ID
     *
     * @param projectId
     * @param userId
     * @return
     */
    public List<Integer> queryMyProxyClockInUserIds(int projectId, int userId) {
        String hql = "select user_id from proxy_clock_in where project_id = ? and proxy_user_id = ? and to_days(create_time) = to_days(now()) group by user_id";
        return queryChannelDao.selectMany(hql, Lists.newArrayList(projectId, userId));
    }


    /**
     * 获取代理打卡详情列表
     */
    public List<ProxyClockIn> getPoClockInList(int proxyUserId, Date bDate, Date eDate, int userId,int proId) {
        try {
            String sql = "select pci.project_id projectId,pci.status,pci.create_time createTime,pci.longitude,pci.latitude,pci.image " +
                    " from proxy_clock_in pci " +
                    " where pci.proxy_user_id =? and pci.user_id = ? and pci.project_id = ? and pci.create_time between ? and ?";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProxyClockIn.class));
            sqlQuery.setParameter(0, proxyUserId);
            sqlQuery.setParameter(1, userId);
            sqlQuery.setParameter(2, proId);
            sqlQuery.setParameter(3, bDate);
            sqlQuery.setParameter(4, eDate);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("获取数据异常", re);
            throw re;
        }
    }

    /**
     * 获取自己打卡详情列表
     */
    public List<ProxyClockIn> getPoClockInList(int proxyUserId, Date bDate, Date eDate, int proId) {
        try {
            String sql = "select kaoqin.pro_id projectId," +
                    "kaoqin.k_state status," +
                    "kaoqin.k_time createTime," +
                    "kaoqin.k_lbsx latitude," +
                    "kaoqin.k_lbsy longitude," +
                    "kaoqin.k_pic image " +
                    " from kaoqin"+proId+ " kaoqin " +
                    " where kaoqin.user_id = ? and kaoqin.k_time between ? and ?";


            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProxyClockIn.class));
            sqlQuery.setParameter(0, proxyUserId);
            sqlQuery.setParameter(1, bDate);
            sqlQuery.setParameter(2, eDate);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("获取数据异常", re);
            throw re;
        }
    }

    public Project getprojectFWById(int proId) {
        try {
            String sql = "select p_lbs_fw PLbsFw,p_lbs_x PLbsX,p_lbs_y PLbsY from project where id = ?";


            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(Project.class));
            queryObject.setParameter(0, proId);
            //Object o =  queryObject.uniqueResult();
            return (Project) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取代理打卡列表", re);
            throw re;
        }
    }
}
