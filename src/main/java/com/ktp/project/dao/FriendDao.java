package com.ktp.project.dao;

import com.ktp.project.dto.FriendListDto;
import com.ktp.project.entity.ChatFriend;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by LinHon 2018/8/4
 */
@Repository
public class FriendDao {

    private final int AGREE = 2;
    private final int REMOVE = 3;
    private final int NOT_READ = 99;

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public void saveOrUpdateFriend(ChatFriend chatFriend) {
        getCurrentSession().saveOrUpdate(chatFriend);
    }

    /**
     * 新的朋友未读数量
     *
     * @param userId
     * @return
     */
    public long queryNewFriendNotReadNumber(int userId) {
        String hql = "select count(*) from ChatFriend c where c.rightUid = ? and c.relationType = ?";
        Query queryObject = getCurrentSession().createQuery(hql);
        queryObject.setParameter(0, userId);
        queryObject.setParameter(1, NOT_READ);
        return (long) queryObject.uniqueResult();
    }

    /**
     * 根据手机号搜索好友
     *
     * @param mobile
     * @return
     */
    public List queryFriendsByMobile(String mobile) {
        String hql = "select u.id as user_id , u.u_sex as u_sex , u.u_realname as u_realname , u.u_nicheng as u_nicheng , u.u_pic as u_pic , u.u_cert as u_cert from UserInfo u where u.u_name = ?";
        Query query = getCurrentSession().createQuery(hql);
        query.setResultTransformer(new AliasToBeanResultTransformer(FriendListDto.class));
        query.setParameter(0, mobile);
        return query.list();
    }

    /**
     * 查询好友
     *
     * @param userId
     * @param userFriendId
     * @return
     */
    public ChatFriend queryFriend(int userId, int userFriendId) {
        Query query = getCurrentSession().createQuery("from ChatFriend c where c.leftUid = ? and c.rightUid = ?");
        query.setParameter(0, userId);
        query.setParameter(1, userFriendId);
        return (ChatFriend) query.uniqueResult();
    }

    /**
     * 根据添加状态查询好友
     *
     * @param userId
     * @param relationType
     * @return
     */
    public List queryFriendsByRelationType(int userId, int relationType) {
        Query query = getCurrentSession().createQuery("from ChatFriend c where c.rightUid = ? and relationType = ?");
        query.setParameter(0, userId);
        query.setParameter(1, relationType);
        return query.list();
    }

    /**
     * 查询新的朋友记录
     *
     * @param userId
     * @param start
     * @param end
     * @return
     */
    public List queryNewFriends(int userId, int start, int end) {
        String sql = "select u.id user_id , u.u_sex , u.u_realname , u.u_nicheng as u_nicheng , u.u_pic , u.u_cert , c.apply_msg , c.relationType " +
                "from chat_friend c inner join user_info u on c.left_uid = u.id " +
                "where c.right_uid = ? and c.relationType != ? order by c.last_time desc";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(FriendListDto.class));
        sqlQuery.setParameter(0, userId);
        sqlQuery.setParameter(1, REMOVE);
        sqlQuery.setFirstResult(start);
        sqlQuery.setMaxResults(end);
        return sqlQuery.list();
    }

    /**
     * 查询所有好友
     *
     * @param userId
     * @return
     */
    public List queryFriends(int userId) {
        String sql = "select u.id user_id , u.u_sex , u.u_realname , u.u_nicheng as u_nicheng , u.u_pic , u.u_cert, u.u_name mobile " +
                "from chat_friend c inner join user_info u on c.right_uid = u.id " +
                "where c.left_uid = ? and c.relationType = ? order by c.last_time desc";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(FriendListDto.class));
        sqlQuery.setParameter(0, userId);
        sqlQuery.setParameter(1, AGREE);
        return sqlQuery.list();
    }

    /**
     * 查询所有用户ID
     *
     * @return
     */
    public List queryUsers() {
        String hql = "select u.id from UserInfo u";
        Query query = getCurrentSession().createQuery(hql);
        return query.list();
    }

}
