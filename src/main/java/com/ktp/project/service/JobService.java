package com.ktp.project.service;

import com.ktp.project.dao.JobDao;
import com.ktp.project.dao.PushLogCircleDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.entity.JobApply;
import com.ktp.project.entity.JobList;
import com.ktp.project.entity.JobLooking;
import com.ktp.project.entity.PushLogCircle;
import com.ktp.project.util.JPushClientUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.KeyContent;
import com.zm.entity.UserInfo;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * job服务
 *
 * @author djcken
 * @date 2018/6/23
 */
@Service
public class JobService {

    @Autowired
    private JobDao jobDao;

    @Autowired
    private PushLogCircleDao pushLogCircleDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Value("${jpush.env}")
    private String env;

    /**
     * 保存招聘信息
     *
     * @return
     */
    public boolean saveJob(JobList job) {
        return jobDao.saveOrUpdateJob(job);
    }

    /**
     * 查找我的招聘信息列表
     *
     * @param pubUid  发布人uid
     * @param jobType 招聘类型1项目部招聘 2班组长招聘
     * @return
     */
    public List<JobList> queryMyJobList(int pubUid, int jobType, int jobObj, int page, int pageSize) {
        return jobDao.queryMyJobList(pubUid, jobType, jobObj, page, pageSize);
    }

    /**
     * 查找招聘信息列表
     *
     * @param jobType
     * @param jobObj
     * @param orderby
     * @return
     */
    public List<JobList> queryJobList(int userId, int jobType, int jobObj, int orderby, int sort, int apply, String gzIdstr, String experience, String teamsize, String area, int page, int pageSize) {
        String[] gzIds = !TextUtils.isEmpty(gzIdstr) ? gzIdstr.split(",") : null;
        List<Integer> gzIdList = new ArrayList<>();
        if (gzIds != null && gzIds.length > 0) {
            for (String gzId : gzIds) {
                gzIdList.add(StringUtil.parseToInt(gzId));
            }
        }
        return jobDao.queryJobList(userId, jobType, jobObj, orderby, sort, apply, gzIdList, experience, teamsize, area, page, pageSize);
    }

    /**
     * 查询招聘信息详情
     *
     * @param jobId 招聘信息id
     * @return
     */
    public JobList queryJobDetail(int jobId) {
        return jobDao.queryJobDetail(jobId);
    }

    /**
     * 删除某条招聘
     *
     * @param userId 发布人的uid
     * @param jobId  jobid
     * @return
     */
    public boolean delete(int userId, int jobId) {
        return jobDao.delete(userId, jobId) == 1;
    }

    /**
     * 停止某条招聘
     *
     * @param userId
     * @param jobId
     * @return
     */
    public boolean stopUsing(int userId, int jobId) {
        return jobDao.stopUsing(userId, jobId) == 1;
    }

    /**
     * 保存找工信息
     *
     * @return
     */
    public boolean saveJobLooking(JobLooking jobLooking) {
        return jobDao.saveJobLooking(jobLooking);
    }

    public List<JobLooking> queryJobLooking(int pubType, int orderby, int sort, String gzIdstr, String experience, String teamsize, String city, int page, int pageSize) {
        String[] gzIds = !TextUtils.isEmpty(gzIdstr) ? gzIdstr.split(",") : null;
        List<Integer> gzIdList = new ArrayList<>();
        if (gzIds != null && gzIds.length > 0) {
            for (String gzId : gzIds) {
                gzIdList.add(StringUtil.parseToInt(gzId));
            }
        }
        return jobDao.queryJobLooking(pubType, orderby, sort, gzIdList, experience, teamsize, city, page, pageSize);
    }

    public List<JobLooking> queryMyJobLooking(int pubUid, int pubType, int page, int pageSize) {
        return jobDao.queryMyJobLooking(pubUid, pubType, page, pageSize);
    }

    @Transactional
    public boolean applyJob(int jobId, int userId, String mobile, String content) {
        JobApply jobApply = new JobApply();
        jobApply.setJobId(jobId);
        jobApply.setUserId(userId);
        jobApply.setInTime(new Date());
        jobApply.setMobile(mobile);
        jobApply.setContent(content);
        boolean flag = jobDao.applyJob(jobApply);
        if (flag) {
            Integer uId = jobDao.getUserId(jobId);
            UserInfo userInfo = userInfoDao.getUserInfoById(uId);
            if (userInfo != null) {
                String OS = userInfo.getLast_device();
                List<String> aliasList = new ArrayList<>();
                aliasList.add("KTP_" + env + "_" + OS + "_" + userInfo.getId());
                Map<String, String> map = new HashMap<>();
                map.put("notify", "1");//是否显示在通知栏 0不显示 1显示
                map.put("messageType", "job");//招聘信息详情页
                map.put("messageId", jobId + "");
                map.put("pushType", "job");
                int result = JPushClientUtil.getInstance().pushDevice(aliasList, OS, "新招聘信息", "发现新的应聘者，点击查看!", map, "1");
                PushLogCircle pushLogCircle = new PushLogCircle();
                pushLogCircle.settIndex(2);
                pushLogCircle.setIndexId(jobApply.getId());
                pushLogCircle.setFromUserId(userId);
                pushLogCircle.setToUserId(uId);
                pushLogCircle.setType(1);
                pushLogCircle.setNotify(1);
                pushLogCircle.setStatus(result);
                pushLogCircle.setCreateTime(new Timestamp(System.currentTimeMillis()));
                pushLogCircleDao.savePushLogCircle(pushLogCircle);
            }
        }
        return flag;
    }

    public List<JobApply> queryApplyList(int jobId) {
        return jobDao.queryApplyList(jobId);
    }

    public long queryApplyCount(int jobId) {
        return jobDao.queryApplyCount(jobId);
    }

    public boolean checkIsApply(int jobId, int userId) {
        return jobDao.checkIsApply(jobId, userId) > 0;
    }

    public List<KeyContent> queryFilterForJob() {
        return jobDao.queryFilterForJob();
    }

    /**
     * 删除某条找工
     *
     * @param userId 发布人的uid
     * @param joblId joblId
     * @return
     */
    public boolean deleteJobLooking(int userId, int joblId) {
        return jobDao.deleteJobLooking(userId, joblId) == 1;
    }

    /**
     * 停止某条找工
     *
     * @param userId
     * @param joblId
     * @return
     */
    public boolean stopUsingJobLooking(int userId, int joblId) {
        return jobDao.stopUsingJobLooking(userId, joblId) == 1;
    }

}
