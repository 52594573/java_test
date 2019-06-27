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
 * ZmFriendCircleAlbum entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmFriendCircleAlbum
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmFriendCircleAlbumDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmFriendCircleAlbumDAO.class);
	// property constants
	public static final String SHUOSHUO_ID = "shuoshuoId";
	public static final String PIC_URL = "picUrl";
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

	public void save(ZmFriendCircleAlbum transientInstance) {
		log.debug("saving ZmFriendCircleAlbum instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmFriendCircleAlbum persistentInstance) {
		log.debug("deleting ZmFriendCircleAlbum instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ZmFriendCircleAlbum findById(java.lang.Integer id) {
		log.debug("getting ZmFriendCircleAlbum instance with id: " + id);
		try {
			ZmFriendCircleAlbum instance = (ZmFriendCircleAlbum) getCurrentSession()
					.get("com.zm.friendCircle.entity.ZmFriendCircleAlbum", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmFriendCircleAlbum instance) {
		log.debug("finding ZmFriendCircleAlbum instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.zm.friendCircle.entity.ZmFriendCircleAlbum")
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
		log.debug("finding ZmFriendCircleAlbum instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmFriendCircleAlbum as model where model."
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
			String queryString = "from ZmFriendCircleAlbum as model where model."
					+ INDEX + "= ? AND model."+SHUOSHUO_ID +" =? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, index);
			queryObject.setParameter(1, indexId);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	

	public List findByShuoshuoId(Object shuoshuoId) {
		return findByProperty(SHUOSHUO_ID, shuoshuoId);
	}

	public List findByPicUrl(Object picUrl) {
		return findByProperty(PIC_URL, picUrl);
	}
	
	
	public List findByUserIdAndPagination( int userId, int length ){
		
		try {
			String queryString = "from ZmFriendCircleAlbum as model where model."
					+ USER_ID + "= ? order by model.id desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, userId);
			queryObject.setFirstResult(0);
			queryObject.setMaxResults(length);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByIndex(Object index) {
		return findByProperty(INDEX, index);
	}

	public List findAll() {
		log.debug("finding all ZmFriendCircleAlbum instances");
		try {
			String queryString = "from ZmFriendCircleAlbum";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmFriendCircleAlbum merge(ZmFriendCircleAlbum detachedInstance) {
		log.debug("merging ZmFriendCircleAlbum instance");
		try {
			ZmFriendCircleAlbum result = (ZmFriendCircleAlbum) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmFriendCircleAlbum instance) {
		log.debug("attaching dirty ZmFriendCircleAlbum instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmFriendCircleAlbum instance) {
		log.debug("attaching clean ZmFriendCircleAlbum instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmFriendCircleAlbumDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmFriendCircleAlbumDAO) ctx.getBean("ZmFriendCircleAlbumDAO");
	}
}