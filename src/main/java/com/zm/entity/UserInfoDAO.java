package com.zm.entity;

import com.ktp.project.entity.Relation;
import com.ktp.project.util.DateUtil;
import org.apache.http.util.TextUtils;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for
 * UserInfo entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 *
 * @author MyEclipse Persistence Tools
 * @see com.zm.entity.UserInfo
 */


@Transactional
public class UserInfoDAO {
    private static final Logger log = LoggerFactory
            .getLogger(UserInfoDAO.class);
    // property constants
    public static final String _UNAME = "u_name";
    public static final String _UPIC = "u_pic";
    public static final String _USFZ = "u_sfz";
    public static final String _UREALNAME = "u_realname";
    public static final String _UTYPE = "u_type";
    public static final String _USTATE = "u_state";
    public static final String _UCERT = "u_cert";
    public static final String _USEX = "u_sex";
    public static final String _UPSW = "u_psw";
    public static final String _UPROID = "u_proid";
    public static final String _UUP_PROID = "u_u_proid";
    public static final String _UXYF = "u_xyf";
    public static final String _UJNF = "u_jnf";
    public static final String _USFZPIC = "u_sfzpic";
    public static final String _UYHKPIC = "u_yhkpic";
    public static final String _ULBS_X = "u_lbs_x";
    public static final String _ULBS_Y = "u_lbs_y";
    public static final String _ULBS = "u_lbs";


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


    /**
     * 获取籍贯   表user_sfz
     */
    public String findUserSfzInfo(int user_id) {

        String sql = "select us_city from user_sfz where u_id=" + user_id;
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        String name = (String) sqlQuery.uniqueResult();
        return name;
    }


