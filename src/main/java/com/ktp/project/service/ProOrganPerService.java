package com.ktp.project.service;

import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.dao.ModifyChannelDao;
import com.ktp.project.dao.OrganDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Service
@Transactional
public class ProOrganPerService {

    private static Logger logger = LoggerFactory.getLogger(ProOrganPerService.class);
    private static final String baseFromId = "project-";

    @Autowired
    private ProOrganPerDAO proOrganPerDao;
    @Autowired
    private ProOrganDAO proOrganDAO;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private GroupService groupService;
    @Autowired
    private OrganDao organDao;
    @Autowired
    private ShareAndInviteService shareAndInviteService;
    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private ModifyChannelDao modifyChannelDao;

    public void createByIdCardOrErCode(Integer u_id, Integer pro_id, Integer po_id, Integer in_u_id, String in_u_sfz, Integer in_p_type) {
        // 当po_id为班组时候，此字段代表的是工种id，可为空，默认为班组工种；当po_id 为部门的时候，此字段代表：用户职务id，不可为空
//        ProOrgan organ = proOrganDAO.findById(po_id);
        //po_stateint(11) NULL机构类型1.项目部2.施工班组 todo
//        if (organ.getPoState().equals(1) && in_p_type == null) {
//            throw new BusinessException("当po_id为班组时候, in_p_type不可为空");
//        }
        //判断添加方式
        UserInfo user = null;
        if (StringUtils.isNotBlank(in_u_sfz)) {
            user = userInfoDao.getUserInfoByCardId(in_u_sfz);
        } else {
            user = userInfoDao.getUserInfoById(in_u_id);
        }
        if (user == null) {
            throw new BusinessException("未注册");
        }
        //加入到开太平组织
        shareAndInviteService.joinKtpFamilyByMobile(pro_id, po_id, user.getU_name(), u_id);
//        joinKtpFamily(pro_id, po_id, user.getId(), in_p_type, u_id);
    }

    public ProOrganPer delete(Integer u_id, Integer pro_id, Integer del_u_id, Integer del_po_id) {
        ProOrgan proOrgan = proOrganDAO.findById(del_po_id);
        if (proOrgan == null || !proOrgan.getProId().equals(pro_id)) {
            throw new BusinessException("通过班组ID查询不到结果或者班组绑定的项目ID与传入的项目ID不匹配");
        }
        ProOrganPer per = proOrganPerDao.findByUserIdAndPoId(del_po_id, del_u_id);
        if (per == null){
            throw new BusinessException("该员工已经删除");
        }
        per.setPopState(4);//设置为4 禁用
        proOrganPerDao.save(per);
        //如果该员工是班组长
        if (per.getPopType().equals(8)) {
            proOrgan.setPoFzr(0);
            proOrganDAO.save(proOrgan);
        }
        //把员工从聊天群主中删除 项目群和班组群
        Project project = projectService.queryOne(pro_id);
        if (proOrgan.getPoState() == 1) {
            boolean inGroup = HuanXinRequestUtils.isInGroup(project.getGroupId(), String.valueOf(del_u_id));
            if (inGroup) {
                groupService.removeUserOfProject(pro_id, del_u_id);
            }
        } else if (proOrgan.getPoState() == 2) {
            boolean inGroup = HuanXinRequestUtils.isInGroup(proOrgan.getGroupId(), String.valueOf(del_u_id));
            if (inGroup) {
                groupService.removeUserOfOrgan(del_po_id, del_u_id);
            }
        } else {
            throw new BusinessException("不正确的机构类型");
        }
        return per;
    }

    public ProOrganPer joinKtpFamily(Integer pId, Integer poId, Integer userId, Integer gzId, Integer inviteUserId) {
        //加入项目
        ProOrgan proOrgan = organDao.queryOne(poId);
        ProOrganPer per = proOrganPerDao.findByUserIdAndPoId(poId, userId);
        if (per == null) {
            per = new ProOrganPer();
            per.setPopIntime(new Timestamp(new Date().getTime()));
        }
        if (proOrgan.getPoState() == 1) {
            per.setPopType(7);
        } else {
            per.setPopType(4);
        }
        per.setpId(pId);
        per.setPoId(poId);
        per.setPopState(0);
        per.setPType(gzId);
        per.setUserId(userId);
        proOrganPerDao.save(per);
        logger.info("用户ID:{}加入项目成功", userId);

        //发送IM消息
        Project project = projectService.queryOne(pId);
        String fromUserId = baseFromId + pId;
        UserInfo inviteUser = userInfoDao.getUserInfoById(inviteUserId);
        String message = inviteUser.getU_nicheng() + "成功邀请你加入" + project.getPName() + "项目，进入项目页查看吧！";
        HuanXinRequestUtils.sendMessage(fromUserId, Lists.newArrayList(userId), new HashMap<>(), message);
        logger.info("用户ID:{},发送IM消息成功", userId);

        //加入群聊
        if (proOrgan.getPoState() == 1) {
            groupService.addUsersOfProject(pId, Arrays.asList(userId));
        } else if (proOrgan.getPoState() == 2) {
            groupService.addUsersOfOrgan(poId, Arrays.asList(userId));
        } else {
            throw new BusinessException("不正确的机构类型");
        }
        logger.info("用户ID:{},加入群主成功", userId);

        //推送到实名系统
        if (EnumMap.subclassMap.containsKey(pId)) {
            AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(pId);
            authRealNameApi.synBuildPo(poId, userId, "POUSERSAV");
            logger.info("用户ID:{},同步到实名系统成功", userId);
        }
        return per;
    }


    /**
     * 添加项目成员
     *
     * @param projectId
     * @param organId
     * @param userId
     */
    public void defaultCreate(Integer projectId, Integer organId, Integer popType, Integer userId) {
        ProOrganPer per = proOrganPerDao.findByUserIdAndPoId(organId, userId);
        if (per == null){
            per = new ProOrganPer();
            per.setPoId(organId);
            per.setpId(projectId);
            per.setUserId(userId);
            per.setPopType(popType);
            per.setPopJn(0.00);
            per.setPopXy(0.00);
            per.setPopCard(0L);
            per.setPopIntime(new Timestamp(System.currentTimeMillis()));
        }
        per.setPType(24);  //其他
        per.setPopState(0);
        modifyChannelDao.save(per);
    }


    /**
     * 删除项目成员
     *
     * @param projectId
     * @param userId
     */
    public void deleteByProject(Integer projectId, Integer userId) {
        String sql = "select pop.* from pro_organ po left join pro_organ_per pop on po.id = pop.po_id " +
                "where po.pro_id = ? and pop.user_id = ? and pop.pop_state = ?";
        queryChannelDao.selectMany(sql, Arrays.asList(projectId, userId, 0), ProOrganPer.class).forEach(item -> {
            item.setPopState(4);
            modifyChannelDao.update(item);
        });
    }


    /**
     * 删除班组成员
     *
     * @param organId
     * @param userId
     */
    public void deleteByOrgan(Integer organId, Integer userId) {
        String sql = "select pop.* from pro_organ_per pop where pop.po_id = ? and pop.user_id = ? and pop.pop_state = ?";
        queryChannelDao.selectMany(sql, Arrays.asList(organId, userId, 0), ProOrganPer.class).forEach(item -> {
            item.setPopState(4);
            modifyChannelDao.update(item);
        });
    }

}
