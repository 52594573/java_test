package com.ktp.project.service;

import com.google.common.collect.Lists;
import com.ktp.project.dao.ProjectDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.*;
import com.ktp.project.exception.TransactionalException;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.Project;
import com.zm.entity.ProjectCollectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-05 11:35
 */
@Service("proService")
@Transactional
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private ProjectDao proDao;

    @Autowired
    private QueryChannelDao queryChannelDao;

    /**
     * 保存项目
     *
     * @return boolean
     * @params: [project]
     * @Author: wuyeming
     * @Date: 2018-10-05 15:40
     */
    public Integer saveProject(Project project) {
        return proDao.saveProject(project);
    }

    /**
     * 获取项目信息
     *
     * @return com.ktp.project.dto.ProjectDto
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-05 15:40
     */
    public ProjectDto getProjectInfo(Integer id) {
        return proDao.getProjectInfo(id);
    }

    /**
     * 项目统计-获取项目详情
     *
     * @return com.ktp.project.dto.ProjectDetailDto
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-06 16:41
     */
    public ProjectDetailDto getProjectDetail(Integer id) {
        return proDao.getProjectDetail(id);
    }

    /**
     * 项目统计-获取工种信息
     *
     * @return java.util.List<com.ktp.project.dto.ProjectDeptDto>
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-08 09:35
     */
    public List<ProjectDeptDto> getDeptList(Integer id) {
        return proDao.getDeptList(id);
    }

    /**
     * 根据城市编码获取城市id
     *
     * @return java.lang.Integer
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-09 09:27
     */
    public Integer getCityId(Integer id) {
        return proDao.getCityId(id);
    }


    /**
     * 根据主键查询
     *
     * @param primaryKey
     * @return
     */
    public Project queryOne(int primaryKey) {
        return proDao.queryOne(primaryKey);
    }


    /**
     * 创建项目
     *
     * @param projectName
     * @param userId
     */
    public int create(String projectName, int userId, Integer hatchStatus) {
//        UserInfo user = userInfoDao.getUserInfoById(userId);
//        if (!user.getHatchRole().equals(2)){
//            throw new BusinessException("您没有创建孵化项目的权限!");
//        }

        //创建项目
        Project project = new Project();
        project.setPName(projectName);
        project.setPPrincipal(userId);
        project.setPCreater(userId);
        project.setIsDel(0);
        project.setPState(0);
        project.setPIntime(new Timestamp(System.currentTimeMillis()));
        project.setPCreateType(1);
        project.setHatchStatus(hatchStatus);
        proDao.create(project);

        //创建项目助手
        HuanXinRequestUtils.register("project-" + project.getId());

        //创建群聊
        String groupId = groupService.doCreate(project.getId(), projectName, String.valueOf(userId), "1", Lists.newArrayList());

        //更新群ID，如失败则删除群聊
        try {
            project.setGroupId(groupId);
            proDao.update(project);
            return project.getId();
        } catch (Exception e) {
            log.error("创建项目异常", e);
            throw new TransactionalException(groupId);
        }
    }


    /**
     * 查询用户所在项目的ID
     *
     * @param userId
     * @return
     */
    public List getSubordinateProjectsId(int userId) {
        return proDao.getSubordinateProjectsId(userId);
    }


    /**
     * 更新项目
     *
     * @param project
     */
    public void update(Project project) {
        proDao.update(project);
    }

    public List<ProjectCollectId> getProjectCollectId() {
        return proDao.getProjectCollectId();
    }

    public List<Integer> getProjectId() {
        return proDao.getProjectId();
    }

    public List<ProGZKaoQinLvDto> getProGZKaoQinLv(Integer proId) {
        return proDao.getProGZKaoQinLv(proId);
    }

    public List<WorkingHoursDto> getWorkingHours() {
        return proDao.getWorkingHours();
    }

    public String getProjectName(Integer proId) {
        return proDao.getProjectName(proId);
    }


    /**
     * 判断用户是否已经加入项目
     *
     * @param projectId
     * @param userId
     * @return
     */
    public Boolean isInProject(Integer projectId, Integer userId) {
        String sql = "select count(1) from pro_organ po left join pro_organ_per pop on po.id = pop.po_id " +
                "where po.pro_id = ? and pop.user_id = ? and pop.pop_state = ?";
        return queryChannelDao.selectCount(sql, Arrays.asList(projectId, userId, 0)) >= 1L;
    }
}
