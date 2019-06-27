package com.ktp.project.dao;


import com.google.common.collect.Lists;
import com.ktp.project.dto.ProOrganCountDto;
import com.ktp.project.dto.clockin.UserInfoDto;
import com.ktp.project.dto.im.GroupUserDto;
import com.ktp.project.exception.BusinessException;
import com.zm.entity.ProOrgan;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 组织架构
 * Created by LinHon 2018/11/20
 */
@Repository
@Transactional
public class OrganDao {

    @Autowired
    private QueryChannelDao queryChannelDao;

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    public void save(Object o) {
        getCurrentSession().save(o);
    }

    public void update(Object o) {
        getCurrentSession().update(o);
    }

    public void saveOrUpdate(Object o) {
        getCurrentSession().saveOrUpdate(o);
    }

    public void executeUpdate(String hql, Object... params) {
        getCurrentSession().createQuery(String.format(hql, params)).executeUpdate();
    }

    public void remove(Object o) {
        getCurrentSession().delete(o);
    }

    public ProOrgan queryOne(int primaryKey) {
        return (ProOrgan) getCurrentSession().get(ProOrgan.class, primaryKey);
    }


    /**
     * 根据项目ID查询所有机构
     *
     * @param projectId
     * @param type
     * @return
     */
    public List<ProOrgan> queryManyByProjectId(int projectId, int type) {
        String hql = "from ProOrgan p where p.proId = ? and p.poState = ?";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, projectId);
        query.setParameter(1, type);
        return query.list();
    }

    /**
     * 根据机构id和名称判断是否存在
     *
     * @param
     * @param name
     * @return
     */
    public List<ProOrgan> queryByPoIdAndName(Integer projectId, String name) {
        String hql = "from ProOrgan p where p.proId = ? and p.poName = ?";
        return queryChannelDao.queryMany(hql, Lists.newArrayList(projectId, name), ProOrgan.class);
    }


    /**
     * 取得班组的所有用户信息
     *
     * @param organId
     * @return
     */
    public List<GroupUserDto> queryUsersOfOrgan(int organId) {
        String sql = "select u.id userId , u.u_pic uPic , u.u_sex uSex , u.u_realname uRealname , u.u_nicheng uNicheng , k.key_name popTypeName , u.u_name uName , u.u_cert uCert " +
                "from pro_organ po " +
                "inner join pro_organ_per pop on po.id = pop.po_id " +
                "inner join user_info u on pop.user_id = u.id " +
                "left join key_content k on pop.p_type = k.id " +
                "where po.id = ? and po.po_state = ? and pop.pop_state = ?";
        return queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(organId, 2, 0), GroupUserDto.class);
    }

    public List selectUsersOfOrgan(int organId) {
        String sql = "select u.id , u.u_realname , k.key_name , pop.pop_intime " +
                "from pro_organ po " +
                "inner join pro_organ_per pop on po.id = pop.po_id " +
                "inner join user_info u on pop.user_id = u.id " +
                "left join key_content k on pop.p_type = k.id " +
                "where po.id = ? and po.po_state = ? and pop.pop_state = ?";
        return queryChannelDao.selectMany(sql, Lists.newArrayList(organId, 2, 0));
    }


    /**
     * 取得被代理班组用户列表
     *
     * @param organId
     * @return
     */
    public List<UserInfoDto> queryUsersOfProxyClockIn(int organId, int userId) {
        String sql = "select u.id userId , u.u_pic uPic , u.u_sex uSex , u.u_realname uRealname , u.u_nicheng uNicheng , u.u_name uName , u.u_cert_pic uCertPic , k.key_name popTypeName " +
                "from pro_organ po " +
                "inner join pro_organ_per pop on po.id = pop.po_id " +
                "inner join user_info u on pop.user_id = u.id " +
                "left join key_content k on pop.p_type = k.id " +
                "where po.id = ? and po.po_state = ? and pop.pop_state = ? and pop.user_id != ?";
        return queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(organId, 2, 0, userId), UserInfoDto.class);
    }


    public ProOrgan findByPidAndPoFzr(Integer projectId, Integer userId) {
        ProOrgan result = null;
        try {
            String hql = "from ProOrgan where proId = ? and poFzr = ? ";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameter(0, projectId).setParameter(1, userId);
            result = (ProOrgan) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID: %s, 负责人ID: %s, 查询班组失败!", projectId, userId));
        }
        return result;
    }

    public List<ProOrganCountDto.WorkerInfo> findWorkerInfo(Integer projectId, Integer userId) {
        String sql = "SELECT u.`u_realname` AS userName, u.`u_name` AS phone, u.id AS userId, u.u_sex userSex, " +
                "kc.`key_name` AS gzName, u.`u_star` AS userStar, u.`u_pic` headPhotoUrl " +
                "FROM `pro_organ` po " +
                "LEFT JOIN `key_content` kc ON po.`po_gzid` = kc.`id` " +
                "LEFT JOIN `pro_organ_per` pop ON po.`id` = pop.`po_id` " +
                "LEFT JOIN user_info u ON pop.`user_id` = u.`id` " +
                "WHERE po.`pro_id` = ? " +
                "AND po.`po_fzr` = ? and pop.pop_state = 0 " +
                "GROUP BY userId ";
        List<ProOrganCountDto.WorkerInfo> content = null;
        try {
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProOrganCountDto.WorkerInfo.class));
            sqlQuery.setParameter(0, projectId).setParameter(1, userId);
            content = sqlQuery.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID: %s, 负责人ID: %s, 统计查询班组失败!", projectId, userId));
        }
        return content;
    }


    /**
     * 取得项目的所有用户信息
     *
     * @param projectId
     * @return
     */
    public List<GroupUserDto> queryUsersInfo(int projectId) {
        String hql = "select user_id userId , u_pic uPic , u_sex uSex , u_realname uRealname , u_nicheng uNicheng , key_name popTypeName , u_name uName , u_cert uCert " +
                "from v_pro_organ_per where is_del = ? and pop_state = ? and pro_id = ?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(hql);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(GroupUserDto.class));
        sqlQuery.setParameter(0, 0);
        sqlQuery.setParameter(1, 0);
        sqlQuery.setParameter(2, projectId);
        return sqlQuery.list();
    }


    /**
     * 根据群聊ID查询机构
     *
     * @param groupId
     * @return
     */
    public ProOrgan queryByGroupId(String groupId) {
        String hql = "from ProOrgan p where p.groupId = ?";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, groupId);
        return (ProOrgan) query.uniqueResult();
    }


    /**
     * 根据机构ID查询用户
     *
     * @param organId
     * @return
     */
    public List<Integer> queryUserIdsByOrganId(int organId) {
        String hql = "select p.userId from ProOrganPer p where p.poId = ? and p.popState = ?";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, organId);
        query.setParameter(1, 0);
        return query.list();
    }


    /**
     * 查询我的班组
     *
     * @param projectId
     * @param userId
     * @return
     */
    public ProOrgan queryMyProOrgan(int projectId, int userId) {
        String hql = "select DISTINCT p.id , p.pro_id proId , p.po_name poName , p.po_state poState from pro_organ p " +
                "left join pro_organ_per pop on p.id = pop.po_id " +
                "where p.pro_id = ? and pop.user_id = ? and pop.pop_state = ?";
        return queryChannelDao.selectOneAndTransformer(hql, Lists.newArrayList(projectId, userId, 0), ProOrgan.class);
    }


    public List<ProOrgan> queryOrganProjectId(int projectId, Integer... type) {
        String hql = "from ProOrgan p where p.proId = ? and p.poState in (:ids)";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, projectId);
        query.setParameterList("ids", type);
        return query.list();
    }


    /**
     * 根据项目ID和机构类型查询用户ID
     *
     * @param projectId
     * @param organType
     * @return
     */
    public List<Integer> queryUserIds(int projectId, int organType) {
        String sql = "select pop.user_id from pro_organ po left join pro_organ_per pop on po.id = pop.po_id " +
                "where po.pro_id = ? and po.po_state = ? and pop.pop_state = ?";
        return queryChannelDao.selectMany(sql, Lists.newArrayList(projectId, organType, 0));
    }


    /**
     * 查询项目中的领导成员用户ID
     *
     * @param projectId
     * @param leaderType
     * @return
     */
    public List<Integer> queryLeaderUserIds(int projectId, int leaderType) {
        String sql = "select pop.user_id from pro_organ po left join pro_organ_per pop on po.id = pop.po_id " +
                "where po.pro_id = ? and pop.pop_state = ? and pop.p_type = ?";
        return queryChannelDao.selectMany(sql, Lists.newArrayList(projectId, 0, leaderType));
    }


    /**
     * 查询项目中的所有用户ID
     *
     * @param projectId
     * @return
     */
    public List<Integer> queryUserIdsByProjectId(int projectId) {
        String sql = "select pop.user_id from pro_organ po left join pro_organ_per pop on po.id = pop.po_id " +
                "where po.pro_id = ? and pop.pop_state = ?";
        return queryChannelDao.selectMany(sql, Lists.newArrayList(projectId, 0));
    }
}
