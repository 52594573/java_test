package com.ktp.project.dao;

import com.zm.entity.KeyContent;
import com.zm.friendCircle.entity.ZmEmployeeCircle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 找工dao
 *
 * @Author: wuyeming
 * @Date: 2018-10-18 下午 13:47
 */
@Repository("employeeDao")
public class ZmEmployeeCircleDao {
    private static final Integer WORK_TYPE = 4;
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * 保存找工
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmEmployeeCircle]
     * @Date: 2018-10-18 下午 13:59
     */
    public void saveZmEmployeeCircle(ZmEmployeeCircle zmEmployeeCircle) {
        try {
            this.getCurrentSession().saveOrUpdate(zmEmployeeCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id删除
      *
      * @Author: wuyeming
      * @params: [zmEmployeeCircle]
      * @Date: 2018-10-18 下午 14:03
      * @return void
      *
      */
    public void deleteById(Integer id) {
        try {
            ZmEmployeeCircle zmEmployeeCircle = findById(id);
            if (zmEmployeeCircle != null) {
                deleteZmEmployeeCircle(zmEmployeeCircle);
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
     * @params: [zmEmployeeCircle]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteZmEmployeeCircle(ZmEmployeeCircle zmEmployeeCircle) {
        try {
            this.getCurrentSession().delete(zmEmployeeCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id查找记录
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:38
      * @return com.zm.friendCircle.entity.ZmEmployeeCircle
      *
      */
    public ZmEmployeeCircle findById(Integer id) {
        ZmEmployeeCircle zmEmployeeCircle = null;
        try {
            zmEmployeeCircle = (ZmEmployeeCircle) getCurrentSession().get(ZmEmployeeCircle.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmEmployeeCircle;
    }


    /**
      * 获取工种类型
      *
      * @return java.util.List<com.zm.entity.KeyContent>
      * @Author: wuyeming
      * @params: []
      * @Date: 2018-10-19 下午 14:19
      */
    public List<KeyContent> getWorkType() {
        String hql = "from KeyContent where keyId = '" + WORK_TYPE + "'";
        List<KeyContent> list = null;
        try {
            list = this.getCurrentSession().createQuery(hql).list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }
}
