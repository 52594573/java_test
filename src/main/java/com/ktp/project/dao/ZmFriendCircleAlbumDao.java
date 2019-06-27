package com.ktp.project.dao;

import com.ktp.project.dto.PicDto;
import com.zm.friendCircle.entity.ZmFriendCircleAlbum;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分享图片
 * @Author: wuyeming
 * @Date: 2018-10-18 下午 16:30
 */
@Repository("albumDao")
public class ZmFriendCircleAlbumDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
      * 查询分享图片
      *
      * @Author: wuyeming
      * @params: [indexId, index]
      * @Date: 2018-10-18 下午 16:43
      * @return java.util.List<java.lang.String>
      *
      */
    public List getPicList(Integer indexId, Integer index) {
        String sql = "SELECT pic_url picUrl,type FROM zm_friend_circle_album WHERE shuoshuo_id='" + indexId + "' AND t_index='" + index + "' order by id asc";
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(PicDto.class));
            List<PicDto> picList = sqlQuery.list();
            PicDto pic = null;
            for (PicDto picDto : picList) {
                Map<String, Object> map = new HashMap<>();
                if (pic == null) {
                    pic = picDto;
                }
                if (picDto.getType() == pic.getType()) {
                    map.put("type", picDto.getType());
                    map.put("picUrl", picDto.getPicUrl());
                } else {
                    map.put("type", picDto.getType());
                    map.put("picUrl", pic.getPicUrl());
                    map.put("videoUrl", picDto.getPicUrl());
                    list.clear();
                }
                list.add(map);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 保存说说图片
      *
      * @Author: wuyeming
      * @params: [zmFriendCircleAlbum]
      * @Date: 2018-10-18 下午 17:19
      * @return void
      *
      */
    public void saveZmFriendCircleAlbum(ZmFriendCircleAlbum zmFriendCircleAlbum){
        try {
            this.getCurrentSession().saveOrUpdate(zmFriendCircleAlbum);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 获取分享图片对象列表
      *
      * @Author: wuyeming
      * @params: [indexId, index]
      * @Date: 2018-10-19 上午 10:12
      * @return java.util.List<com.zm.friendCircle.entity.ZmFriendCircleAlbum>
      *
      */
    public List<ZmFriendCircleAlbum> getAlbumList(Integer indexId, Integer index) {
        String hql = "from ZmFriendCircleAlbum where shuoshuoId = '" + indexId + "' and index = '" + index + "'";
        List<ZmFriendCircleAlbum> list = null;
        try {
            Query query = this.getCurrentSession().createQuery(hql);
            list = query.list();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }


    /**
      * 根据id删除
      *
      * @Author: wuyeming
      * @params: [id]
      * @Date: 2018-10-19 上午 10:24
      * @return void
      *
      */
    public void deleteById(Integer id) {
        try {
            ZmFriendCircleAlbum zmFriendCircleAlbum = findById(id);
            if (zmFriendCircleAlbum != null) {
                deleteZmFriendCircleAlbum(zmFriendCircleAlbum);
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
      * @params: [zmFriendCircleAlbum]
      * @Date: 2018-10-19 下午 15:41
      */
    public void deleteZmFriendCircleAlbum(ZmFriendCircleAlbum zmFriendCircleAlbum) {
        try {
            this.getCurrentSession().delete(zmFriendCircleAlbum);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 根据id查找记录
     *
     * @return com.zm.friendCircle.entity.ZmFriendCircleAlbum
     * @Author: wuyeming
     * @params: [id]
     * @Date: 2018-10-19 上午 10:24
     */
    public ZmFriendCircleAlbum findById(Integer id) {
        ZmFriendCircleAlbum zmFriendCircleAlbum = null;
        try {
            zmFriendCircleAlbum = (ZmFriendCircleAlbum) this.getCurrentSession().get(ZmFriendCircleAlbum.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return zmFriendCircleAlbum;
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
        String sql = "DELETE FROM zm_friend_circle_album WHERE id IN (" + ids + ")";
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
      * @Date: 2018-10-19 下午 16:36
      */
    public void batchDeleteByObject(List<ZmFriendCircleAlbum> list){
        try {
            List<Object> idList = new ArrayList<>();
            for (ZmFriendCircleAlbum zmFriendCircleAlbum : list) {
                idList.add(zmFriendCircleAlbum.getId());
            }
            if (idList.size() > 0) {
                batchDeleteById(idList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
      * 根据人员id获取相册
      *
      * @return java.util.List<com.zm.friendCircle.entity.ZmFriendCircleAlbum>
      * @Author: wuyeming
      * @params: [fromUserId, toUserId, length]
      * @Date: 2018-10-25 上午 9:39
      */
    public Map<String,Object> getAlbumListByUserId(Integer fromUserId, Integer toUserId, Integer length) {
        try {
            Map<String, Object> map = new HashMap<>();
            List<ZmFriendCircleAlbum> list = new ArrayList<>();
            String sql = "SELECT from_user_id fromUserId FROM zm_refused_look WHERE to_user_id = '" + toUserId + "' AND operator = '" + toUserId + "'";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            List<Integer> idList = sqlQuery.list();
            if (idList.size() > 0 && idList.contains(fromUserId)) {//如果被屏蔽
                map.put("flag", true);
                map.put("picList", list);
                return map;
            } else {
//                String hql = "from ZmFriendCircleAlbum where userId = '" + toUserId + "' order by id desc";
//                Query queryObject = getCurrentSession().createQuery(hql);
//                queryObject.setFirstResult(0);
//                queryObject.setMaxResults(length);
//                list = queryObject.list();
                String picSql = "SELECT a.id,a.shuoshuo_id shuoshuoId,a.pic_url picUrl,a.user_id userId,a.t_index 'index',a.type FROM zm_friend_circle_album a " +
                        "JOIN zm_friend_circle c ON c.t_index = a.t_index AND c.index_id = a.shuoshuo_id AND a.t_index = 3 " +
                        "WHERE c.circle_del = 1 AND a.user_id='" + toUserId + "' AND a.type = 0 ORDER BY id DESC";
                SQLQuery query = getCurrentSession().createSQLQuery(picSql);
                query.setResultTransformer(new AliasToBeanResultTransformer(ZmFriendCircleAlbum.class));
                query.setFirstResult(0);
                query.setMaxResults(length);
                list = query.list();
                map.put("flag", false);
                map.put("picList", list);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
