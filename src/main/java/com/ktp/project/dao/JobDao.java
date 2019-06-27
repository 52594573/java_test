package com.ktp.project.dao;

import com.ktp.project.entity.JobApply;
import com.ktp.project.entity.JobList;
import com.ktp.project.entity.JobLooking;
import com.ktp.project.util.PageUtil;
import org.apache.http.util.TextUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class JobDao {

    private static final Logger log = LoggerFactory.getLogger("JobDao");

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 保存一条招聘信息
     *
     * @param transientInstance
     */
    public boolean saveOrUpdateJob(JobList transientInstance) {
        log.debug("save JobList instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save JobList successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save JobList failed", re);
        }
        return false;
    }

    /**
     * 获取我的招聘信息列表
     *
     * @return
     */
    public List queryMyJobList(int pubUid, int jobType, int jobObj, int page, int pageSize) {
        try {
            String queryString = "from JobList jl where jl.pubUid=? and jl.jobType=? and jl.jobObj=? and jl.jobState<>2 and jl.isDel<>1 order by jl.id desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, pubUid);
            queryObject.setParameter(1, jobType);
            queryObject.setParameter(2, jobObj);
            queryObject.setFirstResult(PageUtil.getFirstResult(page, pageSize));
            queryObject.setMaxResults(pageSize);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all joblooking failed", re);
            throw re;
        }
    }

    /**
     * 获取招聘信息列表
     *
     * @param jobType 招聘类型1项目部招聘 2班组长招聘
     * @param jobObj 招聘对象 4招聘工人 8招聘班组长
     * @param orderby 排序 0默认 1时间 2距离
     * @param sort    排序1升序 asc 2降序desc
     * @param apply   是否已申请 0默认 1已申请 2未申请
     * @param gzIds   工种ids，逗号分隔
     * @return
     */
    public List queryJobList(int userId, int jobType, int jobObj, int orderby, int sort, int apply, List<Integer> gzIds, String experience, String teamsize, String area, int page, int pageSize) {
        boolean haveJobType = jobType > 0;
        boolean havaJobObj = jobObj > 0;
        boolean haveGz = gzIds != null && !gzIds.isEmpty();
        boolean haveExperience = !TextUtils.isEmpty(experience);
        boolean haveTeamSize = !TextUtils.isEmpty(teamsize);
        boolean haveArea = !TextUtils.isEmpty(area);
        boolean haveApply = apply == 1 || apply == 2;
        List applyIdList = null;
        if (haveApply) {
            applyIdList = queryJobIdList(userId);
        }
        int key = -1;
        try {
            String queryString = "select new com.ktp.project.entity.JobList(jl.id,jl.proId,jl.pubUid,jl.jobState,jl.jobArea,jl.jobAddress,jl.jobPosition,jl.jobContent,jl.jobMoney,jl.jobNumber" +
                    ",jl.jobProname,jl.jobEndtime,jl.jobIntime,jl.jobTel,jl.isDel,jl.jobFzr,jl.jobGzid,jl.jobGzname,jl.jobObj,jl.jobAddw,jl.jobAddh,jl.jobLocation,jl.jobType,jl.jobProtime,jl.jobExperience" +
                    ",jl.jobTeamsize,ui.u_name,ui.u_realname) " +
                    "from JobList as jl,UserInfo as ui where jl.jobState<>2 and jl.jobState<>3 and jl.isDel<>1 and jl.pubUid=ui.id " +
                    (haveJobType ? " and jl.jobType=? " : "") +
                    (havaJobObj ? " and jl.jobObj=? " : "") +
                    (haveExperience ? " and jl.jobExperience=? " : "") +
                    (haveTeamSize ? " and jl.jobTeamsize=? " : "") +
                    (haveGz ? " and jl.jobGzid in (:gzIds) " : "") +
                    (haveApply ? (apply == 1 ? " and jl.id in (:applyIdList) " : " and jl.id not in (:applyIdList) ") : "") +
                    (haveArea ? (" and jl.jobAddress like :area ") : "")
                    + "  order by " + (orderby == 1 ? "jl.jobIntime" : "jl.id")
                    + (sort == 1 ? " asc" : " desc");
            Query queryObject = getCurrentSession().createQuery(queryString);
            if (haveJobType) {
                key++;
                queryObject.setParameter(key, jobType);
            }
            if (havaJobObj) {
                key++;
                queryObject.setParameter(key, jobObj);
            }
            if (haveExperience) {
                key++;
                queryObject.setParameter(key, experience);
            }
            if (haveTeamSize) {
                key++;
                queryObject.setParameter(key, teamsize);
            }
            if (haveGz) {
                queryObject.setParameterList("gzIds", gzIds);
            }
            if (haveApply) {
                queryObject.setParameterList("applyIdList", applyIdList != null ? applyIdList : new ArrayList());
            }
            if (haveArea) {
                queryObject.setString("area", "%" + area + "%");
            }
            queryObject.setFirstResult(PageUtil.getFirstResult(page, pageSize));
            queryObject.setMaxResults(pageSize);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all joblooking failed", re);
            throw re;
        }
    }

    /**
     * 查询招聘信息详情
     *
     * @return
     */
    public JobList queryJobDetail(int jobId) {
        try {
            String queryString = "from JobList jl where jl.id=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, jobId);
            return (JobList) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find failed", re);
            return null;
        }
    }

    /**
     * 停用该招聘
     *
     * @param jobId 招聘id
     * @return
     */
    public int stopUsing(int userId, int jobId) {
        try {
            String queryString = "update JobList jl set jl.jobState=3 where jl.id=? and jl.pubUid=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setParameter(0, jobId);
            query.setParameter(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update JobList fail", re);
            return -1;
        }
    }

    /**
     * 软删除该招聘
     *
     * @param jobId 招聘id
     * @return
     */
    public int delete(int userId, int jobId) {
        try {
            String queryString = "update JobList jl set jl.isDel=1 where jl.id=? and jl.pubUid=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setParameter(0, jobId);
            query.setParameter(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update JobList fail", re);
            return -1;
        }
    }

    /**
     * 保存一条找工信息
     *
     * @param transientInstance
     */
    public boolean saveJobLooking(JobLooking transientInstance) {
        log.debug("save JobLooking instance");
        try {
            getCurrentSession().saveOrUpdate(transientInstance);
            log.debug("save JobLooking successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save JobLooking failed", re);
        }
        return false;
    }

    /**
     * 获取我的招工信息列表
     *
     * @return
     */
    public List queryMyJobLooking(int pubUid, int pubType, int page, int pageSize) {
        try {
            boolean havePubType = pubType == 8 || pubType == 4;
            String queryString = "from JobLooking jl where jl.pubUid=? " + (havePubType ? "and jl.pubType=? " : "") + "and jl.pubState<>1 order by jl.id desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, pubUid);
            if (havePubType) {
                queryObject.setParameter(1, pubType);
            }
            queryObject.setFirstResult(PageUtil.getFirstResult(page, pageSize));
            queryObject.setMaxResults(pageSize);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all joblooking failed", re);
            throw re;
        }
    }

    /**
     * 获取工人或班组长招工信息列表
     *
     * @param pubType 找工类型1.班组找工  2工人找工
     * @param orderby 0默认 1时间 2工龄 3评分 4规模
     * @param sort    1升序asc  默认降序desc
     * @return
     */
    public List queryJobLooking(int pubType, int orderby, int sort, List<Integer> gzIdList, String experience, String teamsize, String city, int page, int pageSize) {
        boolean haveGz = gzIdList != null && !gzIdList.isEmpty();
        boolean haveExperience = !TextUtils.isEmpty(experience);
        boolean haveTeamSize = !TextUtils.isEmpty(teamsize);
        boolean haveCity = !TextUtils.isEmpty(city);
        try {
            int key = 0;
            String queryString = "select new com.ktp.project.entity.JobLooking(jl.id,jl.pubUid,jl.pubMobile,jl.pubType,jl.gzId,jl.gzName,jl.workTime,jl.city,jl.teamSize,jl.experience,jl.workAge,jl.content,jl.createTime,ui.u_jnf,ui.u_realname,ui.u_pic,ui.u_sex,ui.u_star) " +
                    "from JobLooking as jl,UserInfo as ui where jl.pubType=? and jl.pubState<>1 and jl.pubState<>2 and jl.pubUid=ui.id " +
                    (haveGz ? " and jl.gzId in (:gzIds) " : "") +
                    (haveExperience ? " and jl.experience=? " : "") +
                    (haveTeamSize ? " and jl.teamSize=? " : "") +
                    (haveCity ? (" and jl.city like :city ") : "") +
                    "order by " + (orderby == 1 ? "jl.createTime" : (orderby == 2 ? "jl.workAge" : (orderby == 3 ? "ui.u_star" : (orderby == 4 ? "jl.teamSize" : "jl.id"))))
                    + (sort == 1 ? " asc" : " desc");
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(key, pubType);
            if (haveGz) {
                queryObject.setParameterList("gzIds", gzIdList);
            }
            if (haveTeamSize) {
                key++;
                queryObject.setParameter(key, teamsize);
            }
            if (haveExperience) {
                key++;
                queryObject.setParameter(key, experience);
            }
            if (haveCity) {
                queryObject.setString("city", "%" + city + "%");
            }
            queryObject.setFirstResult(PageUtil.getFirstResult(page, pageSize));
            queryObject.setMaxResults(pageSize);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all joblooking failed", re);
            throw re;
        }
    }

    /**
     * 保存一条招聘申请信息
     *
     * @param transientInstance
     */
    public boolean applyJob(JobApply transientInstance) {
        log.debug("save JobApply instance");
        try {
            getCurrentSession().save(transientInstance);
            log.debug("save JobApply successful");
            return true;
        } catch (RuntimeException re) {
            log.error("save JobApply failed", re);
        }
        return false;
    }

    /**
     * 获取该招聘申请列表
     *
     * @return
     */
    public List queryApplyList(int jobId) {
        try {
            String queryString = "select new com.ktp.project.entity.JobApply(ja.id,ja.jobId,ja.userId,ja.inTime,ja.mobile,ja.content,ui.u_realname,ui.u_pic,ui.u_jnf,ui.u_sex) " +
                    "from JobApply as ja,UserInfo as ui where ja.jobId=? and ja.userId = ui.id order by ja.id desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, jobId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all JobApply failed", re);
            throw re;
        }
    }

    /**
     * 查询该招聘申请人数
     *
     * @param jobId 招聘id
     * @return
     */
    public long queryApplyCount(int jobId) {
        try {
            String queryString = "select count(*) from JobApply ja where ja.jobId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, jobId);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 查询是否已申请
     *
     * @param jobId 招聘id
     * @return
     */
    public long checkIsApply(int jobId, int userId) {
        try {
            String queryString = "select count(*) from JobApply ja where ja.jobId=? and ja.userId=?";
            Query queryObject = getCurrentSession().createQuery(queryString).setParameter(0, jobId).setParameter(1, userId);
            return (long) queryObject.uniqueResult();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 获取我的申请列表
     *
     * @return
     */
    public List queryJobIdList(int userId) {
        try {
            String queryString = "select ja.jobId from JobApply as ja where ja.userId =? order by ja.id desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, userId);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all JobIdList failed", re);
            throw re;
        }
    }

    /**
     * 查找工种-班组规模-工作经验
     *
     * @return
     */
    public List queryFilterForJob() {
        try {
            String queryString = "from KeyContent kc where kc.keyId in (4,15,16) order by kc.id";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all KeyContent failed", re);
            throw re;
        }
    }

    /**
     * 停用该找工
     *
     * @param joblId 找工id
     * @return
     */
    public int stopUsingJobLooking(int userId, int joblId) {
        try {
            String queryString = "update JobLooking jl set jl.pubState=2 where jl.id=? and jl.pubUid=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setParameter(0, joblId);
            query.setParameter(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update JobLooking fail", re);
            return -1;
        }
    }

    /**
     * 软删除该找工
     *
     * @param joblId 找工id
     * @return
     */
    public int deleteJobLooking(int userId, int joblId) {
        try {
            String queryString = "update JobLooking jl set jl.pubState=1 where jl.id=? and jl.pubUid=?";
            Query query = getCurrentSession().createQuery(queryString);
            query.setParameter(0, joblId);
            query.setParameter(1, userId);
            return query.executeUpdate();
        } catch (RuntimeException re) {
            log.error("update JobLooking fail", re);
            return -1;
        }
    }


    /**
      * 根据发布id查询人员id
      *
      * @return java.lang.Integer
      * @Author: wuyeming
      * @params: [jobId]
      * @Date: 2018-10-29 下午 16:46
      */
    public Integer getUserId(Integer jobId) {
        String sql = "select job_uid userId from jobs_list where id =" + jobId;
        Integer userId = null;
        try {
            userId = (Integer) getCurrentSession().createSQLQuery(sql).uniqueResult();
        } catch (Exception e) {
            throw e;
        }
        return userId;
    }
}
