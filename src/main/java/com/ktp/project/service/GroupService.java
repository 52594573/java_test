package com.ktp.project.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ktp.project.dao.GroupDao;
import com.ktp.project.dao.ProjectDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.im.GroupDto;
import com.ktp.project.dto.im.GroupUserDto;
import com.ktp.project.dto.im.MessageDto;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.ProOrgan;
import com.zm.entity.Project;
import com.zm.entity.UserInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by LinHon 2018/8/9
 */
@Service
@Transactional
public class GroupService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private OrganService organService;

    @Autowired
    private ProOrganPerService proOrganPerService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private QueryChannelDao queryChannelDao;


    /**
     * 查询群组用户信息
     *
     * @param userIds 用户ID数组
     * @return
     */
    public List queryUsersInfo(Integer[] userIds) {
        return groupDao.queryUsersInfo(userIds);
    }


    /**
     * 更换群主
     *
     * @param groupId
     * @param newOwnerId
     */
    public void changeOwner(String groupId, int newOwnerId) {
        HuanXinRequestUtils.changeOwner(groupId, newOwnerId);

        //TODO 接口需修改
        ProOrgan proOrgan = organService.queryByGroupId(groupId);
        if (proOrgan != null && proOrgan.getPoState() == 2) {
            UserInfo userInfo = userService.queryUserInfo(newOwnerId);
            StringBuffer buffer = new StringBuffer("班组长修改为\"").append(userInfo.getU_nicheng()).append("\"");

            MessageDto messageDto = new MessageDto("admin", Lists.newArrayList(groupId), "chatgroups");
            messageDto.setMsg("txt", buffer.toString());
            messageDto.putExt("type", "system");
            HuanXinRequestUtils.sendExtendMessage(messageDto);


        }
    }


    /**
     * 批量添加群成员，如果该班组没有创建过群聊，则第一个userId是群主
     *
     * @param organId
     * @param userIds
     */
    public void addUsersOfOrgan(int organId, List userIds) {
        ProOrgan proOrgan = organService.queryOne(organId);
        if (Strings.isNullOrEmpty(proOrgan.getGroupId())) {
            Project project = projectService.queryOne(proOrgan.getProId());
            List<String> members = userIds.subList(1, userIds.size());
            String groupName = project.getPName() + "-" + proOrgan.getPoName();
            proOrgan.setGroupId(doCreate(proOrgan.getId(), groupName, userIds.get(0).toString(), "2", members));
            organService.update(proOrgan);
        } else {
            HuanXinRequestUtils.addUsers(proOrgan.getGroupId(), userIds);
        }
    }


    /**
     * 批量添加群成员，如果该项目没有创建过群聊，则第一个userId是群主
     *
     * @param projectId
     * @param userIds
     */
    public void addUsersOfProject(int projectId, List userIds) {
        Project project = projectService.queryOne(projectId);
        if (Strings.isNullOrEmpty(project.getGroupId())) {
            List<String> members = userIds.subList(1, userIds.size());
            project.setGroupId(doCreate(projectId, project.getPName(), userIds.get(0).toString(), "1", members));
            projectService.update(project);
        } else {
            HuanXinRequestUtils.addUsers(project.getGroupId(), userIds);
        }
    }


    /**
     * 移除班组群聊成员
     *
     * @param organId
     * @param userId
     */
    public void removeUserOfOrgan(int organId, int userId) {
        ProOrgan proOrgan = organService.queryOne(organId);
        if (!HuanXinRequestUtils.removeUser(proOrgan.getGroupId(), userId)) {
            proOrgan.setGroupId(null);
            organService.update(proOrgan);
        }
    }


    /**
     * 移除项目群聊成员
     *
     * @param projectId
     * @param userId
     */
    public void removeUserOfProject(int projectId, int userId) {
        Project project = projectService.queryOne(projectId);
        if (!HuanXinRequestUtils.removeUser(project.getGroupId(), userId)) {
            project.setGroupId(null);
            projectService.update(project);
        }
    }


    /**
     * 执行创建群聊操作
     *
     * @param id
     * @param groupName
     * @param ownerId
     * @param groupType
     * @param userIds
     * @return
     */
    public String doCreate(int id, String groupName, String ownerId, String groupType, List userIds) {
        Map<String, String> data = Maps.newHashMap();
        data.put("id", String.valueOf(id));
        data.put("groupType", groupType);

        GroupDto groupDto = new GroupDto();
        groupDto.setGroupName(groupName);
        groupDto.setOwner(ownerId);
        groupDto.setDesc(JSONObject.fromObject(data).toString());
        groupDto.setMembers(userIds);
        return HuanXinRequestUtils.createGroup(groupDto);
    }


    /**
     * 取得所有项目的用户信息
     *
     * @param userId
     * @return
     */
    public Map<String, Object> getUsersOfAllProject(int userId) {
        List result = Lists.newArrayList();
        List<Object[]> projectsInfo = projectService.getSubordinateProjectsId(userId);
        for (int i = 0; i < projectsInfo.size(); i++) {
            int projectId = Integer.parseInt(projectsInfo.get(i)[0].toString());
            List<GroupUserDto> users = organService.queryUsersInfo(projectId);
            Map<String, Object> data = Maps.newHashMap();
            data.put("proId", projectId);
            data.put("pName", projectsInfo.get(i)[1]);
            data.put("proList", users);
            result.add(data);
        }
        return ImmutableMap.of("proUserList", result);
    }


    public Map<String, Object> getUserListOfOrganGroup(Integer organId, Integer userId) {
        Integer projectId = queryChannelDao.queryOne(ProOrgan.class, organId).getProId();
        return getUserListOfProjectGroup(projectId, userId);
    }


    public Map<String, Object> getUserListOfProjectGroup(Integer projectId, Integer userId) {
        List result = Lists.newArrayList();
        List<Object[]> projectsInfo = projectService.getSubordinateProjectsId(userId);
        projectsInfo.forEach(item -> {
            Integer proId = Integer.parseInt(item[0].toString());
            if (!projectId.equals(proId)) {
                Map<String, Object> data = Maps.newHashMap();
                data.put("proId", proId);
                data.put("pName", item[1]);
                data.put("proList", organService.queryUsersInfo(proId));
                result.add(data);
            }
        });
        return ImmutableMap.of("proUserList", result);
    }


    /**
     * 取得项目部的所有用户信息
     *
     * @param projectId
     * @return
     */
    public Map<String, Object> getUsersOfProject(int projectId) {
        Project project = projectService.queryOne(projectId);
        List<GroupUserDto> users = organService.queryUsersInfo(projectId);
        return ImmutableMap.of("proUserList", Lists.newArrayList(convertResult(project, users)));
    }


    /**
     * 取得班组的所有用户信息
     *
     * @param organId
     * @return
     */
    public Map<String, Object> getUsersOfOrgan(int organId) {
        ProOrgan proOrgan = organService.queryOne(organId);
        Project project = projectService.queryOne(proOrgan.getProId());
        List<GroupUserDto> users = organService.queryUsersOfOrgan(proOrgan.getId());
        return ImmutableMap.of("proUserList", Lists.newArrayList(convertResult(project, users)));
    }


    public Map<String, Object> convertResult(Project project, List<GroupUserDto> users) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("proId", project.getId());
        result.put("pName", project.getPName());
        result.put("proList", users);
        return result;
    }


    /**
     * 加入群聊且加入项目
     *
     * @param projectId
     * @param userIds
     */
    public void joinGroupAndProject(Integer projectId, List<Integer> userIds) {
        Project project = queryChannelDao.queryOne(Project.class, projectId);
        if (project == null || Strings.isNullOrEmpty(project.getGroupId())) {
            throw new RuntimeException("项目或群聊不存在");
        }
        String sql = "select * from pro_organ where po_name = ? and pro_id = ? and po_state = ? and create_type = ? ";
        List<ProOrgan> organs = queryChannelDao.selectMany(sql, Arrays.asList("未分配", projectId, 1, 1), ProOrgan.class);
        if (organs.size() > 1) {
            throw new RuntimeException("重复的部门名称，请联系客服处理");
        }
        final Integer organId;
        if (organs.size() == 0) {
            organId = organService.createFree(project.getId());
        } else {
            organId = organs.get(0).getId();
        }
        doJoin(project.getId(), organId, 7, project.getGroupId(), userIds);
    }


    /**
     * 加入群聊且加入班组
     *
     * @param organId
     * @param userIds
     */
    public void joinGroupAndOrgan(Integer organId, List<Integer> userIds) {
        ProOrgan organ = queryChannelDao.queryOne(ProOrgan.class, organId);
        if (organ == null || Strings.isNullOrEmpty(organ.getGroupId())) {
            throw new RuntimeException("班组或群聊不存在");
        }
        doJoin(organ.getProId(), organId, 4, organ.getGroupId(), userIds);
    }


    /**
     * 加入群聊
     *
     * @param projectId
     * @param organId
     * @param groupId
     * @param userIds
     */
    public void doJoin(Integer projectId, Integer organId, Integer popType, String groupId, List<Integer> userIds) {
        userIds.stream().map(item -> {
            if (!projectService.isInProject(projectId, item)) {
                proOrganPerService.defaultCreate(projectId, organId, popType, item);
            }
            return item;
        }).collect(Collectors.toList());
        HuanXinRequestUtils.addUsers(groupId, userIds);
    }


    /**
     * 退出群聊且退出项目
     *
     * @param projectId
     * @param userIds
     */
    public void quitGroupAndProject(Integer projectId, List<Integer> userIds) {
        Project project = queryChannelDao.queryOne(Project.class, projectId);
        if (project == null || Strings.isNullOrEmpty(project.getGroupId())) {
            throw new RuntimeException("项目或群聊不存在");
        }
        userIds.forEach(item -> proOrganPerService.deleteByProject(projectId, item));
        HuanXinRequestUtils.removeUsers(project.getGroupId(), userIds);
    }


    /**
     * 退出群聊且退出班组
     *
     * @param organId
     * @param userIds
     */
    public void quitGroupAndOrgan(Integer organId, List<Integer> userIds) {
        ProOrgan organ = queryChannelDao.queryOne(ProOrgan.class, organId);
        if (organ == null || Strings.isNullOrEmpty(organ.getGroupId())) {
            throw new RuntimeException("班组或群聊不存在");
        }
        userIds.forEach(item -> proOrganPerService.deleteByOrgan(organId, item));
        HuanXinRequestUtils.removeUsers(organ.getGroupId(), userIds);
    }
}
