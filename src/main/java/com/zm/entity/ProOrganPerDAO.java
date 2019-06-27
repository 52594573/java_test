package com.zm.entity;

import com.ktp.project.dto.AuthRealName.GmOrganInfo;
import com.ktp.project.dto.AuthRealName.GmUserInfo;
import com.ktp.project.entity.KtpProjectInfoEntity;
import com.ktp.project.exception.BusinessException;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * A data access object (DAO) providing persistence and search support for
 * ProOrganPer entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.entity.ProOrganPer
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ProOrganPerDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ProOrganPerDAO.class);
	// property constants
	public static final String PO_ID = "poId";
	public static final String USER_ID = "userId";
	public static final String _PTYPE = "PType";
	public static final String POP_STATE = "popState";
	public static final String POP_TYPE = "popType";
	public static final String POP_JN = "popJn";
	public static final String POP_XY = "popXy";
	public static final String POP_CARD = "popCard";
	public static final String POP_PIC1 = "popPic1";
	public static final String POP_PIC2 = "popPic2";
	public static final String POP_PIC3 = "popPic3";
	public static final String POP_PIC4 = "popPic4";
	public static final String POP_SGZT = "popSgzt";
	public static final String POP_PIC5 = "popPic5";
	public static final String FACE_SIM = "faceSim";
	public static final String ZF_UID = "zfUid";
	public static final String POP_PIC6 = "popPic6";

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(ProOrganPer transientInstance) {
		log.debug("saving ProOrganPer instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ProOrganPer persistentInstance) {
		log.debug("deleting ProOrganPer instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	

	public ProOrganPer findById(java.lang.Integer id) {
		log.debug("getting ProOrganPer instance with id: " + id);
		try {
			ProOrganPer instance = (ProOrganPer) getCurrentSession().get(
					"com.zm.entity.ProOrganPer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	

	public List findByExample(ProOrganPer instance) {
		log.debug("finding ProOrganPer instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.entity.ProOrganPer")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ProOrganPer instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ProOrganPer as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	/** 根据PoId 进行分页查询，每次不能超过100 */
	public List findByPoIdWithPage( int poId, int begin, int length ){
		
		String propertyName = PO_ID;
		try {
			String queryString = "from ProOrganPer as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter( 0, poId );
			queryObject.setFirstResult( begin );
			queryObject.setMaxResults( length );
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	/***
	 *  查询用户所在的项目
	 * */
	public List findByUserIdAndPoType( int userId ){
		
		try {
			String queryString = "select poId from ProOrganPer as model where model."
					+ USER_ID + "= ? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter( 0, userId );
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	

	

	public List findByPoId(Object poId) {
		return findByProperty(PO_ID, poId);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByPType(Object PType) {
		return findByProperty(_PTYPE, PType);
	}

	public List findByPopState(Object popState) {
		return findByProperty(POP_STATE, popState);
	}

	public List findByPopType(Object popType) {
		return findByProperty(POP_TYPE, popType);
	}

	public List findByPopJn(Object popJn) {
		return findByProperty(POP_JN, popJn);
	}

	public List findByPopXy(Object popXy) {
		return findByProperty(POP_XY, popXy);
	}

	public List findByPopCard(Object popCard) {
		return findByProperty(POP_CARD, popCard);
	}

	public List findByPopPic1(Object popPic1) {
		return findByProperty(POP_PIC1, popPic1);
	}

	public List findByPopPic2(Object popPic2) {
		return findByProperty(POP_PIC2, popPic2);
	}

	public List findByPopPic3(Object popPic3) {
		return findByProperty(POP_PIC3, popPic3);
	}

	public List findByPopPic4(Object popPic4) {
		return findByProperty(POP_PIC4, popPic4);
	}

	public List findByPopSgzt(Object popSgzt) {
		return findByProperty(POP_SGZT, popSgzt);
	}

	public List findByPopPic5(Object popPic5) {
		return findByProperty(POP_PIC5, popPic5);
	}

	public List findByFaceSim(Object faceSim) {
		return findByProperty(FACE_SIM, faceSim);
	}

	public List findByZfUid(Object zfUid) {
		return findByProperty(ZF_UID, zfUid);
	}

	public List findByPopPic6(Object popPic6) {
		return findByProperty(POP_PIC6, popPic6);
	}

	public List findAll() {
		log.debug("finding all ProOrganPer instances");
		try {
			String queryString = "from ProOrganPer";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ProOrganPer merge(ProOrganPer detachedInstance) {
		log.debug("merging ProOrganPer instance");
		try {
			ProOrganPer result = (ProOrganPer) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ProOrganPer instance) {
		log.debug("attaching dirty ProOrganPer instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProOrganPer instance) {
		log.debug("attaching clean ProOrganPer instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 检查是否存在
	 *
	 * @param userId  用户id
	 * @param popType 在项目中的职位类型
	 * @return
	 */
	public boolean checkexist(int userId, int popType) {
		try {
			String queryString = "select count(*) from ProOrganPer pop where pop.userId=? and pop.popType=?";
			Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, userId).setParameter(1, popType);
			Long count = (Long) queryObject.uniqueResult();
			return count > 0;
		} catch (RuntimeException re) {
			log.error("select count failed", re);
			return false;
		}
	}

	/**
	 * 查询该用户在此项目的银行卡号
	 *
	 * @param userId
	 * @return
	 */
	public String queryBankId(int userId, int popType, int poId) {
		try {
			String queryString = "select pop.popBankid from ProOrganPer pop where pop.userId=? and pop.popType=? and pop.popState=0 and pop.poId=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, userId);
			queryObject.setParameter(1, popType);
			queryObject.setParameter(2, poId);
			return (String) queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find ProOrganPer failed", re);
			throw re;
		}
	}

	/**
	 * 删除银行卡号
	 *
	 * @param userId  用户id
	 * @param popType 用户类型
	 * @param poId    用户在该项目的角色
	 * @param bankId  银行卡号
	 * @return
	 */
	public int updateBankId(int userId, int popType, int poId, String bankId) {
		try {
			String queryString = "update ProOrganPer pop set pop.popBankid=? where pop.userId=? and pop.popType=? and pop.popState=0 and pop.poId=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, bankId);
			queryObject.setParameter(1, userId);
			queryObject.setParameter(2, popType);
			queryObject.setParameter(3, poId);
			return queryObject.executeUpdate();
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	/**
	 * 在视图v_pro_organ_per里面查找职务
	 *
	 * @param user_id
	 * @return
	 */
	public String findzhiwu(int user_id, int pro_id) {

		String sql = "select zhiwu from v_pro_organ_per where  pop_state=0 and user_id=" + user_id + " and pro_id=" + pro_id;
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
		return (String) sqlQuery.uniqueResult();
	}

	/**
	 * 是否为项目工友
	 *
	 * @return
	 */
	public int isWorkFriend(int fromUserId, int toUserId) {
		try {
			String queryString = "SELECT count(*) FROM v_pro_organ_per as vpop1 , v_pro_organ_per as vpop2 " +
					"where vpop1.user_id=" + fromUserId + " and vpop2.user_id=" + toUserId + " and vpop1.pro_id=vpop2.pro_id";
			SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
			BigInteger bigInteger = (BigInteger) sqlQuery.uniqueResult();
			return bigInteger.intValue();
		} catch (RuntimeException re) {
			log.error("select count failed", re);
			throw re;
		}
	}

	public static ProOrganPerDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ProOrganPerDAO) ctx.getBean("ProOrganPerDAO");
	}

	//根据项目id查询项目经理和生成经理
    public List<Object> getUserList(Integer projectId) {
        try {
            String queryString ="SELECT po.user_id from  pro_organ pro left JOIN\n" +
					"pro_organ_per po on pro.id = po.po_id \n" +
					"where (po.p_type=120 OR po.p_type=118)  AND pro.pro_id="+projectId;
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("select count failed", re);
            throw re;
        }
    }

	/**
	 * 获取用户信息-高明
	 * @param teamId
	 * @return
	 */
	public List<GmUserInfo> getUserInfo(Integer teamId,Integer uerId) {
		try {
			String queryString ="\n" +
					"select  u.id ,u.u_realname,u.u_sfz,u.u_name,u.u_pic,k.key_name ,w.w_resi ,w.w_edu,w.w_gzid, w.w_zs ,po.pop_intime ,po.pop_endtime,po.pop_state  " +
					"  from  pro_organ_per po   LEFT JOIN  user_info  u on u.id=po.user_id \n" +
					"  LEFT JOIN user_work w  on w.u_id=u.id LEFT JOIN key_content k on k.id=w.w_nation " +
					" where  po.po_id="+teamId+" and  po.pop_state=0 and  u.id="+ uerId;
			SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
			sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(GmUserInfo.class));
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("select count failed", re);
			throw re;
		}
	}

	/**
	 * 获取用户信息-高明
	 * @param teamId
	 * @return
	 */
	public GmOrganInfo getGmOrganInfo(Integer teamId) {
		try {
			String queryString ="select  po.id ,po.po_gzid,po.po_fzr  from  pro_organ po " +
					"where po.id="+teamId;
			SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
			sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(GmOrganInfo.class));
			return (GmOrganInfo)sqlQuery.uniqueResult();
		} catch (RuntimeException re) {
			log.error("select count failed", re);
			throw re;
		}
	}
	/**
	 * 根据项目用户查询班组id
	 * @param projectId
	 * @return
	 */
	public int findTeamId(int projectId, int uerId) {
		String sql = "select per.po_id  from  pro_organ po LEFT JOIN   pro_organ_per  per  on  po.id=per.po_id  where po.pro_id=" +projectId
				+" and  per.pop_state = 0 and  per.user_id="+uerId;
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
		return (int) sqlQuery.uniqueResult();
	}

/*	*//**
	 * 在视图v_pro_organ_per里面查找职务
	 *
	 * @param user_id
	 * @return
	 *//*
	public String findpro_organ_per(int user_id, int pro_id) {

		String sql = "select zhiwu from pro_organ_per where user_id=" + user_id + " and pro_id=" + pro_id;
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
		return (String) sqlQuery.uniqueResult();
	}*/


	public ProOrganPer findByUserIdAndPoId(Integer poId, Integer user_id) {
		String hql = "from ProOrganPer where poId = ? and userId = ? and popState = 0 ";
		ProOrganPer one = null;
		try {
			Query query = getCurrentSession().createQuery(hql);
			query.setParameter(0, poId);
			query.setParameter(1, user_id);
			query.setFirstResult(0);
			query.setMaxResults(1);
			one = (ProOrganPer)query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(String.format("通过班组ID:%s,用户ID:%s,查询工人失败", poId, user_id));
		}
		return one;
	}

    public List<ProOrganPer> findByPoId(Integer poId) {
        try {
            String queryString ="select id,user_id userId from pro_organ_per where po_id = ? and pop_state = 0";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
            sqlQuery.setParameter(0, poId);
            sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(ProOrganPer.class));
            return sqlQuery.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过班组ID:%s,查询工人失败", poId));
        }
    }

	public List<Map<String, Object>> getAllUserList(Integer proId) {
		String sql = "SELECT\n" +
				"\tms.m_status,ms.m_user_id\n" +
				"FROM\n" +
				"\tpro_organ_per po\n" +
				"LEFT JOIN pro_organ pro ON pro.id = po.po_id\n" +
				"LEFT JOIN massage_switch ms ON (\n" +
				"\tpo.user_id = ms.m_user_id\n" +
				"\tAND ms.m_type_id = 1\n" +
				")\n" +
				"WHERE\n" +
				"\t(\n" +
				"\t\tpo.p_type = 120\n" +
				"\t\tOR po.p_type = 118\n" +
				"\t)\n" +
				"AND pro.pro_id = ?\n" +
				"AND ms.m_pro_id = ?";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
		sqlQuery.setParameter(0,proId);
		sqlQuery.setParameter(1,proId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

	public int getUser(Integer proId, Integer userId) {
		String sql = "SELECT m_status from massage_switch WHERE m_pro_id=? AND m_type_id =1 AND m_user_id=?";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
		sqlQuery.setParameter(0,proId);
		sqlQuery.setParameter(1,userId);
		Object object = sqlQuery.uniqueResult();
		if(object==null){
			return 1;
		}
		return (int) object;
	}

   /* //查询对应项目下的项目经理，生产经理，班组长
	public List<Object> getUserListAndBan(Integer proId) {
		try {
			String queryString ="SELECT po.user_id from  pro_organ pro INNER JOIN\n" +
					"pro_organ_per po on pro.id = po.po_id \n" +
					"where po.p_type=120 OR po.p_type=118 OR po.p_type=8 AND pro.pro_id = "+proId;
			SQLQuery sqlQuery = getCurrentSession().createSQLQuery(queryString);
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("select count failed", re);
			throw re;
		}
	}*/
}