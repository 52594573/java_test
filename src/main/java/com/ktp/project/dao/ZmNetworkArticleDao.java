package com.ktp.project.dao;

import com.zm.friendCircle.entity.ZmNetworkArticle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 网络内容表
 * @Author: wuyeming
 * @Date: 2018-10-18 下午 15:30
 */
@Repository("networkDao")
public class ZmNetworkArticleDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
      * 保存网络内容
      *
      * @Author: wuyeming
      * @params: [zmNetworkArticle]
      * @Date: 2018-10-18 下午 15:31
      * @return void
      *
      */
    public void saveZmNetworkArticle(ZmNetworkArticle zmNetworkArticle){
        try {
            this.getCurrentSession().saveOrUpdate(zmNetworkArticle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id删除
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:41
      * @return void
      *
      */
    public void deleteById(Integer id) {
        try {
            ZmNetworkArticle zmNetworkArticle = findById(id);
            if (zmNetworkArticle != null) {
                deleteZmNetworkArticle(zmNetworkArticle);
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
     * @params: [zmNetworkArticle]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteZmNetworkArticle(ZmNetworkArticle zmNetworkArticle) {
        try {
            this.getCurrentSession().delete(zmNetworkArticle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id查找记录
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:43
      * @return com.zm.friendCircle.entity.ZmNetworkArticle
      *
      */
    public ZmNetworkArticle findById(Integer id){
        ZmNetworkArticle zmNetworkArticle = null;
        try {
            zmNetworkArticle = (ZmNetworkArticle) this.getCurrentSession().get(ZmNetworkArticle.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmNetworkArticle;
    }
}
