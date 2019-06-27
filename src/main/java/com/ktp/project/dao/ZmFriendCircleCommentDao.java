package com.ktp.project.dao;

import com.ktp.project.dto.circledto.CommentDto;
import com.zm.friendCircle.entity.ZmFriendCircleComment;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论dao
 * @Author: wuyeming
 * @Date: 2018-10-18 上午 10:26
 */
@Repository("commentDao")
public class ZmFriendCircleCommentDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
      * 查询评论
      *
      * @Author: wuyeming
      * @params: [indexId, tIndex, userId]
      * @Date: 2018-10-18 上午 10:49
      * @return java.util.List<com.zm.friendCircle.entity.ZmFriendCircleComment>
      *
      */
    public List<ZmFriendCircleComment> getCommentList(Integer indexId, Integer tIndex, Integer userId) {
        String hql = "from ZmFriendCircleComment where (indexId = '" + indexId + "' and index = '" + tIndex + "' and auditsStatus = 1 and commentDel = 1) ";
        if (userId != null) {
            hql += "OR (indexId = '" + indexId + "' AND index = '" + tIndex + "' AND fromUserId='" + userId + "' and commentDel = 1) ";
        }
        hql += "order by id asc";
        List<ZmFriendCircleComment> list = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            list = query.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 保存评论
      *
      * @Author: wuyeming
      * @params: [circleComment]
      * @Date: 2018-10-18 下午 14:45
      * @return void
      *
      */
    public void saveComment(ZmFriendCircleComment circleComment){
        try {
            this.getCurrentSession().saveOrUpdate(circleComment);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据id删除
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:06
      * @return void
      *
      */
    public void deleteById(Integer id) {
        String sql = "UPDATE zm_friend_circle_comment SET comment_del = 0 WHERE id = '" + id + "'";
        try {
            this.getCurrentSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 根据对象删除
     *
     * @return void
     * @Author: wuyeming
     * @params: [zmFriendCircleComment]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteComment(ZmFriendCircleComment zmFriendCircleComment){
        try {
            this.getCurrentSession().delete(zmFriendCircleComment);
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
      * @return com.zm.friendCircle.entity.ZmFriendCircleComment
      *
      */
    public ZmFriendCircleComment findById(Integer id) {
        ZmFriendCircleComment zmFriendCircleComment = null;
        try {
            zmFriendCircleComment = (ZmFriendCircleComment) getCurrentSession().get(ZmFriendCircleComment.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmFriendCircleComment;
    }


    /**
      * 根据id批量删除
      *
      * @return void
      * @Author: wuyeming
      * @params: [idList]
      * @Date: 2018-10-19 上午 10:45
      */
    public void batchDeleteById(List<Object> idList){
        String ids = StringUtils.strip(idList.toString(), "[]");
        String sql = "UPDATE zm_friend_circle_comment SET comment_del = 0 WHERE id IN (" + ids + ")";
        try {
            this.getCurrentSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据对象批量删除
      *
      * @return void
      * @Author: wuyeming
      * @params: [list]
      * @Date: 2018-10-19 下午 16:38
      */
    public void batchDeleteByObject(List<ZmFriendCircleComment> list) {
        try {
            List<Object> idList = new ArrayList<>();
            for (ZmFriendCircleComment zmFriendCircleComment : list) {
                idList.add(zmFriendCircleComment.getId());
            }
            if (idList.size() > 0) {
                batchDeleteById(idList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 根据id批量更新状态
     *
     * @return void
     * @Author: wuyeming
     * @params: [idList]
     * @Date: 2018-10-26 下午 15:20
     */
    public void batchUpdateStatus(List<Object> idList) {
        String ids = StringUtils.strip(idList.toString(), "[]");
        String sql = "UPDATE zm_friend_circle_comment SET status = 1 WHERE id IN (" + ids + ")";
        try {
            this.getCurrentSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 查询评论(用于新接口)
      *
      * @return java.util.List<com.ktp.project.dto.circledto.CommentDto>
      * @params: [indexId, tIndex, userId]
      * @Author: wuyeming
      * @Date: 2018-11-20 下午 18:35
      */
    public List<CommentDto> getCommentListByNew(Integer indexId, Integer tIndex, Integer userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT c.id,c.comment,c.comment_del commentDel,c.create_time createTime, ");
        sql.append("c.from_user_id fromUserId,fromUser.u_nicheng fromUserName,fromUser.u_sex fromUserSex,fromUser.u_pic fromUserPic, ");
        sql.append("c.to_user_id toUserId,toUser.u_nicheng toUserName,toUser.u_pic toUserPic,toUser.u_sex toUserSex ");
        sql.append("FROM zm_friend_circle_comment c JOIN user_info fromUser ON fromUser.id=c.from_user_id ");
        sql.append("LEFT JOIN user_info toUser ON toUser.id=c.to_user_id  ");
        sql.append("WHERE (c.t_index='" + tIndex + "' AND c.index_id='" + indexId + "' AND c.audits_status = 1 and c.comment_del = 1)  ");
        if (userId != null) {
            sql.append("OR (c.index_id = '" + indexId + "' AND c.t_index = '" + tIndex + "' AND c.from_user_id='" + userId + "' and c.comment_del = 1) ");
        }
        sql.append("ORDER BY id ASC ");
        List<CommentDto> list = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql.toString());
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(CommentDto.class));
            list = sqlQuery.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

}
