package com.ktp.project.web;

import com.ktp.project.entity.JobApply;
import com.ktp.project.entity.JobList;
import com.ktp.project.entity.JobLooking;
import com.ktp.project.entity.JobSelection;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.JobService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.LocationUtils;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.KeyContent;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * job controller
 *
 * @author djcken
 * @date 2018/6/23
 */
@Controller
@RequestMapping(value = "api/job", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class JobController {

    private Logger logger = LoggerFactory.getLogger("JobController");

    @Autowired
    private JobService jobService;

    /**
     * 保存或更新一条招聘信息
     *
     * @param pubUid     发布人uid
     * @param jobType    招聘类型1项目部招聘 2班组长招聘
     * @param jobObj     招聘对象1工人 2班组长
     * @param jobGzid    工种id
     * @param jobGzname  工种名称
     * @param jobAddress 项目地址
     * @param jobAddw    项目经度
     * @param jobAddh    项目纬度
     * @param jobProtime 项目开工时间
     * @param jobNumber  招聘数量
     * @param jobContent 招聘内容
     * @param request
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public String saveJob(@Param(value = "pubUid") int pubUid, @Param(value = "proName") String proName,
                          @Param(value = "jobType") int jobType, @Param(value = "jobObj") int jobObj,
                          @Param(value = "jobGzid") int jobGzid, @Param(value = "jobGzname") String jobGzname, @Param(value = "jobAddress") String jobAddress,
                          @Param(value = "jobAddw") double jobAddw, @Param(value = "jobAddh") double jobAddh, @Param(value = "jobLocation") String jobLocation,
                          @Param(value = "jobProtime") String jobProtime, @Param(value = "jobNumber") String jobNumber, @Param(value = "jobContent") String jobContent,
                          HttpServletRequest request) {
        if (pubUid <= 0 || jobGzid <= 0 || TextUtils.isEmpty(jobGzname) || TextUtils.isEmpty(jobAddress) || TextUtils.isEmpty(jobProtime) ||
                jobAddw <= 0 || jobAddh <= 0 || TextUtils.isEmpty(jobNumber) || TextUtils.isEmpty(jobContent)) {
            throw new BusinessException("缺少必要参数");
        }
        if ((jobType != 1 && jobType != 2) || (jobObj != 1 && jobObj != 2)) {
            throw new BusinessException("类型不正确");
        }
        if (StringUtil.parseToInt(jobNumber) == 0) {
            throw new BusinessException("招聘数量不能为0");
        }
        String jobExperience = request.getParameter("jobExperience");
        String jobTeamsize = request.getParameter("jobTeamsize");
        String jobIdStr = request.getParameter("jobId");
        int jobId = StringUtil.parseToInt(jobIdStr);
        JobList job = new JobList();
        if (jobId > 0) {//如果带id则更新
            job.setId(jobId);
        }
        job.setPubUid(pubUid);
        job.setJobProname(proName);
        job.setJobType(jobType);
        job.setJobState(1);
        job.setJobObj(jobObj == 1 ? 4 : 8);
        job.setJobIntime(DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME));
        job.setJobGzid(jobGzid);
        job.setJobGzname(jobGzname);
        job.setJobAddress(jobAddress);
        job.setJobAddw(jobAddw);
        job.setJobAddh(jobAddh);
        job.setJobLocation(jobLocation);
        job.setJobExperience(jobExperience);
        job.setJobTeamsize(jobTeamsize);
        job.setJobProtime(DateUtil.format(DateUtil.getFormatDate(jobProtime, DateUtil.FORMAT_DATE), DateUtil.FORMAT_DATE));
        job.setJobNumber(jobNumber);
        job.setJobContent(jobContent);
        boolean success = jobService.saveJob(job);

        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "mylist", method = RequestMethod.GET)
    @ResponseBody
    public String getMyJobList(@Param(value = "pubUid") int pubUid, @Param(value = "jobType") int jobType, @Param(value = "jobObj") int jobObj, @Param(value = "page") int page, @Param(value = "pageSize") int pageSize) {
        if (pubUid <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        if (jobType != 1 && jobType != 2) {
            throw new BusinessException("类型不正确");
        }
        List<JobList> jobList = jobService.queryMyJobList(pubUid, jobType, jobObj, page, pageSize);
        if (jobList != null && !jobList.isEmpty()) {
            for (JobList job : jobList) {
                int jobId = job.getId();
                int applyCount = (int) jobService.queryApplyCount(jobId);
                job.setApplyNum(applyCount);
            }
        }
        return ResponseUtil.createNormalJson(jobList);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public String getJobList(@Param(value = "u_id") int u_id, @Param(value = "page") int page, @Param(value = "pageSize") int pageSize, HttpServletRequest request) {
        if (u_id <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        int jobType = StringUtil.parseToInt(request.getParameter("jobType"));//招聘类型
        int jobObj = StringUtil.parseToInt(request.getParameter("jobObj"));//招聘对象4工人 8班组长
        int orderby = StringUtil.parseToInt(request.getParameter("orderby"));//排序 0默认 1时间 2距离
        int sort = StringUtil.parseToInt(request.getParameter("sort"));//1升序asc  2降序desc
        int apply = StringUtil.parseToInt(request.getParameter("apply"));//是否已申请 0默认 1已申请 2未申请
        String gzIds = request.getParameter("gzIds");//工种ids 逗号分隔
        String experience = request.getParameter("experience");//经验
        String teamsize = request.getParameter("teamsize");//规模
        String area = request.getParameter("area");//地区 传入省，市
        double longitude = StringUtil.parseToDouble(request.getParameter("lon"));//经度
        double latitude = StringUtil.parseToDouble(request.getParameter("lat"));//维度
        boolean havelonlat = orderby == 2 && longitude > 0 && latitude > 0;//按距离排序
        List<JobList> jobList = jobService.queryJobList(u_id, jobType, jobObj, orderby, sort, apply, gzIds, experience, teamsize, area, page, pageSize);
        if (jobList != null && !jobList.isEmpty()) {
            for (JobList job : jobList) {
                int jobId = job.getId();
                boolean isApply = jobService.checkIsApply(jobId, u_id);
                job.setApply(isApply ? 1 : 0);
                int applyCount = (int) jobService.queryApplyCount(jobId);
                job.setApplyNum(applyCount);
                if (havelonlat) {
                    double addw = job.getJobAddw();
                    double addh = job.getJobAddh();
                    job.setDistance(LocationUtils.getDistance(addw, addh, longitude, latitude));
                }
            }
            if (havelonlat) {//距离排序后返回
                jobList.sort((o1, o2) -> (sort == 1 ? (int) (o1.getDistance() - (o2.getDistance())) : ((int) (o2.getDistance() - (o1.getDistance())))));
            }
        }
        return ResponseUtil.createNormalJson(jobList);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ResponseBody
    public String getDetail(@Param(value = "jobId") int jobId) {
        if (jobId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        JobList jobList = jobService.queryJobDetail(jobId);
        if (jobList != null) {
            int applyCount = (int) jobService.queryApplyCount(jobId);
            jobList.setApplyNum(applyCount);
            List<JobApply> applyList = jobService.queryApplyList(jobId);
            jobList.setJobApplyList(applyList);
        }
        return ResponseUtil.createNormalJson(jobList);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@Param(value = "pubUid") int pubUid, @Param(value = "jobId") int jobId) {
        if (pubUid <= 0 && jobId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = jobService.delete(pubUid, jobId);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @ResponseBody
    public String stop(@Param(value = "pubUid") int pubUid, @Param(value = "jobId") int jobId) {
        if (pubUid <= 0 && jobId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = jobService.stopUsing(pubUid, jobId);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "looking/save", method = RequestMethod.POST)
    @ResponseBody
    public String saveJobLooking(@Param(value = "pubUid") int pubUid, @Param(value = "pubMobile") String pubMobile, @Param(value = "pubType") int pubType,
                                 @Param(value = "gzId") int gzId, @Param(value = "gzName") String gzName, @Param(value = "workTime") String workTime, @Param(value = "city") String city,
                                 @Param(value = "content") String content, HttpServletRequest request) {
        if (pubUid <= 0 || TextUtils.isEmpty(pubMobile) || gzId <= 0 || TextUtils.isEmpty(gzName) || TextUtils.isEmpty(workTime) || TextUtils.isEmpty(city) || TextUtils.isEmpty(content)) {
            throw new BusinessException("缺少必要参数");
        }
        if (pubType != 1 && pubType != 2) {
            throw new BusinessException("类型不正确");
        }
        String teamSize = request.getParameter("teamsize");
        String experience = request.getParameter("experience");
        String workAge = request.getParameter("workAge");
        int joblId = StringUtil.parseToInt(request.getParameter("joblId"));
        JobLooking jobLooking = new JobLooking();
        if (joblId > 0) {//如果带id则更新
            jobLooking.setId(joblId);
        }
        jobLooking.setPubUid(pubUid);
        jobLooking.setPubMobile(pubMobile);
        jobLooking.setPubType(pubType == 1 ? 8 : 4);
        jobLooking.setGzId(gzId);
        jobLooking.setGzName(gzName);
        jobLooking.setWorkTime(workTime);
        jobLooking.setCity(city);
        jobLooking.setTeamSize(teamSize);
        jobLooking.setExperience(experience);
        jobLooking.setWorkAge(workAge);
        jobLooking.setContent(content);
        jobLooking.setCreateTime(DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME));
        boolean success = jobService.saveJobLooking(jobLooking);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "looking/mylist", method = RequestMethod.GET)
    @ResponseBody
    public String getMyJobLooking(@Param(value = "pubUid") int pubUid, @Param(value = "page") int page, @Param(value = "pageSize") int pageSize, HttpServletRequest request) {
        if (pubUid <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        int pubTypeReal = -1;
        String pubTypeStr = request.getParameter("pubType");
        if (!TextUtils.isEmpty(pubTypeStr)) {
            int pubType = StringUtil.parseToInt(pubTypeStr);
            pubTypeReal = pubType == 1 ? 8 : 4;
        }
        List<JobLooking> lookingList = jobService.queryMyJobLooking(pubUid, pubTypeReal, page, pageSize);
        return ResponseUtil.createNormalJson(lookingList);
    }

    /**
     * @param pubType 找工类型1.班组找工  2工人找工
     * @return
     */
    @RequestMapping(value = "looking/list", method = RequestMethod.GET)
    @ResponseBody
    public String getJobLooking(@Param(value = "pubType") int pubType, @Param(value = "page") int page, @Param(value = "pageSize") int pageSize, HttpServletRequest request) {
        if (pubType != 1 && pubType != 2) {
            throw new BusinessException("类型不正确");
        }
        int orderby = StringUtil.parseToInt(request.getParameter("orderby"));//排序 0默认 1规模 2经验 3评分
        int sort = StringUtil.parseToInt(request.getParameter("sort"));//1升序asc  2降序desc
        String gzIds = request.getParameter("gzIds");//工种ids 逗号分隔
        String experience = request.getParameter("experience");//经验
        String teamsize = request.getParameter("teamsize");//规模
        String city = request.getParameter("city");
        List<JobLooking> lookingList = jobService.queryJobLooking(pubType == 1 ? 8 : 4, orderby, sort, gzIds, experience, teamsize, city, page, pageSize);
        return ResponseUtil.createNormalJson(lookingList);
    }

    @RequestMapping(value = "apply", method = RequestMethod.POST)
    @ResponseBody
    public String applyJob(@Param(value = "u_id") int u_id, @Param(value = "jobId") int jobId, @Param(value = "mobile") String mobile, HttpServletRequest request) {
        if (u_id <= 0 || jobId <= 0 || TextUtils.isEmpty(mobile)) {
            throw new BusinessException("缺少必要参数");
        }
        String content = request.getParameter("content");
        boolean isApply = jobService.checkIsApply(jobId, u_id);//判断是否已申请
        if (!isApply) {
            boolean success = jobService.applyJob(jobId, u_id, mobile, content);
            return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
        } else {
            return ResponseUtil.createBussniessErrorJson(0, "您已经申请过该招聘");
        }
    }

    /**
     * 工种-班组规模-经验选择
     *
     * @return
     */
    @RequestMapping(value = "selection", method = RequestMethod.GET)
    @ResponseBody
    public String selection(HttpServletRequest request) {
        List<KeyContent> selectionList = jobService.queryFilterForJob();
        JobSelection jobSelection = new JobSelection();
        List<KeyContent> gzList = new ArrayList<>();
        List<KeyContent> experienceList = new ArrayList<>();
        List<KeyContent> teamsizeList = new ArrayList<>();
        jobSelection.setGzList(gzList);
        jobSelection.setExperienceList(experienceList);
        jobSelection.setTeamsizeList(teamsizeList);
        if (selectionList != null && !selectionList.isEmpty()) {
            for (KeyContent keyContent : selectionList) {
                int keyId = keyContent.getKeyId();
                if (keyId == 4) {
                    gzList.add(keyContent);
                } else if (keyId == 15) {
                    teamsizeList.add(keyContent);
                } else if (keyId == 16) {
                    experienceList.add(keyContent);
                }
            }
        }
        return ResponseUtil.createNormalJson(jobSelection);
    }

    @RequestMapping(value = "looking/delete", method = RequestMethod.POST)
    @ResponseBody
    public String lookingDelete(@Param(value = "pubUid") int pubUid, @Param(value = "joblId") int joblId) {
        if (pubUid <= 0 && joblId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = jobService.deleteJobLooking(pubUid, joblId);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }

    @RequestMapping(value = "looking/stop", method = RequestMethod.POST)
    @ResponseBody
    public String lookingStop(@Param(value = "pubUid") int pubUid, @Param(value = "joblId") int joblId) {
        if (pubUid <= 0 && joblId <= 0) {
            throw new BusinessException("缺少必要参数");
        }
        boolean success = jobService.stopUsingJobLooking(pubUid, joblId);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessErrorJson(0, "失败");
    }


}
