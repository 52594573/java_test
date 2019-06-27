package com.ktp.project.dao;

import com.zm.friendCircle.entity.ZmEmployerCircle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 招工Dao
 * @Author: wuyeming
 * @Date: 2018-10-18 下午 13:50
 */
@Repository("employerDao")
public class ZmEmployerCircleDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * 保存招工
     *
     * @Author: wuyeming
     * @params: [zmEmployerCircle]
     * @Date: 2018-10-18 下午 13:59
     * @return void
     *
     */
    public void saveZmEmployerCircle(ZmEmployerCircle zmEmployerCircle) {
        try {
            this.getCurrentSession().saveOrUpdate(zmEmployerCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据id删除
     *
     * @Author: wuyeming
     * @params: [zmEmployerCircle]
     * @Date: 2018-10-18 下午 14:03
     * @return void
     *
     */
    public void deleteById(Integer id) {
        try {
            ZmEmployerCircle zmEmployerCircle = findById(id);
            if (zmEmployerCircle != null) {
                deleteZmEmployerCircle(zmEmployerCircle);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 根据对象删除
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmEmployerCircle]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteZmEmployerCircle(ZmEmployerCircle zmEmployerCircle) {
        try {
            this.getCurrentSession().delete(zmEmployerCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id查找记录
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:39
      * @return com.zm.friendCircle.entity.ZmEmployerCircle
      *
      */
    public ZmEmployerCircle findById(Integer id) {
        ZmEmployerCircle zmEmployerCircle = null;
        try {
            zmEmployerCircle = (ZmEmployerCircle) getCurrentSession().get(ZmEmployerCircle.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmEmployerCircle;
    }
}
