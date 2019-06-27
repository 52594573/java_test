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
 * ZmNetworkArticle entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmNetworkArticle
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmNetworkArticleDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmNetworkArticleDAO.class);
	// property constants
	public static final String CONTENT = "content";
	public static final String URL = "url";
	public static final String USER_ID = "userId";

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

	public void save(ZmNetworkArticle transientInstance) {
		log.debug("saving ZmNetworkArticle instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmNetworkArticle persistentInstance) {
		log.debug("deleting ZmNetworkArticle instance");
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
			
			String sql = "from ZmNetworkArticle  order by id desc";
			Query query = getCurrentSession().createQuery(sql);
			query.setFirstResult(start);
			query.setMaxResults(length);
			return query.list();
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	

	public ZmNetworkArticle findById(java.lang.Integer id) {
		log.debug("getting ZmNetworkArticle instance with id: " + id);
		try {
			ZmNetworkArticle instance = (ZmNetworkArticle) getCurrentSession()
					.get("com.zm.friendCircle.entity.ZmNetworkArticle", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmNetworkArticle instance) {
		log.debug("finding ZmNetworkArticle instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.zm.friendCircle.entity.ZmNetworkArticle")
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
		log.debug("finding ZmNetworkArticle instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmNetworkArticle as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List findByUrl(Object url) {
		return findByProperty(URL, url);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findAll() {
		log.debug("finding all ZmNetworkArticle instances");
		try {
			String queryString = "from ZmNetworkArticle";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmNetworkArticle merge(ZmNetworkArticle detachedInstance) {
		log.debug("merging ZmNetworkArticle instance");
		try {
			ZmNetworkArticle result = (ZmNetworkArticle) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmNetworkArticle instance) {
		log.debug("attaching dirty ZmNetworkArticle instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmNetworkArticle instance) {
		log.debug("attaching clean ZmNetworkArticle instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmNetworkArticleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmNetworkArticleDAO) ctx.getBean("ZmNetworkArticleDAO");
	}
}