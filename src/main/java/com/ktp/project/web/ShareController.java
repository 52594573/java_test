package com.ktp.project.web;

import com.ktp.project.entity.ShareCommit;
import com.ktp.project.entity.ShareRecord;
import com.ktp.project.entity.ShareUser;
import com.ktp.project.exception.DefaultException;
import com.ktp.project.service.ShareService;
import com.ktp.project.service.UserService;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
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
import java.util.List;

@Controller
@RequestMapping(value = "h5/share", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class ShareController {

    private Logger logger = LoggerFactory.getLogger("ShareController");

    @Autowired
    private ShareService shareService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "detail", method = RequestMethod.GET)
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
     * @param shareType 分享类型 1.商城分享 2.开太平app分享 3是邀请
     * @return
     */
    @RequestMapping(value = "click")
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
     * @param shareType  分享类型 1.商城分享 2.开太平app分享  3.邀请加入ktp项目
     * @param commitType 用户点击类型 1.点击购物车 2.点击立即购买
     * @param commitTel  提交的电话号码
     * @return
     */
    @RequestMapping(value = "commit")
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


    /**
     * 统计提交
     *
     * @param shareType  分享类型 1.商城分享 2.开太平app分享  3.邀请加入ktp项目
     * @param commitType 用户点击类型 1.点击购物车 2.点击立即购买
     * @param commitTel  提交的电话号码
     * @return
     */
    @RequestMapping(value = "commitbybankid")
    @ResponseBody
    public String commitbybankid(@Param(value = "bank_id") int bank_id, @Param(value = "shareType") int shareType,
                                 @Param(value = "commitType") int commitType, @Param(value = "commitTel") String commitTel, HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String channel = request.getParameter("channel");
        int channelId = !TextUtils.isEmpty(channel) ? Integer.parseInt(channel) : 0;
        ShareCommit shareCommit = shareService.queryShareCommit(0, shareType, commitType, commitTel);
        if (shareCommit == null) {
            shareCommit = new ShareCommit();
            shareCommit.setShareUid(0);
            shareCommit.setShareType(shareType);
            shareCommit.setCommitType(commitType);
            shareCommit.setCommitTel(commitTel);
            shareCommit.setCommitRegister(0);
            shareCommit.setCommitCert(0);
            shareCommit.setShareCannel(channelId);
        }
        shareCommit.setCcbBankId(bank_id);
        shareCommit.setCommitTime(new Date());
        boolean success = shareService.saveOrUpdateShareCommit(shareCommit);
        return success ? ResponseUtil.createBussniessJson("成功") : ResponseUtil.createBussniessJson("失败");
    }

}
