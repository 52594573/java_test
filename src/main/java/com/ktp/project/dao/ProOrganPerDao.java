package com.ktp.project.dao;

import com.zm.entity.ProOrganPer;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-12-7 下午 15:40
 */
@Repository("perDao")
@Transactional
public class ProOrganPerDao {
    private static final Logger log = LoggerFactory.getLogger(ProOrganPerDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
      * 根据班组id，人员类型查询人员
      *
      * @return java.util.List
      * @params: [poId, pType]
      * @Author: wuyeming
      * @Date: 2018-12-7 下午 15:52
      */
    public List queryUser(int poId, Integer ... popType) {
        String hql = "from ProOrganPer where poId=? and popType in (:popType) and popState <> 4";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, poId);
        query.setParameterList("popType", popType);
        List list = query.list();
        return list;
    }


    /**
     * 根据班组id查询性别总数量
     *
     * @return java.lang.Integer
     * @params: [poId, sex]
     * @Author: wuyeming
     * @Date: 2018-12-7 下午 15:59
     */
    public int querySexAllCount(int poId, int sex) {
        String sql = "SELECT COUNT(*) FROM pro_organ_per pe " +
                "JOIN user_info u ON u.id = pe.user_id " +
                " WHERE pe.po_id = ? AND pe.pop_type IN (4,8) AND pe.pop_state <> 4 AND u.u_sex = ?";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, poId);
        sqlQuery.setParameter(1, sex);
        BigInteger count = (BigInteger) sqlQuery.uniqueResult();
        return count.intValue();
    }


    /**
     * 查询性别的数量
     *
     * @return java.lang.Integer
     * @params: [poId, sex]
     * @Author: wuyeming
     * @Date: 2018-12-7 下午 16:05
     */
    public int querySexCount(int poId, int sex) {
        int age = sex == 1 ? 55 : 50;//如果性别=1：男时，年龄取55 否则年龄取50
        String sql = "SELECT COUNT(*) FROM pro_organ_per pe " +
                "JOIN user_info u ON u.id = pe.user_id " +
                "WHERE pe.po_id = ? AND pe.pop_type IN(4,8) AND pe.pop_state <> 4 AND u.u_sex = ? AND (YEAR(NOW())-YEAR(u.u_birthday) >= ?)";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, poId);
        sqlQuery.setParameter(1, sex);
        sqlQuery.setParameter(2, age);
        BigInteger count = (BigInteger) sqlQuery.uniqueResult();
        return count.intValue();
    }

    /**
    *
    * @Description: 根据班组id和查询出班组长对应pojo
    * @Author: liaosh
    * @Date: 2018/12/25 0025
    */
    public ProOrganPer queryProOrganByPoId(int po_id) {
        try {
            String queryString = "from ProOrganPer pop where pop.poId = ? and pop.popType = 8 and pop.popState = 0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, po_id);
            return (ProOrganPer) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取信息", re);
            throw re;
        }
    }

    /**
     *
     * @Description: 根据班组id和查询出部门对应pojo
     * @Author: liaosh
     * @Date: 2018/12/25 0025
     */
    public ProOrganPer queryProOrganBuMenByPoId(int po_id) {
        try {
            String queryString = "from ProOrganPer pop where pop.poId = ? and (pop.popType = 7 or pop.popType = 4) and pop.popState = 0 order by id asc  ";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setFirstResult(0);
            queryObject.setMaxResults(1);
            queryObject.setParameter(0, po_id);
            return (ProOrganPer) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取信息", re);
            throw re;
        }
    }



    /**
     *
     * @Description: 根据班组id和用户id查询出pojo
     * @Author: liaosh
     * @Date: 2018/12/25 0025
     */
    public ProOrganPer queryProOrganByUserId(int po_id,int user_id) {
        try {
            String queryString = "from ProOrganPer pop where pop.poId = ? and pop.userId = ? and pop.popState = 0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, po_id);
            queryObject.setParameter(1, user_id);
            return (ProOrganPer) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("获取信息", re);
            throw re;
        }
    }

    public boolean updateProOrganPer(ProOrganPer per) {
        try {
            getCurrentSession().update(per);
            log.debug("update ProOrganPer successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update ProOrganPer failed", re);
        }
        return false;
    }

    public List<ProOrganPer> listProOrganPerByPoId(Integer poId) {
        try {
            String queryString = "from ProOrganPer pop where pop.poId = ?  and pop.popState = 0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, poId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("获取信息", re);
            throw re;
        }
    }
}
