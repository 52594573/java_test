package com.ktp.project.web;

import com.ktp.project.entity.ShareCommit;
import com.ktp.project.entity.ShareRecord;
import com.ktp.project.entity.ShareUser;
import com.ktp.project.exception.DefaultException;
import com.ktp.project.service.BenefitService;
import com.ktp.project.service.ShareService;
import com.ktp.project.service.UserService;
import com.ktp.project.service.WorkLogService;
import com.ktp.project.util.LoanUtils;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.Project;
import com.zm.entity.UserInfo;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "benefit/h5", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class BenefitH5Controller {
    private Logger logger = LoggerFactory.getLogger("BenefitH5Controller");

    @Autowired
    private BenefitService benefitService;

    @Autowired
    private WorkLogService workLogService;


    @Autowired
    private ShareService shareService;
    @Autowired
    private UserService userService;

    /**
     * 获取活动分享接口信息
     */
    @RequestMapping(value = "activityShare", method = RequestMethod.GET)
    @ResponseBody
    public String getActivityShare(@Param(value = "actId") int actId, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();
        logger.debug("test xxxxxxxx session is " + session.getId());
        String checkcode = (String) session.getAttribute("checkcode");
        if (checkcode == null) {
            checkcode = StringUtil.randomUUID();
            session.setAttribute("checkcode", checkcode);
        }
        try {
            String str = ResponseUtil.createNormalJson(benefitService.getActivityShare(actId));
            return str;
        } catch (Exception e) {
            logger.error("获取活动分享异常", e);
            return LoanUtils.buildExceptionResponse(logger, "获取活动分享异常", e);
        }
    }

    /**
     * 捐赠详情分享页
     */
    @RequestMapping(value = "donateShare", method = RequestMethod.GET)
    @ResponseBody
    public String getDonateShare(@Param(value = "actId") int actId, @Param(value = "donId") int donId, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            return ResponseUtil.createNormalJson(benefitService.getDonateShare(actId, donId));
        } catch (Exception e) {
            logger.error("获取活动分享异常", e);
            return LoanUtils.buildExceptionResponse(logger, "获取活动分享异常", e);
        }
    }

    /**
     * 领取详情分享页
     */
    @RequestMapping(value = "recipientShare", method = RequestMethod.GET)
    @ResponseBody
    public String getRecipientShare(@Param(value = "actId") int actId, @Param(value = "donId") int donId,
                                    HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            return ResponseUtil.createNormalJson(benefitService.getRecipientShare(actId, donId));
        } catch (Exception e) {
            logger.error("获取活动分享异常", e);
            return LoanUtils.buildExceptionResponse(logger, "获取活动分享异常", e);
        }
    }


    /**
     * 领取详情分享页
     */
    @RequestMapping(value = "use_and_pro_content", method = RequestMethod.GET)
    @ResponseBody
    public String use_and_pro_content(@Param(value = "userId") int userId, @Param(value = "proId") int proId,
                                      HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            HttpSession session = request.getSession();
            String checkcode = (String) session.getAttribute("checkcode");
            if (checkcode == null) {
                checkcode = StringUtil.randomUUID();
                session.setAttribute("checkcode", checkcode);
            }
            UserInfo userInfo = workLogService.queryUseInfoByID(userId + "");
            Project project = workLogService.queryProject(proId + "");
            Map<String, String> map = new HashMap<>();
            map.put("shareUserName", userInfo.getU_realname());
            //头像地址处理
            if (userInfo != null && userInfo.getU_pic() != null && !StringUtil.isEmpty(userInfo.getU_pic())) {
                if (userInfo.getU_pic().indexOf("https") == -1 || userInfo.getU_pic().indexOf("ktpis") == -1 || userInfo.getU_pic().indexOf("http") == -1) {
                    map.put("shareUserPic", "https://tcs.ktpis.com" + userInfo.getU_pic());
                } else {
                    map.put("shareUserPic", userInfo.getU_pic());
                }
            } else {
                // if(comment.getuSex()==1){//男
                map.put("shareUserPic", "https://images.ktpis.com/images/pic/20181122154936224290102181.png");
            }
            map.put("shareProjectName", project.getPName());
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            logger.error("获取邀请加入项目信息异常", e);
            return LoanUtils.buildExceptionResponse(logger, "获取邀请加入项目信息异常", e);
        }
    }


    @RequestMapping(value = "share/detail", method = RequestMethod.GET)
    @ResponseBody
    public String detail(@Param(value = "userId") int userId, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();
        logger.debug("test session is " + session.getId());
        String checkcode = (String) session.getAttribute("checkcode");
        if (checkcode == null) {
            checkcode = StringUtil.randomUUID();
            session.setAttribute("checkcode", checkcode);
        }
        UserInfo userInfo = userService.queryUserInfo(userId);
        if (userInfo == null) {
            throw new DefaultException("该用户不存在");
        }
        ShareUser shareUser = new ShareUser();
        shareUser.setShareUserId(userId);
        shareUser.setShareUserName(userInfo.getU_nicheng());
        return ResponseUtil.createNormalJson(shareUser);
    }

    /**
     * 点击统计
     *
     * @param userId    分享用户id
     * @param shareType 分享类型 1.商城分享 2.开太平app分享
     * @return
     */
    @RequestMapping(value = "share/click")
    @ResponseBody
    public String click(@Param(value = "userId") int userId, @Param(value = "shareType") int shareType, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();
        String checkcode = (String) session.getAttribute("checkcode");
        logger.debug("test xxxxxxxx session is " + session.getId());
        if (checkcode == null) {
            return ResponseUtil.createBussniessErrorJson(0, "无效请求");
        } else {
            String channel = request.getParameter("channel");
            int channelId = !TextUtils.isEmpty(channel) ? Integer.parseInt(channel) : 0;
            ShareRecord shareRecord = shareService.queryShareRecord(userId, shareType);
            if (shareRecord == null) {
                shareRecord = new ShareRecord();
                shareRecord.setShareUid(userId);
                shareRecord.setShareClick(1);
                shareRecord.setShareTime(new Date());
                shareRecord.setShareType(shareType);
                shareRecord.setShareChannel(channelId);
            } else {
                int count = shareRecord.getShareClick() + 1;
                shareRecord.setShareClick(count);
            }
            shareRecord.setUpdateTime(new Date());
            boolean success = shareService.saveOrUpdateShareRecord(shareRecord);
            return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessJson("失败");
        }
    }

    /**
     * 统计提交
     *
     * @param userId     分享用户id
     * @param shareType  分享类型 1.商城分享 2.开太平app分享  4.邀请加入ktp项目
     * @param commitType 用户点击类型 1.点击购物车 2.点击立即购买
     * @param commitTel  提交的电话号码
     * @return
     */
    @RequestMapping(value = "share/commit")
    @ResponseBody
    public String commit(@Param(value = "userId") int userId, @Param(value = "shareType") int shareType,
                         @Param(value = "commitType") int commitType, @Param(value = "commitTel") String commitTel, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();
        String checkcode = (String) session.getAttribute("checkcode");
        if (checkcode == null) {
            return ResponseUtil.createBussniessErrorJson(0, "无效请求");
        } else {
            Object object = session.getAttribute("postcount");
            int postcount = StringUtil.parseToInt((String) object);
            if (postcount >= 0 && postcount < 5) {//同一session最多提交五次
                postcount++;
                session.setAttribute("postcount", "" + postcount);//设置提交次数
                String channel = request.getParameter("channel");
                int channelId = !TextUtils.isEmpty(channel) ? Integer.parseInt(channel) : 0;
                ShareCommit shareCommit = shareService.queryShareCommit(userId, shareType, commitType, commitTel);
                if (shareCommit == null) {
                    shareCommit = new ShareCommit();
                    shareCommit.setShareUid(userId);
                    shareCommit.setShareType(shareType);
                    shareCommit.setCommitType(commitType);
                    shareCommit.setCommitTel(commitTel);
                    shareCommit.setCommitRegister(0);
                    shareCommit.setShareCannel(channelId);
                }
                shareCommit.setCommitTime(new Date());
                boolean success = shareService.saveOrUpdateShareCommit(shareCommit);
                return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessJson("失败");
            }
            return ResponseUtil.createBussniessErrorJson(0, "同一session提交次数超出限制");
        }
    }


}
