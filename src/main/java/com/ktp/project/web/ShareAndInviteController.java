package com.ktp.project.web;

import com.ktp.project.entity.ShareCommit;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.CheckManagerService;
import com.ktp.project.service.ShareAndInviteService;
import com.ktp.project.service.ShareService;
import com.ktp.project.service.UserService;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.ResponseUtil;
import com.zm.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 分享与邀请
 */
@RestController
@RequestMapping(value = "api/shareAndInvite", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class ShareAndInviteController {


    @Autowired
    private ShareAndInviteService shareAndInviteService;
    @Autowired
    private ShareService shareService;


    @RequestMapping(value = "/joinKtpFamilyByMobile", method = RequestMethod.POST)
    public String joinKtpFamilyByMobile(@RequestParam("projectId") Integer projectId, @RequestParam("organId") Integer organId,
                                        @RequestParam("mobile") String mobile, @RequestParam(value = "inviteUserId",defaultValue = "-1") Integer inviteUserId) {
        try {
            shareAndInviteService.joinKtpFamilyByMobile(projectId, organId, mobile, inviteUserId);
            return ResponseUtil.createNormalJson("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/joinKtpFamilyByUserIds", method = RequestMethod.POST)
    public String joinKtpFamilyByUserIds(@RequestParam("projectId") Integer projectId, @RequestParam("organId") Integer organId,
                                         @RequestParam("userIds") String mobiles, @RequestParam(value = "inviteUserId", defaultValue = "-1") Integer inviteUserId) {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.joinKtpFamilyByUserIds(projectId, organId, mobiles, inviteUserId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/queryInviteLogTopTen", method = RequestMethod.GET)
    public String queryInviteLogTopTen() {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.queryInviteLogTopTen());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/queryInviteLogByInviteId", method = RequestMethod.GET)
    public String queryInviteLogByInviteId(@RequestParam("userInviteId") Integer userInviteId) {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.queryInviteLogByInviteId(userInviteId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/queryShareContentByUserId", method = RequestMethod.GET)
    public String queryShareContentByUserId(Integer projectId, Integer userId) {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.queryShareContentByUserId(projectId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/judgePhoneIfRegister", method = RequestMethod.POST)
    public String judgePhoneIfRegister(@RequestParam("projectId") Integer projectId, @RequestParam("organId") Integer organId, @RequestParam("mobiles") String mobiles) {
        try {
            if (StringUtils.isBlank(mobiles)) {
                return ResponseUtil.createNormalJson(null);
            }
            return ResponseUtil.createNormalJson(shareAndInviteService.judgePhoneIfRegister(projectId, organId, mobiles));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/queryProjectFence", method = RequestMethod.POST)
    public String queryProjectFence(@RequestParam("projectId") Integer projectId, @RequestParam("userId") Integer userId) {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.queryProjectFence(projectId, userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/createShareCommit", method = RequestMethod.POST)
    public String createShareCommit(@RequestParam("userId") Integer userId, @RequestParam("commitTel") String commitTel, @RequestParam("srChannel") Integer srChannel) {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.saveOrUpdateShareCommit(userId, commitTel, srChannel));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/createShare", method = RequestMethod.POST)
    public String createShare(@RequestParam("userId") Integer userId, @RequestParam("commitTel") String commitTel, @RequestParam("srChannel") Integer srChannel,
                              @RequestParam("proId") Integer proId,@RequestParam("organid") Integer organid) {
        try {
            return ResponseUtil.createNormalJson(shareAndInviteService.saveOrUpdateShareCommit(userId, commitTel, srChannel,proId,organid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/updateCommitRegisterByMobile", method = RequestMethod.GET)
    public String updateCommitRegisterByMobile(@RequestParam("mobile") String mobile) {
        try {

            boolean b = shareAndInviteService.updateCommitRegisterByMobile(mobile);
            if(b){
                joinProject(mobile);
            }
            return ResponseUtil.createNormalJson(b);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    private void joinProject(String mobile) {
        ShareCommit shareCommit = shareService.getShareCommit(mobile);
        if (shareCommit != null){
            int commitProId = shareCommit.getCommitProId();
            int commitOrganid = shareCommit.getCommitOrganid();
            joinKtpFamilyByMobile(commitProId, commitOrganid, mobile, shareCommit.getShareUid());
        }
    }

    @RequestMapping(value = "/joinKtpFamilyByPhone", method = RequestMethod.POST)
    public String joinKtpFamilyByPhone(@RequestParam("projectId") Integer projectId, @RequestParam("organId") Integer organId,
                                       @RequestParam("mobile") String mobile, @RequestParam(value = "inviteUserId", defaultValue = "-1") Integer inviteUserId) {
        try {
            shareAndInviteService.joinKtpFamilyByPhone(projectId, organId, mobile, inviteUserId);
            return ResponseUtil.createNormalJson("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
