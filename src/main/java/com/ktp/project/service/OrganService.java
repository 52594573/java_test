package com.ktp.project.service;

import com.google.common.base.Strings;
import com.ktp.project.dao.*;
import com.ktp.project.dto.ProOrganCountDto;
import com.ktp.project.dto.ProOrganDto;
import com.ktp.project.dto.im.GroupUserDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.StringUtil;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by LinHon 2018/11/20
 */
@Service
@Transactional
public class OrganService {

    @Autowired
    private OrganDao organDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProOrganPerDao perDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private MessageSwitchDao messageSwitchDao;

    @Autowired
    private ModifyChannelDao modifyChannelDao;

    @Autowired
    private QueryChannelDao queryChannelDao;


    /**
     * 创建组织架构
     *
     * @param proOrgan
     */
    public void create(ProOrgan proOrgan) {
        if (proOrgan.getPoGzid() == null) {
            proOrgan.setPoGzid(24);  //工种类型-其他
        }
        proOrgan.setOperationTime(new Date());
        organDao.save(proOrgan);
    }


    /**
     * 创建机构（未分配），主要用于群聊添加成员的同时加入项目部
     *
     * @param projectId
     * @return
     */
    public Integer createFree(Integer projectId) {
        ProOrgan organ = new ProOrgan();
        organ.setProId(projectId);
        organ.setPoState(1);
        organ.setPoName("未分配");
        organ.setPoKqzt(1);
        organ.setPoKqzt(1);
        organ.setPoWzzt(1);
        organ.setPoGzid(24);  //工种类型-其他
        organ.setOperationTime(new Date());
        organ.setCreateType(1);
        modifyChannelDao.save(organ);
        return organ.getId();
    }




    /**
     * 判断是否需要发送
     *
     * @param project_id
     */
    public Project getPSent(Integer project_id) {
        return projectDao.queryOne(project_id);
    }

    /**
     * 更新
     *
     * @param proOrgan
     */
    public void update(ProOrgan proOrgan) {
        organDao.save(proOrgan);
    }


    /**
     * 删除组织机构，如果是班组则删除群聊，如果是部门则移除群聊成员或解散群聊
     *
     * @param organId
     */
    public void remove(int organId) {
        ProOrgan proOrgan = queryOne(organId);

        List<Integer> userIds = null;
        if (proOrgan.getPoState() == 1) {
            userIds = organDao.queryUserIdsByOrganId(organId);
        }

        organDao.executeUpdate("update ProOrganPer p set p.popState = %d where p.poId = %d", 4, organId);
        organDao.remove(proOrgan);

        //删除部门
        if (proOrgan.getPoState() == 1) {
            Project project = projectService.queryOne(proOrgan.getProId());
            //如果没有创建过群聊或者是空项目部则不做任何处理
            if (project.getGroupId() == null || userIds.size() == 0) {
                return;
            }
            List<ProOrgan> organs = organDao.queryManyByProjectId(project.getId(), 1);
            JSONArray result = JSONArray.fromObject(HuanXinRequestUtils.queryGroup(project.getGroupId()).get("data"));
            //如果当前剩余项目部个数大于0，且IM群聊人数大于当时删除人数则只移除成员（如果数据没问题的情况下）
            if (organs.size() > 0 && result.size() > userIds.size()) {
                HuanXinRequestUtils.removeUsers(project.getGroupId(), userIds);
            } else {
                //删除群聊
                String groupId = project.getGroupId();
                project.setGroupId(null);
                projectService.update(project);
                HuanXinRequestUtils.removeGroup(groupId);
            }
        }

        //删除班组
        if (proOrgan.getPoState() == 2 && !Strings.isNullOrEmpty(proOrgan.getGroupId())) {
            HuanXinRequestUtils.removeGroup(proOrgan.getGroupId());
        }
    }


    /**
     * 查询机构
     *
     * @param primaryKey
     * @return
     */
    public ProOrgan queryOne(int primaryKey) {
        return organDao.queryOne(primaryKey);
    }

