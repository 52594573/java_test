package com.zm.friendCircle.entity;

import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * ZmFriendCircle entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.zm.friendCircle.entity.ZmFriendCircle
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ZmFriendCircleDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ZmFriendCircleDAO.class);
	// property constants
	public static final String INDEX_ID = "indexId";
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

	public void save(ZmFriendCircle transientInstance) {
		log.debug("saving ZmFriendCircle instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ZmFriendCircle persistentInstance) {
		log.debug("deleting ZmFriendCircle instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public List findByIndexAndIndexId( int index, int indexId ){
		
		try {
			String queryString = "from ZmFriendCircle as model where model."
					+INDEX + "= ? AND model."+INDEX_ID+" = ? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, index);
			queryObject.setParameter(1, indexId);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	public List findByUserIdAndPagenation( int userId, int begin, int length ){
		
		try {
			String queryString = "from ZmFriendCircle where userId=? order by id desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, userId);
			queryObject.setFirstResult(begin);
			queryObject.setMaxResults(length);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

//	public List findByPagenation( int begin, int length ){
//
//		try {
//			String queryString = "from ZmFriendCircle order by id desc";
//			Query queryObject = getCurrentSession().createQuery(queryString);
//			queryObject.setFirstResult(begin);
//			queryObject.setMaxResults(length);
//			return queryObject.list();
//		} catch (RuntimeException re) {
//			log.error("find by property name failed", re);
//			throw re;
//		}
//	}

    public List findByPagenation(int userId, int begin, int length) {

        try {
            String sql = "select fc.id,fc.index_id,fc.t_index,fc.user_id,fc.status,fc.operat_time,fc.circle_del from zm_friend_circle as fc left join zm_friend_circle_shuoshuo as fcs  " +
                    "on (fc.user_id=fcs.user_id and fc.index_id=fcs.id)  where (fc.t_index=3 and fcs.id is not null and (fcs.v_state=1 or fcs.v_state is null)) " +
                    "or (fc.t_index=3 and fcs.id is not null and fcs.v_state=0 and fcs.user_id=" + userId + ") or fc.t_index<>3  order by fc.id desc";
            SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql).addEntity(ZmFriendCircle.class);
            sqlQuery.setFirstResult(begin);
            sqlQuery.setMaxResults(length);
            return sqlQuery.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }


	public List findByPagenation(int index, int userId, int begin, int length) {
		
		try {
			String sql = "select fc.id,fc.index_id,fc.t_index,fc.user_id,fc.status,fc.operat_time,fc.circle_del from zm_friend_circle as fc left join zm_friend_circle_shuoshuo as fcs  " +
					"on (fc.user_id=fcs.user_id and fc.index_id=fcs.id)  where (fc.t_index=3 and fcs.id is not null and (fcs.v_state=1 or fcs.v_state is null)) " +
					"or (fc.t_index=3 and fcs.id is not null and fcs.v_state=0 and fcs.user_id=" + userId + " or fc.status=1) or (fc.t_index<>3 and fc.status=1)  order by fc.id desc";

			//查具体分类 1招工 2找工 3说说 4网络文章
			if (index > 0) {
				if (index == 3) {//说说-为了兼容说说表的v_state字段
					sql = "select fc.id,fc.index_id,fc.t_index,fc.user_id from zm_friend_circle as fc left join zm_friend_circle_shuoshuo as fcs  " +
							"on (fc.user_id=fcs.user_id and fc.index_id=fcs.id)  where fc.t_index=3 and fcs.id is not null and ((fcs.v_state=1 or fcs.v_state is null) " +
							"or (fcs.v_state=0 and fcs.user_id=" + userId + ") or fc.status=1 or (fc.status=0 and fc.user_id=" + userId + "))  order by fc.id desc";
				} else {
					sql = "select fc.id,fc.index_id,fc.t_index,fc.user_id from zm_friend_circle as fc where fc.t_index=" + index + " and (fc.status=1 or  (fc.status=0 and fc.user_id=" + userId + "))  order by fc.id desc";
				}
			}
			SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql).addEntity(ZmFriendCircle.class);
			sqlQuery.setFirstResult(begin);
			sqlQuery.setMaxResults(length);
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	

	public ZmFriendCircle findById(java.lang.Integer id) {
		log.debug("getting ZmFriendCircle instance with id: " + id);
		try {
			ZmFriendCircle instance = (ZmFriendCircle) getCurrentSession().get(
					"com.zm.friendCircle.entity.ZmFriendCircle", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ZmFriendCircle instance) {
		log.debug("finding ZmFriendCircle instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.friendCircle.entity.ZmFriendCircle")
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
		log.debug("finding ZmFriendCircle instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ZmFriendCircle as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByIndexId(Object indexId) {
		return findByProperty(INDEX_ID, indexId);
	}

	public List findByIndex(Object index) {
		return findByProperty(INDEX, index);
	}

	public List findAll() {
		log.debug("finding all ZmFriendCircle instances");
		try {
			String queryString = "from ZmFriendCircle";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ZmFriendCircle merge(ZmFriendCircle detachedInstance) {
		log.debug("merging ZmFriendCircle instance");
		try {
			ZmFriendCircle result = (ZmFriendCircle) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ZmFriendCircle instance) {
		log.debug("attaching dirty ZmFriendCircle instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ZmFriendCircle instance) {
		log.debug("attaching clean ZmFriendCircle instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ZmFriendCircleDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ZmFriendCircleDAO) ctx.getBean("ZmFriendCircleDAO");
	}
}