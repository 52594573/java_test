package com.zm.friendCircle.entity;

import java.sql.Timestamp;
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
 * ZmEmployerCircle entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmEmployerCircle
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmEmployerCircleDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmEmployerCircleDAO.class);
	// property constants
	public static final String USER_ID = "userId";
	public static final String CONTENT = "content";
	public static final String WORK_IDS = "workIds";
	public static final String AMOUNT = "amount";
	public static final String ADDRESS = "address";
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

	public void save(ZmEmployerCircle transientInstance) {
		log.debug("saving ZmEmployerCircle instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmEmployerCircle persistentInstance) {
		log.debug("deleting ZmEmployerCircle instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	
	public List findByPagenation( int start, int length ){
		
		try {
			
			String sql = "from ZmEmployerCircle order by id desc";
			Query query = getCurrentSession().createQuery(sql);
			query.setFirstResult(start);
			query.setMaxResults(length);
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	

	public ZmEmployerCircle findById(java.lang.Integer id) {
		log.debug("getting ZmEmployerCircle instance with id: " + id);
		try {
			ZmEmployerCircle instance = (ZmEmployerCircle) getCurrentSession()
					.get("com.zm.friendCircle.entity.ZmEmployerCircle", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmEmployerCircle instance) {
		log.debug("finding ZmEmployerCircle instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.zm.friendCircle.entity.ZmEmployerCircle")
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
		log.debug("finding ZmEmployerCircle instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmEmployerCircle as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List findByWorkIds(Object workIds) {
		return findByProperty(WORK_IDS, workIds);
	}

	public List findByAmount(Object amount) {
		return findByProperty(AMOUNT, amount);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByIndex(Object index) {
		return findByProperty(INDEX, index);
	}

	public List findAll() {
		log.debug("finding all ZmEmployerCircle instances");
		try {
			String queryString = "from ZmEmployerCircle";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmEmployerCircle merge(ZmEmployerCircle detachedInstance) {
		log.debug("merging ZmEmployerCircle instance");
		try {
			ZmEmployerCircle result = (ZmEmployerCircle) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmEmployerCircle instance) {
		log.debug("attaching dirty ZmEmployerCircle instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmEmployerCircle instance) {
		log.debug("attaching clean ZmEmployerCircle instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmEmployerCircleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmEmployerCircleDAO) ctx.getBean("ZmEmployerCircleDAO");
	}
}