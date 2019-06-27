package com.ktp.project.dao;

import com.ktp.project.dto.*;
import com.ktp.project.entity.UserJf;
import com.ktp.project.exception.BusinessException;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import com.zm.entity.ProjectCollectId;
import org.apache.ibatis.annotations.Param;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-10-05 11:08
 */
@Repository
@Transactional
public class UserJfDao {
    private static final Logger log = LoggerFactory.getLogger(UserJfDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void saveOrUpdate(UserJf userJf) {
        this.getCurrentSession().saveOrUpdate(userJf);
    }


    public void save(UserJf userJf) {
        this.getCurrentSession().save(userJf);
    }


    /**
     * 根据用户id查询积分总数
     *
     * @param userId
     * @return
     */
    public int getJfByUserId(int userId) {
        String sql = "SELECT IFNULL(SUM(jf_shu),0) jf FROM user_jf WHERE user_id=?";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        Double count = (Double) sqlQuery.uniqueResult();
        return count.intValue();
    }

    public List<UserJf> getJfListByUserId(int userId) {
        String sql = "SELECT *  FROM user_jf WHERE user_id=? and jf_state = 184";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        return sqlQuery.list();
    }


    /**
     * 查询月份
     *
     * @return java.util.List<java.util.Map                                                               <                                                               java.lang.String                                                               ,                                                               java.lang.Object>>
     * @params: [userId]
     * @Author: wuyeming
     * @Date: 2019-01-15 上午 11:38
     */
    public List<Map<String, Object>> getMonthByUserId(int userId) {
        String sql = "SELECT jf_yue month FROM user_jf WHERE user_id = ? AND DATE_FORMAT(in_time,'%Y-%m-%d') >= '2018-08-26' GROUP BY jf_yue ORDER BY jf_yue DESC";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }


    /**
     * 根据月份查询积分使用/获取总数
     *
     * @return int
     * @params: [userId, month, type]
     * @Author: wuyeming
     * @Date: 2019-01-15 上午 11:38
     */
    public int getMonthJfByUserId(int userId, String month, int type) {
        String sql = "SELECT IFNULL(SUM(jf_shu),0) jfSum FROM user_jf WHERE user_id = ? AND jf_yue= ? AND jf_type=?";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        sqlQuery.setParameter(1, month);
        sqlQuery.setParameter(2, type);
        Double count = (Double) sqlQuery.uniqueResult();
        return count.intValue();
    }


    /**
     * 根据月份查询积分使用/获取详情
     *
     * @return java.util.List<java.util.Map                                                               <                                                               java.lang.String                                                               ,                                                               java.lang.Object>>
     * @params: [userId, month]
     * @Author: wuyeming
     * @Date: 2019-01-15 上午 11:39
     */
    public List<Map<String, Object>> getJfDetail(int userId, String month) {
        String sql = "SELECT u.id,k.key_name keyName,u.jf_type type,u.jf_shu jfShu,DATE_FORMAT(u.in_time,'%Y-%m-%d') intime FROM user_jf u " +
                "JOIN key_content k ON k.id=u.jf_state " +
                "WHERE user_id=? AND jf_yue=? ORDER BY in_time DESC";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        sqlQuery.setParameter(1, month);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = sqlQuery.list();
        return list;
    }
}
