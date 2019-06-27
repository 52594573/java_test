package com.ktp.project.dao;

import com.ktp.project.dto.AdvertiseDto;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-12-28 下午 16:15
 */
@Repository
public class AdvertiseDao {
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    /**
      * 查询广告列表
      *
      * @return java.util.List<com.ktp.project.dto.AdvertiseDto>
      * @params: [index, size]
      * @Author: wuyeming
      * @Date: 2019-01-15 上午 11:33
      */
    public List<AdvertiseDto> getAdvertiseList(int index, int size) {
        String sql = "select  al.al_title title,al.al_images picUrl,al.al_url htmlUrl,al.id id,al.al_state state,'' as content,al.al_intime createTime " +
                "                FROM  ad_list al " +
                "LEFT JOIN ad_sort ads ON ads.id=al.al_sortid  " +
                "WHERE ads.id=7 AND al.is_del<>1  AND al.al_state<>2  ORDER BY al.al_intime";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(AdvertiseDto.class));
        sqlQuery.setFirstResult(index);
        sqlQuery.setMaxResults(size);
        List<AdvertiseDto> list = sqlQuery.list();
        return list;
    }

    public List<AdvertiseDto> getAdvertiseListMy(Integer index, Integer size) {
        String sql = "select  al.al_title title,al.al_images picUrl,al.al_url htmlUrl,al.id id,al.al_state state,'' as content,al.al_intime createTime " +
                "                FROM  ad_list al " +
                "LEFT JOIN ad_sort ads ON ads.id=al.al_sortid  " +
                "WHERE ads.id=6 AND al.is_del<>1  AND al.al_state<>2  ORDER BY al.al_intime";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(AdvertiseDto.class));
        sqlQuery.setMaxResults(size);
        sqlQuery.setFirstResult(index);
        List<AdvertiseDto> list = sqlQuery.list();
        return list;
    }

    public List<AdvertiseDto> listHatchAdvertise() {
        String sql = "select  al.al_title title,al.al_images picUrl,al.al_url htmlUrl,al.id id,al.al_state state,'' as content,al.al_intime createTime " +
                "                FROM  ad_list al " +
                "LEFT JOIN ad_sort ads ON ads.id=al.al_sortid  " +
                "WHERE ads.id=8 AND al.is_del = 0  AND al.al_state = 1  ORDER BY al.al_intime";
        SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(AdvertiseDto.class));
        return sqlQuery.list();
    }
}
