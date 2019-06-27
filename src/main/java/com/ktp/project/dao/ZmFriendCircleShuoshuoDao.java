package com.ktp.project.dao;

import com.zm.friendCircle.entity.ZmFriendCircleShuoshuo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 说说dao
 * @Author: wuyeming
 * @Date: 2018-10-18 下午 15:26
 */
@Repository("shuoshuoDao")
public class ZmFriendCircleShuoshuoDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
      * 保存说说
      *
      * @Author: wuyeming
      * @params: [zmFriendCircleShuoshuo]
      * @Date: 2018-10-18 下午 15:27
      * @return void
      *
      */
    public void saveShuoShuo(ZmFriendCircleShuoshuo zmFriendCircleShuoshuo) {
        try {
            this.getCurrentSession().saveOrUpdate(zmFriendCircleShuoshuo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id删除
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:28
      * @return void
      *
      */
    public void deleteById(Integer id) {
        try {
            ZmFriendCircleShuoshuo zmFriendCircleShuoshuo = findById(id);
            if (zmFriendCircleShuoshuo != null) {
                deleteZmFriendCircleShuoshuo(zmFriendCircleShuoshuo);
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
     * @params: [zmFriendCircleShuoshuo]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteZmFriendCircleShuoshuo(ZmFriendCircleShuoshuo zmFriendCircleShuoshuo) {
        try {
            this.getCurrentSession().delete(zmFriendCircleShuoshuo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id查找记录
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:41
      * @return com.zm.friendCircle.entity.ZmFriendCircleShuoshuo
      *
      */
    public ZmFriendCircleShuoshuo findById(Integer id){
        ZmFriendCircleShuoshuo zmFriendCircleShuoshuo = null;
        try {
            zmFriendCircleShuoshuo = (ZmFriendCircleShuoshuo) this.getCurrentSession().get(ZmFriendCircleShuoshuo.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmFriendCircleShuoshuo;
    }

}
