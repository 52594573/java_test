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
 * ProOrgan entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zm.entity.ProOrgan
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class ProOrganDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ProOrganDAO.class);
	// property constants
	public static final String PRO_ID = "proId";
	public static final String PO_NAME = "poName";
	public static final String PO_STATE = "poState";
	public static final String PO_FZR = "poFzr";
	public static final String PO_GZID = "poGzid";
	public static final String PO_GUA = "poGua";
	public static final String PO_ZF = "poZf";

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

	public void save(ProOrgan transientInstance) {
		log.debug("saving ProOrgan instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ProOrgan persistentInstance) {
		log.debug("deleting ProOrgan instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProOrgan findById(java.lang.Integer id) {
		log.debug("getting ProOrgan instance with id: " + id);
		try {
			ProOrgan instance = (ProOrgan) getCurrentSession().get(
					"com.zm.entity.ProOrgan", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ProOrgan instance) {
		log.debug("finding ProOrgan instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.entity.ProOrgan")
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
		log.debug("finding ProOrgan instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ProOrgan as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	public List findBySql( String sql ){
		
		try {
			
			Query queryObject = getCurrentSession().createQuery(sql);
			return queryObject.list();
			
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	

	public List findByProId(Object proId) {
		return findByProperty(PRO_ID, proId);
	}

    public List<ProOrgan> queryOrganProjectId(int projectId, Integer... type) {
        String hql = "from ProOrgan p where p.proId = ? and p.poState in (:ids)";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter(0, projectId);
        query.setParameterList("ids", type);
        return query.list();
    }

	public List findByPoName(Object poName) {
		return findByProperty(PO_NAME, poName);
	}

	public List findByPoState(Object poState) {
		return findByProperty(PO_STATE, poState);
	}

	public List findByPoFzr(Object poFzr) {
		return findByProperty(PO_FZR, poFzr);
	}

	public List findByPoGzid(Object poGzid) {
		return findByProperty(PO_GZID, poGzid);
	}

	public List findByPoGua(Object poGua) {
		return findByProperty(PO_GUA, poGua);
	}

	public List findByPoZf(Object poZf) {
		return findByProperty(PO_ZF, poZf);
	}

	public List findAll() {
		log.debug("finding all ProOrgan instances");
		try {
			String queryString = "from ProOrgan";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ProOrgan merge(ProOrgan detachedInstance) {
		log.debug("merging ProOrgan instance");
		try {
			ProOrgan result = (ProOrgan) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ProOrgan instance) {
		log.debug("attaching dirty ProOrgan instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProOrgan instance) {
		log.debug("attaching clean ProOrgan instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ProOrganDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ProOrganDAO) ctx.getBean("ProOrganDAO");
	}
}