package com.ktp.project.dao;

import com.ktp.project.dto.*;
import com.ktp.project.exception.BusinessException;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import com.zm.entity.ProjectCollectId;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-05 11:08
 */
@Repository
public class KaoqinDao {
    private static final Logger log = LoggerFactory.getLogger(KaoqinDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private QueryChannelDao queryChannelDao;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public int queryKaoqinCount(int userId) {
        int count = 0;
        String projectSql = "SELECT p.id FROM project p JOIN pro_organ po ON p.id = po.pro_id " +
                "JOIN pro_organ_per pe ON pe.po_id = po.id " +
                "WHERE pe.user_id=?";
        SQLQuery projectQuery = this.getCurrentSession().createSQLQuery(projectSql);
        projectQuery.setParameter(0, userId);
        List<Integer> projectIds = projectQuery.list();
        for (Integer id : projectIds) {
            try {
                String sql = "SELECT count(*) FROM kaoqin" + id + " WHERE user_id = ? AND DATE_FORMAT(k_time,'%Y-%m-%d')= DATE_FORMAT(NOW(),'%Y-%m-%d')";
                SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
                sqlQuery.setParameter(0, userId);
                BigInteger kqCount = (BigInteger) sqlQuery.uniqueResult();
                count += kqCount.intValue();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return count;
    }

    public Integer queryUserIdByKaoQinId(Integer projectId, Integer kaoQinId){
        String sql = "select user_id from kaoqin" + projectId + " where id =  " + kaoQinId;
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        return (Integer) sqlQuery.uniqueResult();
    }

    public List<Integer> queryIdByPro(Integer projectId){
        return queryChannelDao.selectMany("select id from kaoqin" + projectId + " where user_id != 0 and k_time between '2019-03-13 11:00:00' and '2019-03-14 10:00:00' order by id desc");
    }
}
