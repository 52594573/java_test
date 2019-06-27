package com.ktp.project.web;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.dto.im.GroupUserDto;
import com.ktp.project.service.ChatIgnoreService;
import com.ktp.project.service.FriendService;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.ResponseUtil;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 好友
 * Created by LinHon 2018/8/2
 */
@RestController
@RequestMapping(value = "api/friend", produces = "application/json;charset=UTF-8;")
public class FriendController {

    private static final Logger log = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;

    @Autowired
    private ChatIgnoreService chatIgnoreService;


    /**
     * 注册IM账号（通过）
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String userId) {
        try {
            HuanXinRequestUtils.register(userId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("[注册IM账号异常]", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }

    @RequestMapping(value = "/httpRegister", method = RequestMethod.POST)
    public Map<String, Object> httpRegister(@RequestBody GroupUserDto groupUserDto) {
        try {
            HuanXinRequestUtils.register(groupUserDto.getUserId());
            return ImmutableMap.of("code", 200);
        } catch (Exception e) {
            log.error("[注册IM账号异常]", e);
            return ImmutableMap.of("code", 500, "msg", e.getMessage());
        }
    }


    /**
     * 注册默认IM账号（通过）
     *
     * @return
     */
    @RequestMapping(value = "/defaultRegisters", method = RequestMethod.POST)
    public String defaultRegisters() {
        try {
            friendService.defaultRegisters();
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("[注册IM账号异常]", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 搜索好友
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/queryFriend", method = RequestMethod.GET)
    public String queryFriend(@RequestParam("mobile") String mobile) {
        try {
            if (TextUtils.isEmpty(mobile)) {
                return ResponseUtil.createBussniessErrorJson(400, "缺少必要参数");
            }
            return ResponseUtil.createNormalJson(friendService.queryFriendsByMobile(mobile));
        } catch (Exception e) {
            log.error("[搜索好友异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 判断是否已是好友关系
     *
     * @param userId       用户自己ID
     * @param friendUserId 好友ID
     * @return
     */
    @RequestMapping(value = "/isFriend", method = RequestMethod.GET)
    public String isFriend(int userId, int friendUserId) {
        try {
            return ResponseUtil.createNormalJson(ImmutableMap.of("isFriend", friendService.isFriend(userId, friendUserId)));
        } catch (Exception e) {
            log.error("[判断是否已是好友关系异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 添加好友
     *
     * @param userId       用户自己ID
     * @param friendUserId 要添加好友的ID
     * @return
     */
    @RequestMapping(value = "/applyAddFriend", method = RequestMethod.POST)
    public String applyAddFriend(int userId, int friendUserId, String applyMsg) {
        try {
            friendService.applyAddFriend(userId, friendUserId, applyMsg);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("[添加好友异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 新的朋友未读数量
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/queryNewFriendNotReadNumber", method = RequestMethod.GET)
    public String queryNewFriendNotReadNumber(int userId) {
        try {
            return ResponseUtil.createNormalJson(ImmutableMap.of("notReadNumber", friendService.queryNewFriendNotReadNumber(userId)));
        } catch (Exception e) {
            log.error("[查询新的朋友未读数量异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 新的朋友
     *
     * @param userId    用户自己的ID
     * @param startPage 开始页
     * @param length    每页数量
     * @return
     */
    @RequestMapping(value = "/queryNewFriends", method = RequestMethod.GET)
    public String queryNewFriends(int userId, int startPage, int length) {
        try {
            return ResponseUtil.createNormalJson(friendService.queryNewFriends(userId, startPage, length));
        } catch (Exception e) {
            log.error("[获取新的朋友异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 同意添加好友
     *
     * @param userId       用户自己的ID
     * @param friendUserId 发起好友申请的用户ID
     * @return
     */
    @RequestMapping(value = "/agreeAddFriend", method = RequestMethod.POST)
    public String agreeAddFriend(int userId, int friendUserId) {
        try {
            friendService.agreeAddFriend(userId, friendUserId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("[同意添加好友异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 拒绝添加好友
     *
     * @param userId       用户自己的ID
     * @param friendUserId 发起好友申请的用户ID
     * @return
     */
    @RequestMapping(value = "/refuseAddFriend", method = RequestMethod.POST)
    public String refuseAddFriend(int userId, int friendUserId) {
        try {
            friendService.refuseAddFriend(userId, friendUserId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("[拒绝添加好友异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 删除好友
     *
     * @param userId       用户自己的ID
     * @param friendUserId 好友ID
     * @return
     */
    @RequestMapping(value = "/removeFriend", method = RequestMethod.POST)
    public String removeFriend(int userId, int friendUserId) {
        try {
            friendService.removeFriend(userId, friendUserId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("[删除好友异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 好友列表
     *
     * @param userId
     */
    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public String friends(int userId, @RequestParam(value = "pro_id", defaultValue = "-1") int pro_id) {
        try {
            return ResponseUtil.createNormalJson(friendService.queryFriends(userId, pro_id));
        } catch (Exception e) {
            log.error("[获取好友列表异常]", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 切换屏蔽消息状态
     *
     * @param fromUserId
     * @param toUserId
     * @return
     */
    @RequestMapping(value = "/ignoreMsg", method = RequestMethod.POST)
    public String ignoreMsg(int fromUserId, int toUserId) {
        try {
            chatIgnoreService.modify(fromUserId, toUserId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("屏蔽/取消屏蔽好友消息异常", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

}
