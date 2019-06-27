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
 * ZmFriendCircleShuoshuo entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmFriendCircleShuoshuo
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmFriendCircleShuoshuoDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmFriendCircleShuoshuoDAO.class);
	// property constants
	public static final String USER_ID = "userId";
	public static final String CONTENT = "content";

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

	public void save(ZmFriendCircleShuoshuo transientInstance) {
		log.debug("saving ZmFriendCircleShuoshuo instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmFriendCircleShuoshuo persistentInstance) {
		log.debug("deleting ZmFriendCircleShuoshuo instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	
	/**
	 *  分页查找
	 * */
	public List findByPage(int userId, int start, int length) {

		try {

			String sql = "from com.zm.friendCircle.entity.ZmFriendCircleShuoshuo where (vState=0 and userId=?) or vState=1 order by id desc ";
			Query query = getCurrentSession().createQuery(sql);
			query.setInteger(0, userId);
			query.setFirstResult(start);
			query.setMaxResults(length);

			return query.list();


		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}

	}


	public ZmFriendCircleShuoshuo findById(int userId, java.lang.Integer id) {
		log.debug("getting ZmFriendCircleShuoshuo instance with id: " + id);
		try {
			String queryString = "from ZmFriendCircleShuoshuo where (((vState=0 or vState is null) and userId=?) or vState=1) and id=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, userId);
			queryObject.setParameter(1, id);
			return (ZmFriendCircleShuoshuo) queryObject.uniqueResult();
//			ZmFriendCircleShuoshuo instance = (ZmFriendCircleShuoshuo) getCurrentSession()
//					.get("com.zm.friendCircle.entity.ZmFriendCircleShuoshuo",
//							id);
//			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public ZmFriendCircleShuoshuo findById(java.lang.Integer id) {
		log.debug("getting ZmFriendCircleShuoshuo instance with id: " + id);
		try {
			ZmFriendCircleShuoshuo instance = (ZmFriendCircleShuoshuo) getCurrentSession()
					.get("com.zm.friendCircle.entity.ZmFriendCircleShuoshuo",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmFriendCircleShuoshuo instance) {
		log.debug("finding ZmFriendCircleShuoshuo instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.zm.friendCircle.entity.ZmFriendCircleShuoshuo")
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
		log.debug("finding ZmFriendCircleShuoshuo instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmFriendCircleShuoshuo as model where model."
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

	public List findAll() {
		log.debug("finding all ZmFriendCircleShuoshuo instances");
		try {
			String queryString = "from ZmFriendCircleShuoshuo";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmFriendCircleShuoshuo merge(ZmFriendCircleShuoshuo detachedInstance) {
		log.debug("merging ZmFriendCircleShuoshuo instance");
		try {
			ZmFriendCircleShuoshuo result = (ZmFriendCircleShuoshuo) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmFriendCircleShuoshuo instance) {
		log.debug("attaching dirty ZmFriendCircleShuoshuo instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmFriendCircleShuoshuo instance) {
		log.debug("attaching clean ZmFriendCircleShuoshuo instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmFriendCircleShuoshuoDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmFriendCircleShuoshuoDAO) ctx
				.getBean("ZmFriendCircleShuoshuoDAO");
	}
}