package com.ktp.project.dao;

import com.ktp.project.entity.MassageSwitch;
import com.ktp.project.exception.BusinessException;
import com.zm.entity.ProOrganPer;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class RiskWarningDao {
    private static final Logger log = LoggerFactory.getLogger("RiskWarningDao");

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 查询用户在项目中的角色
     */
    public ProOrganPer getRoleId(int userId, int proId) {
        try {
            String sql = "select pop.id id ,pop.p_type PType,pop.pop_type popType from pro_organ_per pop" +
                    " left join pro_organ po on pop.po_id = po.id" +
                    " where pop.user_id = ?" +
                    " and po.pro_id = ? " +
                    " and pop.pop_state = 0" ;

            SQLQuery queryObject = getCurrentSession().createSQLQuery(sql);
            queryObject.setResultTransformer(new AliasToBeanResultTransformer(ProOrganPer.class));
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, proId);
            return (ProOrganPer) queryObject.uniqueResult();

        } catch (RuntimeException re) {
            log.error("查询用户在项目中的角色异常", re);
            throw re;
        }
    }

    /**
     *判断用户是否有初始化数据
     *
     */
    public List getMassageSwitchList(int userId,int proId) {
        try {
            String queryString = "from MassageSwitch ms where ms.mUserId = ?  and ms.mProId = ?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, proId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("查询massage_switch表失败", re);
            throw re;
        }
    }

    /**
     *查询出配置表massager_config中的配置
     *
     */
    public List getMassageConfigList() {
        try {
            String queryString = "from MassageConfig mc where mc.mStatus = 1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("查询出配置表massager_config中的配置失败", re);
            throw re;
        }
    }

    /**
     * 插入风险警告表
     *
     * @param: donate
     * @return:
     */
    public boolean insertMassageSwitch(MassageSwitch massageSwitch) {
        try {
            getCurrentSession().saveOrUpdate(massageSwitch);
            log.debug("提交 massageSwitch successful");
            return true;
        } catch (RuntimeException re) {
            log.error("提交 massageSwitch failed", re);
        }
        return false;
    }

    /**
     *获取预警信息列表
     */
    public List getList(int userId,int proId,int roleId) {
        try {
            String queryString = "from MassageSwitch ms where ms.mUserId = ?  and ms.mProId = ? and ms.mRoleId <= ?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, proId);
            queryObject.setParameter(2, roleId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("查询massage_switch表失败", re);
            throw re;
        }
    }

    /**
     * 活动的数量增加
     *
     * @param: donate
     * @return:
     */
    public int setStatus(int id, int status) {
        try {
            String hql = "update MassageSwitch ms set ms.mStatus = ? where ms.id = ? ";
            Query query = getCurrentSession().createQuery(hql);
            query.setInteger(0, status);
            query.setParameter(1, id);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("修改风险警告设置", re);
            return 0;
        }
    }

    public List<MassageSwitch> listByTypeAndAppId(Integer mTypeId, Integer mAppId){
        String hql = "SELECT m_pro_id AS mProId, m_user_id AS mUserId, m_status AS mStatus " +
                "FROM `massage_switch` " +
                "WHERE (m_status = 0 " +
                " AND m_type_id = ? " +
                " AND m_app_id = ? ) " +
                "GROUP BY m_user_id, m_pro_id";
        try {
            SQLQuery query = getCurrentSession().createSQLQuery(hql);
            query.setInteger(0, mTypeId);
            query.setParameter(1, mAppId);
            query.setResultTransformer(new AliasToBeanResultTransformer(MassageSwitch.class));
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过app标识:%s,类型:%s查询失败", mAppId, mTypeId));
        }
    }

    public Map<Integer, List<Integer>> mappingUserIdWithStatus(Integer mTypeId, Integer mAppId){
        List<MassageSwitch> massageSwitches = listByTypeAndAppId(mTypeId, mAppId);
        Map<Integer, List<Integer>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(massageSwitches)){
//            throw new BusinessException(String.format("通过app标识:%s,类型:%s查不到结果", mAppId, mTypeId));
            return map;
        }
        for (MassageSwitch ms : massageSwitches) {
            Integer proId = ms.getmProId();
            if (proId == null){
                continue;
            }
            if (!map.containsKey(proId)){
                List<Integer> list = new ArrayList<>();
                list.add(ms.getmUserId());
                map.put(proId, list);
            }else {
                List<Integer> list = map.get(proId);
                list.add(ms.getmUserId());
                map.put(proId, list);
            }

        }
        return map;
    }
}
