package com.ktp.project.dao;

import com.google.common.base.Joiner;
import com.ktp.project.dto.FriendListDto;
import com.zm.entity.ProOrgan;
import com.zm.entity.Project;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by LinHon 2018/8/9
 */
@Repository
public class GroupDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public void saveOrUpdate(ProOrgan proOrgan) {
        getCurrentSession().saveOrUpdate(proOrgan);
    }

    /**
     * 查询群组用户信息
     *
     * @param userIds
     * @return
     */
    public List queryUsersInfo(Integer[] userIds) {
        String sql = "select u.id as user_id , u.u_sex as u_sex , u.u_realname as u_realname , u.u_nicheng as u_nicheng , u.u_pic as u_pic , u.u_cert " +
                "from user_info u where u.id in (:ids) order by FIND_IN_SET(u.id , :sort)";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(FriendListDto.class));
        sqlQuery.setParameterList("ids", userIds);
        sqlQuery.setParameter("sort", Joiner.on(",").join(userIds));
        return sqlQuery.list();
    }

    /**
     * 查询班组
     *
     * @param proOrganId
     * @return
     */
    public ProOrgan queryProOrgan(int proOrganId) {
        return (ProOrgan) getCurrentSession().get(ProOrgan.class, proOrganId);
    }

    /**
     * 查询未创建群的班组
     *
     * @return
     */
    public List queryNotGroup() {
        String hql = "select po from ProOrgan po where groupId is null";
        Query query = getCurrentSession().createQuery(hql);
        return query.list();
    }

    /**
     * 查询班组中的所有成员ID
     *
     * @return
     */
    public List queryUserIdsByProOrganId(int proOrganId) {
        String hql = "select pop.userId from ProOrganPer pop where pop.poId = ?";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, proOrganId);
        return query.list();
    }

    /**
     * 查询项目
     *
     * @param projectId
     * @return
     */
    public Project queryProject(int projectId) {
        return (Project) getCurrentSession().get(Project.class, projectId);
    }

}
