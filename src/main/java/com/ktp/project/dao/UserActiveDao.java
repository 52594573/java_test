package com.ktp.project.dao;

import com.ktp.project.entity.UserActive;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public class UserActiveDao {

    private static final Logger log = LoggerFactory.getLogger("UserActiveDao");

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存或更新
     */
    public boolean saveOrUpdate(int userId, double lat, double lon) {
        if (userId <= 0) {
            return false;
        }
        log.debug("save or update UserActive instance");
        try {
            if (checkexist(userId)) {
                int success = updateUserActive(userId, lat, lon);
                log.debug("save or update UserActive-- success" + success);
            } else {
                UserActive model = new UserActive();
                model.setUserId(userId);
                model.setLat(lat);
                model.setLon(lon);
                model.setLasttime(new Date());
                getCurrentSession().save(model);
            }
            log.debug("save or update UserActive successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save or update UserActive failed", re);
        }
        return false;
    }

    /**
     * 查询该用户是否已记录
     *
     * @return
     */
    public boolean checkexist(int userId) {
        try {
            String queryString = "select count(*) from UserActive ua where ua.userId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId);
            return (long) queryObject.uniqueResult() > 0;
        } catch (RuntimeException re) {
            return false;
        }
    }

    /**
     * 更新该条记录
     *
     * @return
     */
    public int updateUserActive(int userId, double lat, double lon) {
        try {
            String queryString = "update UserActive ua set ua.lat=?,ua.lon=? where ua.userId=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setDouble(0, lat);
            query.setDouble(1, lon);
            query.setInteger(2, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update UserActive fail", re);
            return -1;
        }
    }

}