    /**
     *更新机构
     */
    public  ProOrganPer updateOrgan(ProOrganDto dto) throws Exception {
        ProOrgan proOrgan = organDao.queryOne(dto.getPo_id());

        //判断跟新名称是否已经存在（判断是否重复）.
        List<ProOrgan> list = organDao.queryByPoIdAndName(proOrgan.getProId(), dto.getPo_name());


        if(list.size()>=1){
            if(list.size()>1){
                throw new Exception("机构名称已经存在多个");
            }
            if(list.get(0).getId().intValue()!=dto.getPo_id().intValue()){
                throw new Exception("机构名称已经存在");
            }
        }

        if(proOrgan.getPoFzr()==null){
            proOrgan.setPoFzr(0);
        }
        if(proOrgan.getCreatUid()==null){
            proOrgan.setCreatUid(0);
        }
        Project project = projectDao.queryOne(dto.getPro_id());
        /**
         *项目负责人，班组负责人，班组创建人可以修改
         */
        if(dto.getU_id().intValue()==proOrgan.getPoFzr().intValue()
                || dto.getU_id().intValue()==proOrgan.getCreatUid().intValue()
                || dto.getU_id().intValue() == project.getPPrincipal().intValue()){

            if(dto.getPo_kqzt()!=null && !"".equals(dto.getPo_kqzt())){
                proOrgan.setPoKqzt(dto.getPo_kqzt());
            }
            if(dto.getPo_gzid()!=null && !"".equals(dto.getPo_gzid())){
                proOrgan.setPoGzid(dto.getPo_gzid());
            }

            if(dto.getPro_id()!=null && !"".equals(dto.getPro_id())){
                proOrgan.setProId(dto.getPro_id());
            }

            if(dto.getPo_rzzt()!=null && !"".equals(dto.getPo_rzzt())){
                proOrgan.setPoRzzt(dto.getPo_rzzt());
            }

            if(!StringUtil.isEmpty(dto.getPo_name())){
                proOrgan.setPoName(dto.getPo_name());
            }
            if(!"".equals(dto.getPo_fzr())&&dto.getPo_fzr()!=null){
                proOrgan.setPoFzr(dto.getPo_fzr());
            }
            if(!"".equals(dto.getPo_state())&&dto.getPo_state()!=null){
                proOrgan.setPoState(dto.getPo_state());
            }

            organDao.saveOrUpdate(proOrgan);

        }else{
            throw new Exception("没权限修改");
        }
        return   perDao.queryProOrganByPoId(dto.getPo_id());

    }


    /**
     * 取得班组的所有用户信息
     *
     * @param organId
     * @return
     */
    public List<GroupUserDto> queryUsersOfOrgan(int organId) {
        return organDao.queryUsersOfOrgan(organId);
    }

    public List selectUsersOfOrgan(int organId) {
        return organDao.selectUsersOfOrgan(organId);
    }

    public List<ProOrgan> queryManyByProjectId(int projectId, int type) {
        return organDao.queryManyByProjectId(projectId, type);
    }

    public List<Integer> queryLeaderUserIds(int projectId, int leaderType) {
        return organDao.queryLeaderUserIds(projectId, leaderType);
    }

    public List<Integer> queryUserIds(int projectId, int organType) {
        return organDao.queryUserIds(projectId, organType);
    }

    /**
     * 取得项目部的所有用户信息
     *
     * @param projectId
     * @return
     */
    public List<GroupUserDto> queryUsersInfo(int projectId) {
        return organDao.queryUsersInfo(projectId);
    }


    /**
     * 根据群聊ID查询机构
     *
     * @param groupId
     * @return
     */
    public ProOrgan queryByGroupId(String groupId) {
        return organDao.queryByGroupId(groupId);
    }


    public ProOrganCountDto count(Integer projectId, Integer userId) {
        ProOrgan entry = organDao.findByPidAndPoFzr(projectId, userId);
        if (entry == null) {
            throw new BusinessException(String.format("通过项目ID: %s, 负责人ID: %s, 查询不到班组信息!", projectId, userId));
        }
        List<ProOrganCountDto.WorkerInfo> content = organDao.findWorkerInfo(projectId, userId);
        if (!CollectionUtils.isEmpty(content)) {
            for (ProOrganCountDto.WorkerInfo workerInfo : content) {
                if (userId.equals(workerInfo.getUserId())) {
                    workerInfo.setIsLead(1);
                }
            }
        }
        return new ProOrganCountDto().setPoId(entry.getId())
                .setPoName(entry.getPoName())
                .setTotalNum(content == null ? 0 : content.size())
                .setContent(content);
    }


