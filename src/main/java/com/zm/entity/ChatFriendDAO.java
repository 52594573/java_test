package com.zm.entity;

import com.ktp.project.entity.ChatFriend;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * ChatFriend entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zm.entity.ChatFriend
 * @author MyEclipse Persistence Tools
 */


@Transactional
public class ChatFriendDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ChatFriendDAO.class);
	// property constants
	public static final String LEFT_UID = "leftUid";
	public static final String RIGHT_UID = "rightUid";
	public static final String RELATION_TYPE = "relationType";

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

	
	public void update( ChatFriend transientInstance ){
		
		try {
			getCurrentSession().update(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		
	}
	
	
	public void save(ChatFriend transientInstance) {
		log.debug("saving ChatFriend instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ChatFriend persistentInstance) {
		log.debug("deleting ChatFriend instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ChatFriend findById(java.lang.Integer id) {
		log.debug("getting ChatFriend instance with id: " + id);
		try {
			ChatFriend instance = (ChatFriend) getCurrentSession().get(
					"com.zm.entity.ChatFriend", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	


	public List findByExample(ChatFriend instance) {
		log.debug("finding ChatFriend instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.entity.ChatFriend")
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
		log.debug("finding ChatFriend instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ChatFriend as model where model."
					+ propertyName + "= ? order by model.id desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	/**
	 *   根据userId, relationType=99的用户   ->userId作为 toUserId
	 * */
	public List findByUserIdAndRelationType( int fromUserId, int relationType ){
		
		try {
			
			String queryString = "from ChatFriend as model where model.rightUid=? AND model.relationType=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, fromUserId);
			queryObject.setParameter(1, relationType);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 *   根据userId, relationType为2   userId作为 fromUserId
	 * */
	public List findFriendList( int fromUserId, int relationType ){
		
		try {
			
			String queryString = "from ChatFriend as model where model.leftUid=? AND model.relationType=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, fromUserId);
			queryObject.setParameter(1, relationType);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	
	
	
	/**
	 *   根据fromUserId, toUserId来查询是否存在记录，或者relationType是否是不允许被添加
	 * */
	public ChatFriend findByLeftUserIdAndRightUserId( int leftId, int rightId ){
		
		ChatFriend chatFriend = null;
		try {
			
			String queryString = "from ChatFriend as model where model.leftUid=? AND model.rightUid=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, leftId);
			queryObject.setParameter(1, rightId);
			List list =  queryObject.list();
			if( list != null && list.size() > 0 ){
				chatFriend = (ChatFriend)list.get(0);
			}
			return chatFriend;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	
	
	
	

	public List findByLeftUid(Object leftUid) {
		return findByProperty(LEFT_UID, leftUid);
	}


	
	public List findByRightUid(Object rightUid) {
		return findByProperty(RIGHT_UID, rightUid);
	}

	public List findByRelationType(Object relationType) {
		return findByProperty(RELATION_TYPE, relationType);
	}

	public List findAll() {
		log.debug("finding all ChatFriend instances");
		try {
			String queryString = "from ChatFriend";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ChatFriend merge(ChatFriend detachedInstance) {
		log.debug("merging ChatFriend instance");
		try {
			ChatFriend result = (ChatFriend) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ChatFriend instance) {
		log.debug("attaching dirty ChatFriend instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ChatFriend instance) {
		log.debug("attaching clean ChatFriend instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ChatFriendDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ChatFriendDAO) ctx.getBean("ChatFriendDAO");
	}
}