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
 * ZmFriendCircleComment entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmFriendCircleComment
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmFriendCircleCommentDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmFriendCircleCommentDAO.class);
	// property constants
	public static final String INDEX_ID = "indexId";
	public static final String FROM_USER_ID = "fromUserId";
	public static final String TO_USER_ID = "toUserId";
	public static final String COMMENT = "comment";
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

	public void save(ZmFriendCircleComment transientInstance) {
		log.debug("saving ZmFriendCircleComment instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmFriendCircleComment persistentInstance) {
		log.debug("deleting ZmFriendCircleComment instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ZmFriendCircleComment findById(java.lang.Integer id) {
		log.debug("getting ZmFriendCircleComment instance with id: " + id);
		try {
			ZmFriendCircleComment instance = (ZmFriendCircleComment) getCurrentSession()
					.get("com.zm.friendCircle.entity.ZmFriendCircleComment", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmFriendCircleComment instance) {
		log.debug("finding ZmFriendCircleComment instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.zm.friendCircle.entity.ZmFriendCircleComment")
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
		log.debug("finding ZmFriendCircleComment instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmFriendCircleComment as model where model."
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
	public List findByIndexAndIndexId( int index, int indexId ){
		
		try {
			String queryString = "from ZmFriendCircleComment as model where model."
					+ INDEX + "= ? AND model." + INDEX_ID + " =? order by model.id asc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, index);
			queryObject.setParameter(1, indexId);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 *  根据id, index, indexId, fromUserId来查找
	 * */
	public List finByIndexIndexIdFromUserId(  int index, int indexId,int id, int fromUserId ){
		
		try {
			String queryString = "from ZmFriendCircleComment as model where model."
					+ INDEX + "= ? AND model."+INDEX_ID +" = ? AND model.id = ? AND model.fromUserId = ? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, index);
			queryObject.setParameter(1, indexId);
			queryObject.setParameter(2, id);
			queryObject.setParameter(3, fromUserId);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
		
	}
	
	
	
	public List findByIndexId(Object indexId) {
		return findByProperty(INDEX_ID, indexId);
	}

	public List findByFromUserId(Object fromUserId) {
		return findByProperty(FROM_USER_ID, fromUserId);
	}

	public List findByToUserId(Object toUserId) {
		return findByProperty(TO_USER_ID, toUserId);
	}

	public List findByComment(Object comment) {
		return findByProperty(COMMENT, comment);
	}

	public List findByIndex(Object index) {
		return findByProperty(INDEX, index);
	}

	public List findAll() {
		log.debug("finding all ZmFriendCircleComment instances");
		try {
			String queryString = "from ZmFriendCircleComment";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmFriendCircleComment merge(ZmFriendCircleComment detachedInstance) {
		log.debug("merging ZmFriendCircleComment instance");
		try {
			ZmFriendCircleComment result = (ZmFriendCircleComment) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmFriendCircleComment instance) {
		log.debug("attaching dirty ZmFriendCircleComment instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmFriendCircleComment instance) {
		log.debug("attaching clean ZmFriendCircleComment instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmFriendCircleCommentDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmFriendCircleCommentDAO) ctx
				.getBean("ZmFriendCircleCommentDAO");
	}
}