    /**
     * 根据班组人员性别进行推送
     *
     * @return void
     * @params: []
     * @Author: wuyeming
     * @Date: 2018-12-7 下午 16:18
     */
    @Scheduled(cron = "0 0 9 ? * MON")//星期一上午9点推送
//    @Scheduled(cron = "0 1/1 * * * ?")// 测试用
    public void pushByAge() {
        String date = DateUtil.getFormatCurrentTime("yyyy-MM-dd");
        Map<String, Object> emApnsExt = new HashMap<>();
        emApnsExt.put("em_push_content", "年龄预警");
        List<Project> projectList = projectDao.getAllProject();
        for (Project project : projectList) {
            List<Integer> projectManagerList = new ArrayList<>();//项目/生产经理list
//            List<Object> testList = new ArrayList<>();//班组长list
            Map<String, Object> managerExtMap = new HashMap<>();
            List<Map<String, Object>> infoList = new ArrayList<>();//详情列表
            managerExtMap.put("date", date);
            managerExtMap.put("type", "ageWarning");//推送类型--年龄预警
            managerExtMap.put("projectName", project.getPName());//项目名
            managerExtMap.put("myUserName", project.getPName());//项目名
            //查询部门/班组
            List<ProOrgan> proOrganList = organDao.queryOrganProjectId(project.getId(), 1, 2);
            int organCount = 0;//男性年龄超过55岁的工人/女性年龄超过50岁的工人 的班组数
//            testList.add(951840);
//            testList.add(951751);
            for (ProOrgan proOrgan : proOrganList) {
                Map<String, Object> teamExtMap = new HashMap<>();
                int count = 0;
                List<Integer> teamManagerList = new ArrayList<>();//班组长list
                Map<String, Object> info = new HashMap<>();
                teamExtMap.put("date", date);
                teamExtMap.put("type", "ageWarning");//推送类型--年龄预警
                teamExtMap.put("organName", proOrgan.getPoName());//班组名
                teamExtMap.put("myUserName", project.getPName());//项目名
                info.put("organName", proOrgan.getPoName());
                int men = 0;//男性工人总数量
                int exceedMen = 0;//男性工人年龄超过55岁的数量
                int women = 0;//女性工人总数量
                int exceedWomen = 0;//女性工人年龄超过50岁的数量
                int exceedMenCount = 0;//男性工人年龄超过55岁的数量占男性工人总数量的占比
                int exceedWomenCount = 0;//女性工人年龄超过50岁的数量占女性工人总数量的占比
                //如果当前为班组，执行计算
                if (proOrgan.getPoState() == 2) {
                    //查询当前班组中男性数量
                    men = perDao.querySexAllCount(proOrgan.getId(), 1);
                    if (men > 0) {
                        //查询当前班组中年龄超过55岁的男性工人
                        exceedMen = perDao.querySexCount(proOrgan.getId(), 1);
                    }
                    //查询当前班组中女性数量
                    women = perDao.querySexAllCount(proOrgan.getId(), 2);
                    if (women > 0) {
                        //查询当前班组中年龄超过50岁的女性工人
                        exceedWomen = perDao.querySexCount(proOrgan.getId(), 2);
                    }
                    if (exceedMen > 0 && men > 0) {//如果当前班组中年龄超过55岁的男性工人数>0 && 当前班组中男性工人数>0
                        exceedMenCount = (Math.round((float) exceedMen / (float) men * 100));
                        if (exceedMenCount > 50) {
                            teamExtMap.put("men", String.valueOf(exceedMenCount) + "%");
                            info.put("men", String.valueOf(exceedMenCount) + "%");//存放到详情
                        }
                    }
                    if (exceedWomen > 0 && women > 0) {//如果当前班组中年龄超过50岁的女性工人数>0 && 当前班组中女性工人数>0
                        exceedWomenCount = (Math.round((float) exceedWomen / (float) women * 100));
                        if (exceedWomenCount > 50) {
                            teamExtMap.put("women", String.valueOf(exceedWomenCount) + "%");
                            info.put("women", String.valueOf(exceedWomenCount) + "%");//存放到详情
                        }
                    }
                    if (exceedMenCount > 50 || exceedWomenCount > 50) {
                        organCount++;
                        count++;
                        if(!infoList.contains(info)) {//如果详情列表当中不存在当前班组
                            infoList.add(info);//添加至详情列表
                        }
                    }
                }
                //查询班组长，项目经理，生产经理
                List<ProOrganPer> perList = perDao.queryUser(proOrgan.getId(), 8, 118, 120);
                for (ProOrganPer proOrganPer : perList) {
                    if (proOrganPer.getPopType() == 8) {//班组长
                        teamManagerList.add(proOrganPer.getUserId());
                    } else if (proOrganPer.getPopType() == 118 || proOrganPer.getPopType() == 120) {//项目经理/生产经理
                        projectManagerList.add(proOrganPer.getUserId());
                    }
                }
                //存在班组长
                if (teamManagerList.size() > 0) {
                    if (count > 0) {
                        teamExtMap.put("role", "teamManager");
                        teamExtMap.put("em_apns_ext", emApnsExt);
                        //筛选出忽略通知和不不略通知的用户
                        Map<String, List<Integer>> map = messageSwitchDao.filter(project.getId(), 1, teamManagerList);
                        List<Integer> ignoreList = map.get("ignore");
                        if (ignoreList != null && ignoreList.size() > 0) {//透传
                            HuanXinRequestUtils.sendMessage("project-" + project.getId(), ignoreList, teamExtMap, HuanXinRequestUtils.DEFAULT_MSG, true);
                        }
                        List<Integer> noticeList = map.get("notice");
                        if (noticeList != null && noticeList.size() > 0) {//通知
                            HuanXinRequestUtils.sendMessage("project-" + project.getId(), noticeList, teamExtMap, HuanXinRequestUtils.DEFAULT_MSG, false);
                        }
                    }
                }
            }
            //存在项目经理/生产经理
            if (projectManagerList.size() > 0) {
                if (organCount > 0) {
                    managerExtMap.put("organCount", organCount);
                    managerExtMap.put("infoList", infoList);
                    managerExtMap.put("role", "projectManager");
                    managerExtMap.put("em_apns_ext", emApnsExt);
                    //筛选出忽略通知和不不略通知的用户
                    Map<String, List<Integer>> map = messageSwitchDao.filter(project.getId(), 1, projectManagerList);
                    List<Integer> ignoreList = map.get("ignore");
                    if (ignoreList != null && ignoreList.size() > 0) {//透传
                        HuanXinRequestUtils.sendMessage("project-" + project.getId(), ignoreList, managerExtMap, HuanXinRequestUtils.DEFAULT_MSG, true);
                    }
                    List<Integer> noticeList = map.get("notice");
                    if (noticeList != null && noticeList.size() > 0) {//通知
                        HuanXinRequestUtils.sendMessage("project-" + project.getId(), noticeList, managerExtMap, HuanXinRequestUtils.DEFAULT_MSG, false);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        test();
    }

    private static void getManager() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> infoList = new HashMap<>();
        map.put("type", "ageWarning");
        map.put("date", "2018-12-12");
        map.put("role", "projectManager");
        map.put("projectName", "测试项目");
        map.put("organCount", "1");
        infoList.put("organName", "测试班组");
        infoList.put("men", "50%");
        infoList.put("women", "50%");
        map.put("infoList", infoList);
        System.out.println(GsonUtil.toJson(map));
    }

    private static void getTeam() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "ageWarning");
        map.put("date", "2018-12-12");
        map.put("role", "teamManager");
        map.put("organName", "测试班组");
        map.put("men", "50%");
        map.put("women", "50%");
        System.out.println(GsonUtil.toJson(map));
    }

    private static void test(){
        int test = Math.round((float) 4 / (float) 12*100);
        System.out.println(test);

    }

    public ProOrgan createBySystem(Integer projectId, Integer organId) {
        Project project = projectDao.getProjectById(projectId);
        if (project == null){
            throw new BusinessException(String.format("通过项目ID:%s查询不到匹配的项目", projectId));
        }
        if (organId.equals(-1)){
            String sql = "select * from pro_organ where po_name = ? and pro_id = ? and po_state = ? and create_type = ? ";
            List<ProOrgan> organs = queryChannelDao.selectMany(sql, Arrays.asList("未分配", projectId, 1, 1), ProOrgan.class);
            if (organs.size() > 1) {
                throw new RuntimeException("重复的部门名称，请联系客服处理");
            }
            if (!CollectionUtils.isEmpty(organs)){
                organId = organs.get(0).getId();
            }else {
                organId = createFree(projectId);
            }
        }
        return queryOne(organId).setProjectName(project.getPName());
    }
}
