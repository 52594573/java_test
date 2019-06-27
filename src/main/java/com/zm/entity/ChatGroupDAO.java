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
 * ChatGroup entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zm.entity.ChatGroup
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ChatGroupDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ChatGroupDAO.class);
	// property constants
	public static final String PRO_ORGAN_ID = "proOrganId";
	public static final String NAME = "name";
	public static final String NOTICE = "notice";
	public static final String RECV_FLAG = "recvFlag";
	public static final String TRIBE_TYPE = "tribeType";
	public static final String MASTER_ID = "masterId";

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

	public void save(ChatGroup transientInstance) {
		log.debug("saving ChatGroup instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ChatGroup persistentInstance) {
		log.debug("deleting ChatGroup instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ChatGroup findById(java.lang.Long id) {
		log.debug("getting ChatGroup instance with id: " + id);
		try {
			ChatGroup instance = (ChatGroup) getCurrentSession().get(
					"com.zm.entity.ChatGroup", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ChatGroup instance) {
		log.debug("finding ChatGroup instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.entity.ChatGroup")
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
		log.debug("finding ChatGroup instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ChatGroup as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByProOrganId(Object proOrganId) {
		return findByProperty(PRO_ORGAN_ID, proOrganId);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByNotice(Object notice) {
		return findByProperty(NOTICE, notice);
	}

	public List findByRecvFlag(Object recvFlag) {
		return findByProperty(RECV_FLAG, recvFlag);
	}

	public List findByTribeType(Object tribeType) {
		return findByProperty(TRIBE_TYPE, tribeType);
	}

	public List findByMasterId(Object masterId) {
		return findByProperty(MASTER_ID, masterId);
	}

	public List findAll() {
		log.debug("finding all ChatGroup instances");
		try {
			String queryString = "from ChatGroup";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ChatGroup merge(ChatGroup detachedInstance) {
		log.debug("merging ChatGroup instance");
		try {
			ChatGroup result = (ChatGroup) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ChatGroup instance) {
		log.debug("attaching dirty ChatGroup instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ChatGroup instance) {
		log.debug("attaching clean ChatGroup instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ChatGroupDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ChatGroupDAO) ctx.getBean("ChatGroupDAO");
	}
}