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
 * UserSfz entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.zm.entity.UserSfz
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class UserSfzDAO {
	private static final Logger log = LoggerFactory.getLogger(UserSfzDAO.class);
	// property constants
	public static final String _UID = "UId";
	public static final String US_NAME = "usName";
	public static final String US_SEX = "usSex";
	public static final String US_AGE = "usAge";
	public static final String US_PIC = "usPic";
	public static final String US_NATION = "usNation";
	public static final String US_ADDRESS = "usAddress";
	public static final String US_PROVINCE = "usProvince";
	public static final String US_CITY = "usCity";
	public static final String US_AREA = "usArea";
	public static final String US_ORG = "usOrg";
	public static final String US_START_TIME = "usStartTime";
	public static final String US_EXPIRE_TIME = "usExpireTime";
	public static final String US_BIRTH_YEAR = "usBirthYear";
	public static final String US_BIRTH_MONTH = "usBirthMonth";
	public static final String US_BIRTH_DAY = "usBirthDay";

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

	public void save(UserSfz transientInstance) {
		log.debug("saving UserSfz instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UserSfz persistentInstance) {
		log.debug("deleting UserSfz instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserSfz findById(java.lang.Integer id) {
		log.debug("getting UserSfz instance with id: " + id);
		try {
			UserSfz instance = (UserSfz) getCurrentSession().get(
					"com.zm.entity.UserSfz", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserSfz instance) {
		log.debug("finding UserSfz instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.zm.entity.UserSfz")
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
		log.debug("finding UserSfz instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserSfz as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUId(Object UId) {
		return findByProperty(_UID, UId);
	}

	public List findByUsName(Object usName) {
		return findByProperty(US_NAME, usName);
	}

	public List findByUsSex(Object usSex) {
		return findByProperty(US_SEX, usSex);
	}

	public List findByUsAge(Object usAge) {
		return findByProperty(US_AGE, usAge);
	}

	public List findByUsPic(Object usPic) {
		return findByProperty(US_PIC, usPic);
	}

	public List findByUsNation(Object usNation) {
		return findByProperty(US_NATION, usNation);
	}

	public List findByUsAddress(Object usAddress) {
		return findByProperty(US_ADDRESS, usAddress);
	}

	public List findByUsProvince(Object usProvince) {
		return findByProperty(US_PROVINCE, usProvince);
	}

	public List findByUsCity(Object usCity) {
		return findByProperty(US_CITY, usCity);
	}

	public List findByUsArea(Object usArea) {
		return findByProperty(US_AREA, usArea);
	}

	public List findByUsOrg(Object usOrg) {
		return findByProperty(US_ORG, usOrg);
	}

	public List findByUsStartTime(Object usStartTime) {
		return findByProperty(US_START_TIME, usStartTime);
	}

	public List findByUsExpireTime(Object usExpireTime) {
		return findByProperty(US_EXPIRE_TIME, usExpireTime);
	}

	public List findByUsBirthYear(Object usBirthYear) {
		return findByProperty(US_BIRTH_YEAR, usBirthYear);
	}

	public List findByUsBirthMonth(Object usBirthMonth) {
		return findByProperty(US_BIRTH_MONTH, usBirthMonth);
	}

	public List findByUsBirthDay(Object usBirthDay) {
		return findByProperty(US_BIRTH_DAY, usBirthDay);
	}

	public List findAll() {
		log.debug("finding all UserSfz instances");
		try {
			String queryString = "from UserSfz";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserSfz merge(UserSfz detachedInstance) {
		log.debug("merging UserSfz instance");
		try {
			UserSfz result = (UserSfz) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserSfz instance) {
		log.debug("attaching dirty UserSfz instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserSfz instance) {
		log.debug("attaching clean UserSfz instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static UserSfzDAO getFromApplicationContext(ApplicationContext ctx) {
		return (UserSfzDAO) ctx.getBean("UserSfzDAO");
	}
}