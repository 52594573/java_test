package com.zm.friendCircle.entity;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * ZmFriendCircleLike entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmFriendCircleLike
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmFriendCircleLikeDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmFriendCircleLikeDAO.class);
	// property constants
	public static final String INDEX_ID = "indexId";
	public static final String USER_ID = "userId";
	public static final String INDEX = "index";

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

	public void save(ZmFriendCircleLike transientInstance) {
		log.debug("saving ZmFriendCircleLike instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmFriendCircleLike persistentInstance) {
		log.debug("deleting ZmFriendCircleLike instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ZmFriendCircleLike findById(java.lang.Integer id) {
		log.debug("getting ZmFriendCircleLike instance with id: " + id);
		try {
			ZmFriendCircleLike instance = (ZmFriendCircleLike) getCurrentSession()
					.get("com.zm.friendCircle.entity.ZmFriendCircleLike", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmFriendCircleLike instance) {
		log.debug("finding ZmFriendCircleLike instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.zm.friendCircle.entity.ZmFriendCircleLike")
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
		log.debug("finding ZmFriendCircleLike instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmFriendCircleLike as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 *  查找indexId.index
	 * */
	public List findByIndexAndIndexIdAndUserId( int index, int indexId,int userId ){
		
		try {
			String queryString = "from ZmFriendCircleLike as model where model."
					+ INDEX + "= ? AND model."+INDEX_ID +" =? AND model."+USER_ID+" = ? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, index);
			queryObject.setParameter(1, indexId);
			queryObject.setParameter(2, userId);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	public List findByIndexAndIndexId( int index, int indexId ){
		
		try {
			String queryString = "from ZmFriendCircleLike as model where model."
					+ INDEX + "= ? AND model." + INDEX_ID + " =?  order by model.id asc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, index);
			queryObject.setParameter(1, indexId);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	

	public List findByIndexId(Object indexId) {
		return findByProperty(INDEX_ID, indexId);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByIndex(Object index) {
		return findByProperty(INDEX, index);
	}

	public List findAll() {
		log.debug("finding all ZmFriendCircleLike instances");
		try {
			String queryString = "from ZmFriendCircleLike";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmFriendCircleLike merge(ZmFriendCircleLike detachedInstance) {
		log.debug("merging ZmFriendCircleLike instance");
		try {
			ZmFriendCircleLike result = (ZmFriendCircleLike) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmFriendCircleLike instance) {
		log.debug("attaching dirty ZmFriendCircleLike instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmFriendCircleLike instance) {
		log.debug("attaching clean ZmFriendCircleLike instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmFriendCircleLikeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmFriendCircleLikeDAO) ctx.getBean("ZmFriendCircleLikeDAO");
	}
}