package com.ktp.project.dao;

import com.ktp.project.dto.LikeCommentDto;
import com.ktp.project.dto.PicDto;
import com.ktp.project.dto.ZmFriendCircleDto;
import com.ktp.project.dto.circledto.CircleDto;
import com.zm.entity.KeyContent;
import com.zm.friendCircle.entity.ZmFriendCircle;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-10-17 下午 15:36
 */
@Repository("circleDao")
public class ZmFriendCircleDao {
    //未读状态
    private final static Integer noReadStatus = 0;

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 查询分享
     *
     * @Author: wuyeming
     * @params: [index, userId, begin, length] index=0全部  index=1说说  index=2招工  index=3找工  index=4网络内容
     * @Date: 2018-10-17 下午 17:05
     * @return java.util.List<com.ktp.project.dto.ZmFriendCircleDto>
     *
     */
    public List<ZmFriendCircleDto> getContent(Integer index, Integer userId, Integer begin, Integer length) {
        StringBuffer buffer = new StringBuffer();
        List<ZmFriendCircleDto> list = null;
        try {
            buffer.append("SELECT DISTINCT c.id,c.userId,c.content,c.works,c.amount,c.address,DATE_FORMAT(c.createDate, '%Y-%m-%d %T') createDate," +
                    "DATE_FORMAT(c.arriveTime, '%Y-%m-%d %T') arriveTime,c.tIndex,c.workType,c.url,c.summary,c.picUrl,IFNULL(c.gzId,'') gzId, " +
                    "IFNULL(c.gzName,'') gzName FROM zm_friend_circle f " +
                    "JOIN v_zm_friend_circle c ON (f.t_index = c.tIndex AND c.id = f.index_id) " +
                    "WHERE f.circle_del = 1 AND (f.status = 1 OR f.user_id = '" + userId + "') ");
            List<Integer> idList = getrefusedLookUserId(userId);
            if (idList.size() > 0) {
                String ids = StringUtils.strip(idList.toString(), "[]");
                buffer.append("AND c.userId NOT IN(" + ids + ") ");
            }
            if (index != null) {//查询所有分享
                if (index != 0) {
                    buffer.append("AND tIndex = '" + index + "' ");//查询分类
                }
            } else {//查询自己的分享
                buffer.append("AND f.user_id = '" + userId + "' ");
            }
            buffer.append("ORDER BY c.createDate DESC ");
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(buffer.toString());
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ZmFriendCircleDto.class));
            sqlQuery.setFirstResult(begin);
            sqlQuery.setMaxResults(length);
            list = sqlQuery.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 保存
      *
      * @Author: wuyeming
      * @params: [zmFriendCircle]
      * @Date: 2018-10-19 上午 9:56
      * @return void
      *
      */
    public void saveZmFriendCircle(ZmFriendCircle zmFriendCircle){
        try {
            this.getCurrentSession().saveOrUpdate(zmFriendCircle);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
      * 删除
      *
      * @Author: wuyeming
      * @params: [indexId, index]
      * @Date: 2018-10-19 上午 9:57
      * @return void
      *
      */
    public void deleteZmFriendCircle(Integer indexId, Integer index) {
        try {
            ZmFriendCircle zmFriendCircle = findZmFriendCircle(indexId, index);
            if (zmFriendCircle != null) {
                String sql = "UPDATE zm_friend_circle SET circle_del = 0 WHERE id = '" + zmFriendCircle.getId() + "'";
                this.getCurrentSession().createSQLQuery(sql).executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
      * 查询单个对象
      *
      * @Author: wuyeming
      * @params: [indexId, index]
      * @Date: 2018-10-19 上午 9:57
      * @return com.zm.friendCircle.entity.ZmFriendCircle
      *
      */
    public ZmFriendCircle findZmFriendCircle(Integer indexId, Integer index) {
        String hql = "from ZmFriendCircle where indexId = '" + indexId + "' and index = '" + index + "' and circleDel = 1";
        ZmFriendCircle zmFriendCircle = null;
        try {
            zmFriendCircle = (ZmFriendCircle) this.getCurrentSession().createQuery(hql).uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmFriendCircle;
    }


    /**
     * 根据用户id查询被屏蔽的人员
     *
     * @return java.util.List<java.lang.Integer>
     * @Author: wuyeming
     * @params: [userId]
     * @Date: 2018-10-25 下午 13:34
     */
    private List<Integer> getrefusedLookUserId(Integer userId) {
        List<Integer> idList = new ArrayList<>();
        String sql = "SELECT to_user_id FROM zm_refused_look WHERE from_user_id='" + userId + "' AND to_user_id IS NOT NULL";
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            idList = sqlQuery.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return idList;
    }


    /**
      * 获取未读的点赞和评论
      *
      * @return java.util.List<com.ktp.project.dto.LikeCommentDto>
      * @Author: wuyeming
      * @params: [userId]
      * @Date: 2018-10-26 下午 14:00
      */
    public List<LikeCommentDto> getLikeAndCommentList(Integer userId) {
        List<LikeCommentDto> list = null;
        String sql = "SELECT c.id,c.tIndex,v.userId,c.comment,c.fromUserId,t.u_nicheng uRealname,t.u_pic uPic,t.u_sex uSex,v.content,c.type,c.createTime,c.indexId,c.commentDel " +
                "FROM v_zm_friend_circle v LEFT JOIN v_zm_like_comment c ON v.id = c.indexId " +
                "JOIN user_info u ON u.id = v.userId " +
                "JOIN user_info t ON t.id = c.fromUserId " +
                "WHERE ((c.toUserId = '" + userId + "') OR (v.userId = '" + userId + "' AND c.toUserId = 0)) " +
                "AND (c.status = '" + noReadStatus + "' OR c.status IS NULL) AND c.auditsStatus = 1 ORDER BY c.createTime DESC";
        try {
            SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(LikeCommentDto.class));
            list = sqlQuery.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 获取未读点赞评论列表的图片和视频
      *
      * @return void
      * @Author: wuyeming
      * @params: [list]
      * @Date: 2018-10-31 上午 10:14
      */
    public List getLikeCommentPic(LikeCommentDto likeComment) {
        List<PicDto> picList = null;
        Integer index = likeComment.gettIndex();
        String picSql = null;
        if (index != null && index != 4) {
            picSql = "SELECT pic_url picUrl,type FROM zm_friend_circle_album a WHERE a.shuoshuo_id = '" + likeComment.getIndexId() + "' and a.t_index = '" + likeComment.gettIndex() + "' order by id asc";
        } else {
            picSql = "SELECT pic_url picUrl,0 type FROM zm_network_article a WHERE a.id = '" + likeComment.getIndexId() + "' AND a.t_index = '" + likeComment.gettIndex() + "'";
        }
        SQLQuery picQuery = this.getCurrentSession().createSQLQuery(picSql);
        picQuery.setResultTransformer(new AliasToBeanResultTransformer(PicDto.class));
        picList = picQuery.list();
        return picList;
    }


    /**
      * 获取未读的点赞和评论数
      *
      * @return java.util.Map<java.lang.String,java.lang.Object>
      * @Author: wuyeming
      * @params: [userId]
      * @Date: 2018-10-26 下午 17:22
      */
    public Map<String, Object> getLikeAndCommentCounts(Integer userId) {
        String sql = "SELECT t.u_pic uPic,t.u_sex uSex FROM v_zm_friend_circle v LEFT JOIN v_zm_like_comment c ON (v.id = c.indexId AND v.tIndex = c.tIndex) " +
                "JOIN user_info u ON u.id = v.userId " +
                "JOIN user_info t ON t.id = c.fromUserId " +
                "WHERE ((c.toUserId = '" + userId + "') OR (v.userId = '" + userId + "' AND c.toUserId = 0)) " +
                "AND (c.status = '" + noReadStatus + "' OR c.status IS NULL) AND c.auditsStatus = 1 ORDER BY c.createTime DESC";
        try {
            SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Map<String,Object> map = new HashMap<>();
            map.put("counts", 0);
            List<Map<String,Object>> list =  sqlQuery.list();
            if (list.size() > 0) {
                map.put("uPic", list.get(0).get("uPic"));
                map.put("uSex", list.get(0).get("uSex"));
                map.put("counts", list.size());
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 查询分享详情
      *
      * @return java.util.List<com.ktp.project.dto.ZmFriendCircleDto>
      * @Author: wuyeming
      * @params: [index, indexId]
      * @Date: 2018-10-29 下午 14:44
      */
    public List<ZmFriendCircleDto> getContentInfo(Integer index, Integer indexId) {
        String sql = "SELECT DISTINCT c.id,c.userId,c.content,c.works,c.amount,c.address,DATE_FORMAT(c.createDate, '%Y-%m-%d %T') createDate,f.status," +
                "DATE_FORMAT(c.arriveTime, '%Y-%m-%d %T') arriveTime,c.tIndex,c.workType,c.url,c.summary,c.picUrl,c.gzId,c.gzName,f.circle_del circleDel FROM zm_friend_circle f  " +
                "JOIN v_zm_friend_circle c ON (f.index_id = c.id AND c.tIndex = f.t_index) WHERE tIndex='" + index + "' AND c.id='" + indexId + "'";
        List<ZmFriendCircleDto> list = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ZmFriendCircleDto.class));
            list = sqlQuery.list();
        } catch (Exception e) {
            throw e;
        }
        return list;
    }


    /**
     * 获取招聘类型
     *
     * @return java.util.List<com.zm.entity.KeyContent>
     * @Author: wuyeming
     * @params: []
     * @Date: 2018-11-1 上午 10:52
     */
    public List<KeyContent> getEmployType() {
        String hql = "from KeyContent where id in(4,8)";
        List<KeyContent> list = null;
        try {
            Query query = this.getCurrentSession().createQuery(hql);
            list = query.list();
        } catch (Exception e) {
            throw e;
        }
        return list;
    }


    /**
      * 查询分享(用于新接口)
      *
      * @return java.util.List<com.ktp.project.dto.circledto.CircleDto>
      * @params: [index, userId, begin, length]
      * @Author: wuyeming
      * @Date: 2018-11-20 下午 18:33
      */
    public List<CircleDto> getContentByNew(Integer index, Integer userId, Integer begin, Integer length) {
        StringBuffer buffer = new StringBuffer();
        List<CircleDto> list = null;
        try {
            buffer.append("SELECT DISTINCT c.id,c.userId,u.u_nicheng userName,u.u_pic uPic,u.u_sex uSex,u.u_jf jf,u.u_jnf jnf,u.u_xyf xyf,c.content,c.works,c.amount,c.address,DATE_FORMAT(c.createDate, '%Y-%m-%d %T') createDate," +
                    "DATE_FORMAT(c.arriveTime, '%Y-%m-%d %T') arriveTime,c.tIndex,c.workType,c.url,c.summary,c.picUrl,IFNULL(c.gzId,'') gzId, " +
                    "IFNULL(c.gzName,'') gzName FROM zm_friend_circle f " +
                    "JOIN v_zm_friend_circle c ON (f.t_index = c.tIndex AND c.id = f.index_id) " +
                    "JOIN user_info u ON u.id = f.user_id " +
                    "WHERE f.circle_del = 1 AND (f.status = 1 OR f.user_id = '" + userId + "') ");
            List<Integer> idList = getrefusedLookUserId(userId);
            if (idList.size() > 0) {
                String ids = StringUtils.strip(idList.toString(), "[]");
                buffer.append("AND c.userId NOT IN(" + ids + ") ");
            }
            if (index != null) {//查询所有分享
                if (index != 0) {
                    buffer.append("AND tIndex = '" + index + "' ");//查询分类
                }
            } else {//查询自己的分享
                buffer.append("AND f.user_id = '" + userId + "' ");
            }
            buffer.append("ORDER BY c.createDate DESC ");
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(buffer.toString());
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(CircleDto.class));
            sqlQuery.setFirstResult(begin);
            sqlQuery.setMaxResults(length);
            list = sqlQuery.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

}
