package com.zm.friendCircle.entity;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * ZmRefusedLook entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmRefusedLook
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmRefusedLookDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmRefusedLookDAO.class);
	// property constants
	public static final String FROM_USER_ID = "fromUserId";
	public static final String INDEX = "index";
	public static final String INDEX_ID = "indexId";
	public static final String TO_USER_ID = "toUserId";

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

	public void save(ZmRefusedLook transientInstance) {
		log.debug("saving ZmRefusedLook instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmRefusedLook persistentInstance) {
		log.debug("deleting ZmRefusedLook instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ZmRefusedLook findById(java.lang.Integer id) {
		log.debug("getting ZmRefusedLook instance with id: " + id);
		try {
			ZmRefusedLook instance = (ZmRefusedLook) getCurrentSession().get(
					"com.zm.friendCircle.entity.ZmRefusedLook", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmRefusedLook instance) {
		log.debug("finding ZmRefusedLook instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.friendCircle.entity.ZmRefusedLook")
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
		log.debug("finding ZmRefusedLook instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmRefusedLook as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	
	
	public List findByFromUserIdAndToUserId( int fromUserId, int toUserId ){
		
		try {
			String queryString = "from ZmRefusedLook as model where model."
					+ FROM_USER_ID + "= ? AND model."+TO_USER_ID+" = ? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, fromUserId);
			queryObject.setParameter(1, toUserId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	public List findByFromUserIdAndIndex( int  fromUserId, int index, int indexId ){
		
		try {
			String queryString = "from ZmRefusedLook as model where model."
					+ FROM_USER_ID + "= ? AND model."+INDEX+" = ? AND model."+INDEX_ID+" =? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, fromUserId);
			queryObject.setParameter(1, index);
			queryObject.setParameter(2, indexId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	
	public List findByFromUserId(Object fromUserId) {
		return findByProperty(FROM_USER_ID, fromUserId);
	}

	public List findByIndex(Object index) {
		return findByProperty(INDEX, index);
	}

	public List findByIndexId(Object indexId) {
		return findByProperty(INDEX_ID, indexId);
	}

	public List findByToUserId(Object toUserId) {
		return findByProperty(TO_USER_ID, toUserId);
	}
	

	
	

	public List findAll() {
		log.debug("finding all ZmRefusedLook instances");
		try {
			String queryString = "from ZmRefusedLook";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmRefusedLook merge(ZmRefusedLook detachedInstance) {
		log.debug("merging ZmRefusedLook instance");
		try {
			ZmRefusedLook result = (ZmRefusedLook) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmRefusedLook instance) {
		log.debug("attaching dirty ZmRefusedLook instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmRefusedLook instance) {
		log.debug("attaching clean ZmRefusedLook instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmRefusedLookDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmRefusedLookDAO) ctx.getBean("ZmRefusedLookDAO");
	}
}