    //分页查询
    public List findByPage(int page, int pageNumber) {

        int start = page * pageNumber;
        try {

            String queryString = "from UserInfo";
            Query queryObject = getCurrentSession().createQuery(queryString); //打开或者获取一个新的session
            queryObject.setFirstResult(start); //设置起始位置
            queryObject.setMaxResults(pageNumber); //设置每次分页的长度
            return queryObject.list();

        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }


    public void save(UserInfo transientInstance) {
        log.debug("saving UserInfo instance");
        try {
            getCurrentSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(UserInfo persistentInstance) {
        log.debug("deleting UserInfo instance");
        try {
            getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public void update(UserInfo persistentInstance) {

        try {
            getCurrentSession().update(persistentInstance);

        } catch (RuntimeException re) {

            throw re;
        }
    }


    public UserInfo findById(java.lang.Integer id) {
        log.debug("getting UserInfo instance with id: " + id);
        try {
            UserInfo instance = (UserInfo) getCurrentSession().get("com.zm.entity.UserInfo", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }

    public List<UserInfo> queryUserInfobyphone(String u_name) {

        try {
            String queryString = "from UserInfo as model where model.u_name =?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, u_name);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByExample(UserInfo instance) {
        log.debug("finding UserInfo instance by example");
        try {
            List results = getCurrentSession()
                    .createCriteria("com.zm.entity.UserInfo")
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
        log.debug("finding UserInfo instance with property: " + propertyName
                + ", value: " + value);
        try {
            String queryString = "from UserInfo as model where model."
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
     * 通过手机号码进行模糊搜索
     */
    public List findByPrefUName(Object value) {

        try {
            String queryString = "from UserInfo as model where model.u_name LIKE ?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, value + "%");
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByUName(Object UName) {
        return findByProperty(_UNAME, UName);
    }

    public List findByUPic(Object UPic) {
        return findByProperty(_UPIC, UPic);
    }

    public List findByUSfz(Object USfz) {
        return findByProperty(_USFZ, USfz);
    }

    public List findByURealname(Object URealname) {
        return findByProperty(_UREALNAME, URealname);
    }

    public List findByUType(Object UType) {
        return findByProperty(_UTYPE, UType);
    }

    public List findByUState(Object UState) {
        return findByProperty(_USTATE, UState);
    }

    public List findByUCert(Object UCert) {
        return findByProperty(_UCERT, UCert);
    }

    public List findByUSex(Object USex) {
        return findByProperty(_USEX, USex);
    }

    public List findByUPsw(Object UPsw) {
        return findByProperty(_UPSW, UPsw);
    }

    public List findByUProid(Object UProid) {
        return findByProperty(_UPROID, UProid);
    }

    public List findByUUpProid(Object UUpProid) {
        return findByProperty(_UUP_PROID, UUpProid);
    }

    public List findByUXyf(Object UXyf) {
        return findByProperty(_UXYF, UXyf);
    }

    public List findByUJnf(Object UJnf) {
        return findByProperty(_UJNF, UJnf);
    }

    public List findByUSfzpic(Object USfzpic) {
        return findByProperty(_USFZPIC, USfzpic);
    }

    public List findByUYhkpic(Object UYhkpic) {
        return findByProperty(_UYHKPIC, UYhkpic);
    }

    public List findByULbsX(Object ULbsX) {
        return findByProperty(_ULBS_X, ULbsX);
    }

    public List findByULbsY(Object ULbsY) {
        return findByProperty(_ULBS_Y, ULbsY);
    }

    public List findByULbs(Object ULbs) {
        return findByProperty(_ULBS, ULbs);
    }

    public List findAll() {
        log.debug("finding all UserInfo instances");
        try {
            String queryString = "from UserInfo";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List findAllWithPagenation(int startPage, int length) {

        try {
            String queryString = "from UserInfo";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setFirstResult(startPage);
            queryObject.setMaxResults(length);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }


    public List findAllWithDistance(int userId, double left, double right, double top, double bottom) {

        try {

            String queryString = "from UserInfo where u_lbs_x > ? AND u_lbs_x < ? AND u_lbs_y > ? AND u_lbs_y < ? AND id <> ?";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, left);
            queryObject.setParameter(1, right);
            queryObject.setParameter(2, top);
            queryObject.setParameter(3, bottom);
            queryObject.setParameter(4, userId);
            queryObject.setFirstResult(0);
            queryObject.setMaxResults(200);

            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List getNearUserList(int userId, double left, double right, double top, double bottom, String startYear, String endYear, int sex, String areas) {
        try {
            String yearQueryString = (!TextUtils.isEmpty(startYear) && !TextUtils.isEmpty(endYear)) ? " and (ui.u_birthday between :startDate and :endDate) " : "";
            String sexQueryString = sex == 1 || sex == 2 ? " and  ui.u_sex=? " : "";
            String[] areaArr = !TextUtils.isEmpty(areas) ? areas.split(",") : null;
            StringBuilder areaBuilder = null;
            if (areaArr != null && areaArr.length > 0) {
                areaBuilder = new StringBuilder();
                areaBuilder.append(" and (");
                for (int i = 0; i < areaArr.length; i++) {
                    String area = areaArr[i];
                    if (i > 0) {
                        areaBuilder.append(" or ");
                    }
                    areaBuilder.append(" us.address like '%").append(area).append("%' ");
                }
                areaBuilder.append(")");
            }
            String areaQueryString = areaBuilder != null ? areaBuilder.toString() : "";

            String queryString = "select new com.ktp.project.entity.UserNear(ui.id,ui.u_realname,ui.u_nicheng,ui.u_pic,ui.u_sex,ui.u_cert,us.city,ua.lat,ua.lon,ui.u_star,ui.u_proid,ua.lasttime) " +
                    "from UserInfo as ui,UserSfz as us,UserActive as ua where ua.lat > ? AND ua.lat < ? AND ua.lon > ? AND ua.lon < ? AND ui.id <> ? AND ui.u_isfujin<>2"
                    + " and ui.id=us.userId and ui.id=ua.userId " + sexQueryString + yearQueryString + areaQueryString;
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, left);
            queryObject.setParameter(1, right);
            queryObject.setParameter(2, top);
            queryObject.setParameter(3, bottom);
            queryObject.setParameter(4, userId);
            if (!TextUtils.isEmpty(sexQueryString)) {
                queryObject.setParameter(5, sex);
            }
            if (!TextUtils.isEmpty(yearQueryString)) {//如果不为空增加两个条件
                queryObject.setParameter("startDate", DateUtil.getFormatDate(startYear, "yyyy-MM-dd"));
                queryObject.setParameter("endDate", DateUtil.getFormatDate(endYear, "yyyy-MM-dd"));
            }
            queryObject.setFirstResult(0);
            queryObject.setMaxResults(200);

            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public UserInfo merge(UserInfo detachedInstance) {
        log.debug("merging UserInfo instance");
        try {
            UserInfo result = (UserInfo) getCurrentSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(UserInfo instance) {
        log.debug("attaching dirty UserInfo instance");
        try {
            getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(UserInfo instance) {
        log.debug("attaching clean UserInfo instance");
        try {
            getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
                    instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    /**
     * 根据userId切换身份
     *
     * @param userId     用户id
     * @param changeType 切换身份
     * @return
     */
    public int updateUserInfoType(int userId, int changeType) {
        try {
            String queryString = "update UserInfo ui set ui.u_type=? where ui.id=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setInteger(0, changeType);
            query.setInteger(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update user_info fail", re);
            return -1;
        }
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @return
     */
    public boolean updateUserInfo(UserInfo userInfo) {
        log.debug("updateing UserInfo instance");
        try {
            getCurrentSession().update(userInfo);
            log.debug("update UserInfo successful");
            return true;
        } catch (RuntimeException re) {
            log.error("update UserInfo failed", re);
        }
        return false;
    }

    public static UserInfoDAO getFromApplicationContext(ApplicationContext ctx) {
        return (UserInfoDAO) ctx.getBean("UserInfoDAO");
    }

    /**
     * 查新用户关系信息
     *
     * @param userId
     * @return
     */
    public Relation queryRelation(int userId) {
        try {
            String hql = "select u.u_pic as u_pic , u.u_sex as u_sex , u.u_realname as u_realname , u.u_nicheng as u_nicheng from UserInfo u where u.id = ?";
            Query query = getCurrentSession().createQuery(hql);
            query.setResultTransformer(new AliasToBeanResultTransformer(Relation.class));
            query.setParameter(0, userId);
            return (Relation) query.uniqueResult();
        } catch (RuntimeException e) {
            log.error("merge failed", e);
            throw e;
        }
    }

    public void updateByInitTime() {
        String sql = "UPDATE user_info SET u_authentication='2018-12-31' WHERE u_intime<'2018-12-31' AND u_cert=2";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.executeUpdate();
    }

    public void updateByUserId(Integer user_id, String new_pws) {
        String sql = "UPDATE user_info SET u_psw=? WHERE id=?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, new_pws);
        sqlQuery.setParameter(1, user_id);
        sqlQuery.executeUpdate();
    }

    public void insertCcbPush(CcbPush ccbPush) {
        log.debug("saving CcbPush instance");
        try {
            getCurrentSession().save(ccbPush);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public String getCcid(Integer user_id) {
        String sql = "SELECT ccbid from ccbpush WHERE uid=?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, user_id);
        return (String) sqlQuery.uniqueResult();
    }

    public int updateCcbPush(Integer user_id, String code) {
        String sql = "UPDATE ccbpush SET ccbid=? where uid=? ";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, code);
        sqlQuery.setParameter(1, user_id);
        return sqlQuery.executeUpdate();
    }

    public CcbPush getCcbPush(Integer user_id) {
        String sql = "SELECT id,uid,ccbid,intime initime from ccbpush WHERE uid=?";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, user_id);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(CcbPush.class));
        return (CcbPush) sqlQuery.uniqueResult();
    }

    public List<UserInfo> queryUserInfoBy(String idCardNo,Integer u_id) {
        String sql = "SELECT u_name,u_sfz FROM user_info WHERE u_sfz=? and id != ? ";
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sql);
        sqlQuery.setParameter(0, idCardNo);
        sqlQuery.setParameter(1, u_id);
        sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(UserInfo.class));
        return  sqlQuery.list();
    }
}