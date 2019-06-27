package com.ktp.project.dao;

import com.ktp.project.entity.ChatIgnore;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Created by LinHon 2018/8/20
 */
@Repository
public class ChatIgnoreDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public void create(ChatIgnore chatIgnore) {
        getCurrentSession().save(chatIgnore);
    }

    public void remove(ChatIgnore chatIgnore) {
        getCurrentSession().delete(chatIgnore);
    }

    public ChatIgnore query(int fromUserId, int toUserId) {
        String hql = "from ChatIgnore c where c.leftUid = ? and c.rightUid = ?";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, fromUserId);
        query.setParameter(1, toUserId);
        return (ChatIgnore) query.uniqueResult();
    }


}
