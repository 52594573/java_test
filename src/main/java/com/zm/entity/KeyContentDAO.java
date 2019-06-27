package com.zm.entity;

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
<<<<<<< HEAD:src/main/java/com/zm/entity/KeyContentDAO.java
 * KeyContent entities. Transaction control of the save(), update() and delete()
=======
 * Test110 entities. Transaction control of the saveMallOrder(), update() and delete()
>>>>>>> f01977eed4a926bb76715778f3f0bbe092e3a70a:src/main/java/com/ktp/project/entity/Test110DAO.java
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zm.entity.KeyContent
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class KeyContentDAO {
	private static final Logger log = LoggerFactory
			.getLogger(KeyContentDAO.class);
	// property constants
	public static final String KEY_ID = "keyId";
	public static final String KEY_NAME = "keyName";
	public static final String KEY_VALUE = "keyValue";

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

	public void save(KeyContent transientInstance) {
		log.debug("saving KeyContent instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("saveMallOrder successful");
		} catch (RuntimeException re) {
			log.error("saveMallOrder failed", re);
			throw re;
		}
	}

	public void delete(KeyContent persistentInstance) {
		log.debug("deleting KeyContent instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public KeyContent findById(java.lang.Integer id) {
		log.debug("getting KeyContent instance with id: " + id);
		try {
			KeyContent instance = (KeyContent) getCurrentSession().get(
					"com.zm.entity.KeyContent", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(KeyContent instance) {
		log.debug("finding KeyContent instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.entity.KeyContent")
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
		log.debug("finding KeyContent instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from KeyContent as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByKeyId(Object keyId) {
		return findByProperty(KEY_ID, keyId);
	}

	public List findByKeyName(Object keyName) {
		return findByProperty(KEY_NAME, keyName);
	}

	public List findByKeyValue(Object keyValue) {
		return findByProperty(KEY_VALUE, keyValue);
	}

	public List findAll() {
		log.debug("finding all KeyContent instances");
		try {
			String queryString = "from KeyContent";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public KeyContent merge(KeyContent detachedInstance) {
		log.debug("merging KeyContent instance");
		try {
			KeyContent result = (KeyContent) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(KeyContent instance) {
		log.debug("attaching dirty KeyContent instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(KeyContent instance) {
		log.debug("attaching clean KeyContent instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static KeyContentDAO getFromApplicationContext(ApplicationContext ctx) {
		return (KeyContentDAO) ctx.getBean("KeyContentDAO");
	}
}