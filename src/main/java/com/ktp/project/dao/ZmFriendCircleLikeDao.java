package com.ktp.project.dao;

import com.ktp.project.dto.circledto.LikeDto;
import com.zm.friendCircle.entity.ZmFriendCircleLike;
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
 * 点赞dao
 * @Author: wuyeming
 * @Date: 2018-10-18 上午 10:26
 */
@Repository("likeDao")
public class ZmFriendCircleLikeDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
     * 查询点赞
     *
     * @Author: wuyeming
     * @params: [indexId, tIndex]
     * @Date: 2018-10-18 上午 10:49
     * @return java.util.List<com.zm.friendCircle.entity.ZmFriendCircleComment>
     *
     */
    public List<ZmFriendCircleLike> getLikeList(Integer indexId, Integer tIndex) {
        String hql = "from ZmFriendCircleLike where indexId = '" + indexId + "' and index = '" + tIndex + "' and likeDel = 1 order by id asc";
        List<ZmFriendCircleLike> list = null;
        try {
            Query query = getCurrentSession().createQuery(hql);
            list = query.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 根据id查找记录
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:38
      * @return com.zm.friendCircle.entity.ZmFriendCircleLike
      *
      */
    public ZmFriendCircleLike findById(Integer id) {
        ZmFriendCircleLike zmFriendCircleLike = null;
        try {
            zmFriendCircleLike = (ZmFriendCircleLike) this.getCurrentSession().get(ZmFriendCircleLike.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmFriendCircleLike;
    }


    /**
      * 查询点赞
      *
      * @return com.zm.friendCircle.entity.ZmFriendCircleLike
      * @Author: wuyeming
      * @params: [zmFriendCircleLike]
      * @Date: 2018-10-19 上午 11:19
      */
    public ZmFriendCircleLike findZmFriendCircleLike(ZmFriendCircleLike zmFriendCircleLike) {
        String hql = "from ZmFriendCircleLike where userId='" + zmFriendCircleLike.getUserId() + "' and indexId='" + zmFriendCircleLike.getIndexId() + "' and index='" + zmFriendCircleLike.getIndex() + "'";
        ZmFriendCircleLike like = null;
        try {
            like = (ZmFriendCircleLike) this.getCurrentSession().createQuery(hql).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return like;
    }


    /**
      * 保存点赞
      *
      * @Author: wuyeming
      * @params: [zmFriendCircleLike]
      * @Date: 2018-10-18 下午 15:05
      * @return void
      *
      */
    public void saveZmFriendCircleLike(ZmFriendCircleLike zmFriendCircleLike){
        try {
            this.getCurrentSession().saveOrUpdate(zmFriendCircleLike);
            this.getCurrentSession().flush();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
      * 根据id删除
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-18 下午 15:05
      * @return void
      *
      */
    public void deleteById(Integer id) {
        try {
            ZmFriendCircleLike zmFriendCircleLike = findById(id);
            if (zmFriendCircleLike != null) {
                String sql = "UPDATE zm_friend_circle_like SET like_del = 0 WHERE id = '" + id + "'";
                this.getCurrentSession().createSQLQuery(sql).executeUpdate();
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
     * @params: [zmFriendCircleLike]
     * @Date: 2018-10-19 下午 15:41
     */
    public void deleteZmFriendCircleLike(ZmFriendCircleLike zmFriendCircleLike){
        try {
            this.getCurrentSession().delete(zmFriendCircleLike);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
        String sql = "UPDATE zm_friend_circle_like SET like_del = 0 WHERE id IN (" + ids + ")";
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
     * @Date: 2018-10-19 下午 16:34
     */
    public void batchDeleteByObject(List<ZmFriendCircleLike> list) {
        try {
            List<Object> idList = new ArrayList<>();
            for (ZmFriendCircleLike zmFriendCircleLike : list) {
                idList.add(zmFriendCircleLike.getId());
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
     * @Date: 2018-10-26 下午 15:18
     */
    public void batchUpdateStatus(List<Object> idList){
        String ids = StringUtils.strip(idList.toString(), "[]");
        String sql = "UPDATE zm_friend_circle_like SET status = 1 WHERE id IN (" + ids + ")";
        try {
            this.getCurrentSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 查询点赞列表(用于新接口)
      *
      * @return java.util.List<com.ktp.project.dto.circledto.LikeDto>
      * @params: [indexId, tIndex]
      * @Author: wuyeming
      * @Date: 2018-11-20 下午 18:31
      */
    public List<LikeDto> getLikeListByNew(Integer indexId, Integer tIndex) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT l.id,l.like_del likeDel,l.create_time createTime,l.user_id userId,u.u_nicheng userName,u.u_pic uPic,u.u_sex uSex ");
        sql.append("FROM zm_friend_circle_like l JOIN user_info u ON u.id = l.user_id WHERE t_index='" + tIndex + "' AND index_id='" + indexId + "'  AND l.like_del = '1' ");
        sql.append("ORDER BY l.create_time ASC");
        List<LikeDto> list = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql.toString());
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(LikeDto.class));
            list = sqlQuery.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

}
