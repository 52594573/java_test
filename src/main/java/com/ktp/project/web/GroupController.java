package com.ktp.project.web;

import com.ktp.project.service.GroupService;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 * 群组管理
 * Created by LinHon 2018/8/8
 */
@RestController
@RequestMapping(value = "api/group", produces = "application/json;charset=UTF-8;")
public class GroupController {

    private static final Logger log = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;


    /**
     * 公开添加群聊成员
     *
     * @param groupId
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/publicAddUsers", method = RequestMethod.POST)
    public String publicAddUsers(String groupId, String[] userIds) {
        try {
            HuanXinRequestUtils.addUsers(groupId, Arrays.asList(userIds));
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("批量添加群成员异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 添加群聊成员并加入项目
     *
     * @param groupType
     * @param id
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/joinGroupAndProject", method = RequestMethod.POST)
    public String joinGroupAndProject(@RequestParam("groupType") Integer groupType, @RequestParam("id") Integer id, @RequestParam("userIds") Integer[] userIds) {
        try {
            switch (groupType) {
                case 1:
                    groupService.joinGroupAndProject(id, Arrays.asList(userIds));
                    return ResponseUtil.createNormalJson("成功");
                case 2:
                    groupService.joinGroupAndOrgan(id, Arrays.asList(userIds));
                    return ResponseUtil.createNormalJson("成功");
                default:
                    return ResponseUtil.createBussniessErrorJson(500, "错误的类型");
            }
        } catch (Exception e) {
            log.error("添加群聊成员并加入项目", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 退出群聊且退出项目
     *
     * @param groupType
     * @param id
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/quitGroupAndProject", method = RequestMethod.POST)
    public String quitGroupAndProject(@RequestParam("groupType") Integer groupType, @RequestParam("id") Integer id, @RequestParam("userIds") Integer[] userIds) {
        try {
            switch (groupType) {
                case 1:
                    groupService.quitGroupAndProject(id, Arrays.asList(userIds));
                    return ResponseUtil.createNormalJson("成功");
                case 2:
                    groupService.quitGroupAndOrgan(id, Arrays.asList(userIds));
                    return ResponseUtil.createNormalJson("成功");
                default:
                    return ResponseUtil.createBussniessErrorJson(500, "错误的类型");
            }
        } catch (Exception e) {
            log.error("退出群聊且退出项目", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 批量添加群成员，如果没有创建过群聊，则第一个userId是群主（通过）
     *
     * @param type
     * @param id
     * @param userIds
     * @return
     */
    @RequestMapping(value = "/addUsers", method = RequestMethod.POST)
    public String addUsers(int type, int id, String[] userIds) {
        try {
            switch (type) {
                case 1:
                    groupService.addUsersOfProject(id, Arrays.asList(userIds));
                    return ResponseUtil.createBussniessJson("成功");
                case 2:
                    groupService.addUsersOfOrgan(id, Arrays.asList(userIds));
                    return ResponseUtil.createBussniessJson("成功");
                default:
                    return ResponseUtil.createBussniessErrorJson(500, "错误的类型");
            }
        } catch (Exception e) {
            log.error("批量添加群成员异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 移除群聊成员，如果群里只有一个人则删除群聊，如果是群主则先转让再退群（通过）
     *
     * @param
     * @param userId
     * @return
     */
    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public String removeUser(int type, int id, int userId) {
        try {
            switch (type) {
                case 1:
                    groupService.removeUserOfProject(id, userId);
                    return ResponseUtil.createBussniessJson("成功");
                case 2:
                    groupService.removeUserOfOrgan(id, userId);
                    return ResponseUtil.createBussniessJson("成功");
                default:
                    return ResponseUtil.createBussniessErrorJson(500, "错误的类型");
            }
        } catch (Exception e) {
            log.error("批量删除群成员异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 获取群成员信息
     *
     * @param userIds 用户ID数组
     * @return
     */
    @RequestMapping(value = "/queryUsersInfo", method = RequestMethod.GET)
    public String queryUsersInfo(Integer[] userIds) {
        try {
            return ResponseUtil.createNormalJson(groupService.queryUsersInfo(userIds));
        } catch (Exception e) {
            log.error("获取群成员信息异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 更换群主（通过）
     *
     * @param groupId    群聊ID
     * @param newOwnerId 新群主ID
     * @return
     */
    @RequestMapping(value = "/changeOwner", method = RequestMethod.POST)
    public String changeOwner(String groupId, int newOwnerId) {
        try {
            groupService.changeOwner(groupId, newOwnerId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("更换群主异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 获取添加用户列表
     *
     * @param id
     * @param groupType
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public String getUserList(int id, int groupType) {
        try {
            switch (groupType) {
                case 0:
                    return ResponseUtil.createNormalJson(groupService.getUsersOfAllProject(id), "成功");
                case 1:
                    return ResponseUtil.createNormalJson(groupService.getUsersOfProject(id), "成功");
                case 2:
                    return ResponseUtil.createNormalJson(groupService.getUsersOfOrgan(id), "成功");
                default:
                    return ResponseUtil.createBussniessErrorJson(500, "错误的类型");
            }
        } catch (Exception e) {
            log.error("获取用户列表异常", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 获取群聊添加用户的列表
     *
     * @param id
     * @param groupType
     * @return
     */
    @RequestMapping(value = "/getNewUserList", method = RequestMethod.GET)
    public String getNewUserList(@RequestParam("groupType") Integer groupType, @RequestParam("id") Integer id, @RequestParam("userId") Integer userId) {
        try {
            switch (groupType) {
                case 0:
                    return ResponseUtil.createNormalJson(groupService.getUsersOfAllProject(userId), "成功");
                case 1:
                    return ResponseUtil.createNormalJson(groupService.getUserListOfProjectGroup(id, userId), "成功");
                case 2:
                    return ResponseUtil.createNormalJson(groupService.getUserListOfOrganGroup(id, userId), "成功");
                default:
                    return ResponseUtil.createBussniessErrorJson(500, "错误的类型");
            }
        } catch (Exception e) {
            log.error("获取用户列表异常", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 创建默认群（所有未创建群的项目、班组）
     *
     * @return
     */
   /* @Deprecated
    @RequestMapping(value = "/createDefaultGroups", method = RequestMethod.POST)
    @ResponseBody
    public String createDefaultGroups() {
        List<Integer> errors = Lists.newArrayList();
        try {
            List<ProOrgan> proOrgans = groupService.queryNotGroup();
            for (ProOrgan proOrgan : proOrgans) {
                try {
                    groupService.createGroup(appendUrl("chatgroups"), token, proOrgan.getId(), getGroupDto(proOrgan));
                } catch (TransactionalException e) {
                    log.error("创建失败的proOrganId=" + proOrgan.getId());
                    errors.add(proOrgan.getId());
                    removeGroup(e.getMessage());    //删除无效群组
                } catch (Exception e) {
                    log.error("创建群组异常", e);
                    errors.add(proOrgan.getId());
                }
            }
        } finally {
            return ResponseUtil.createNormalJson(errors);
        }
    }*/


    /**
     * 向环信服务器获取所有群组以及群组详情
     *
     * @return
     */
   /* @Deprecated
    @RequestMapping(value = "/queryGroups", method = RequestMethod.GET)
    @ResponseBody
    public String queryGroups() {
        try {
            JSONObject responseGroups = HuanXinRequestUtils.queryGroups(appendUrl("chatgroups"), token);
            JSONArray groups = (JSONArray) responseGroups.get("data");
            for (int i = 0; i < groups.size(); i++) {
                String url = appendUrl("chatgroups", ((JSONObject) groups.get(i)).get("groupid"));
                JSONObject responseGroupDetails = HuanXinRequestUtils.queryGroupDetails(url, token);
                return ResponseUtil.createNormalJson(responseGroupDetails);
                //TODO 暂时只拿一条数据显示
            }
        } catch (Exception e) {
            buildExceptionResponse(e);
        }
        return null;
    }*/

}
