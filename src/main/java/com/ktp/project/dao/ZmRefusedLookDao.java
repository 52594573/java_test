package com.ktp.project.dao;

import com.zm.friendCircle.entity.ZmRefusedLook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 屏蔽工友圈Dao
 *
 * @Author: wuyeming
 * @Date: 2018-10-19 下午 14:12
 */
@Repository("lookDao")
public class ZmRefusedLookDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * 是否屏蔽了某人
     *
     * @return com.zm.friendCircle.entity.ZmRefusedLook
     * @Author: wuyeming
     * @params: [fromUserId, toUserId]
     * @Date: 2018-10-19 下午 14:56
     */
    public ZmRefusedLook findZmRefusedLook(Integer fromUserId, Integer toUserId, Integer operator) {
        String hql = "from ZmRefusedLook where fromUserId = '" + fromUserId + "' and toUserId = '" + toUserId + "' and operator = '" + operator +"'";
        ZmRefusedLook zmRefusedLook = null;
        try {
            zmRefusedLook = (ZmRefusedLook) this.getCurrentSession().createQuery(hql).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmRefusedLook;
    }


    /**
     * 保存屏蔽记录
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmRefusedLook]
     * @Date: 2018-10-19 下午 15:01
     */
    public void saveZmRefusedLook(ZmRefusedLook zmRefusedLook){
        try {
            this.getCurrentSession().saveOrUpdate(zmRefusedLook);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id删除
      *
      * @return void
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-19 下午 15:00
      */
    public void deleteById(Integer id) {
        try {
            ZmRefusedLook zmRefusedLook = findById(id);
            if (zmRefusedLook != null) {
                deleteZmRefusedLook(zmRefusedLook);
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
     * @params: [zmRefusedLook]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteZmRefusedLook(ZmRefusedLook zmRefusedLook) {
        try {
            this.getCurrentSession().delete(zmRefusedLook);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id查找记录
      *
      * @return com.zm.friendCircle.entity.ZmRefusedLook
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-19 下午 14:59
      */
    public ZmRefusedLook findById(Integer id) {
        ZmRefusedLook zmRefusedLook = null;
        try {
            zmRefusedLook = (ZmRefusedLook) this.getCurrentSession().get(ZmRefusedLook.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmRefusedLook;
    }


    /**
      * 查询被屏蔽的内容
      *
      * @return com.zm.friendCircle.entity.ZmRefusedLook
      * @Author: wuyeming
      * @params: [fromUserId, index, indexId]
      * @Date: 2018-10-19 下午 15:11
      */
    public ZmRefusedLook refusedLookContent(Integer fromUserId, Integer index, Integer indexId) {
        String hql = "from ZmRefusedLook where fromUserId = '" + fromUserId + "' and index = '" + index + "' and indexId = '" + indexId + "'";
        ZmRefusedLook zmRefusedLook = null;
        try {
            zmRefusedLook = (ZmRefusedLook) getCurrentSession().createQuery(hql).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmRefusedLook;
    }

}
