package com.ktp.project.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ktp.mq.common.KtpMqContants;
import com.ktp.mq.core.rocketmq.producer.KtpProducerMQ;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.dao.ModifyChannelDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.dto.im.MessageDto;
import com.ktp.project.dao.*;
import com.ktp.project.dto.share.*;
import com.ktp.project.entity.HatchUserRole;
import com.ktp.project.entity.OpenapiInitProject;
import com.ktp.project.entity.ShareCommit;
import com.ktp.project.entity.UserJf;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.impl.KtpCloudHelp;
import com.ktp.project.util.*;
import com.zm.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
@SuppressWarnings("ALL")
public class ShareAndInviteService {

    private static Logger logger = LoggerFactory.getLogger(ShareAndInviteService.class);
    private static Properties prop = PropertiesUtil.readConfig("/properties/application.properties");

    private static String ktpaideId = String.valueOf(prop.get("ktp.aide.id"));
    private static String ktpaideName = String.valueOf(prop.get("ktp.aide.name"));
    @Autowired
    private ModifyChannelDao modifyChannelDao;
    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private ShareService shareService;
    @Autowired
    private ProOrganPerDAO proOrganPerDAO;
    @Autowired
    private ProOrganDAO proOrganDAO;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserJfDao userJfDao;
    @Autowired
    private KeyContentDAO keyContentDAO;
    @Autowired
    private OrganDao organDao;
    @Autowired
    private com.ktp.project.service.realName.ZhzhAuthRealNameApi zhzhAuthRealNameApi;
    @Autowired
    private OpenapiInitProjectDao openapiInitProjectDao;

    public List<BrCodeInviteDto> queryInviteLogTopTen() {
        //cl.`sr_type` = 3 AND cl.sc_type = 3  AND
        String sql = "SELECT CAST( (@rowNO\\:=@rowNo + 1) AS SIGNED ) AS top, temp.* " +
                "FROM ( " +
                "    SELECT ui.`id` AS userId, ui.u_nicheng AS nickName, ui.`u_pic` AS headPortrait, COUNT(1) AS totalInviteNum,  MAX(cl.`sc_time`) nearTime  " +
                "    FROM share_commit cl " +
                "        LEFT JOIN user_info ui ON ui.`id` = cl.`sr_uid` " +
                "    WHERE ( cl.sc_reg = 1 ) " +
                "    GROUP BY cl.`sr_uid` " +
                "    ORDER BY totalInviteNum DESC , nearTime ASC " +
                ") temp, ( " +
                "        SELECT @rowNO\\:=0 " +
                "    ) b " +
                "LIMIT 10 ";
        List<BrCodeInviteDto> brCodeInviteDtos = null;
        try {
            brCodeInviteDtos = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), BrCodeInviteDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("统计查询邀请记录失败");
        }
        return brCodeInviteDtos;
    }

    public BrCodeInviteDto queryInviteLogByInviteId(Integer userInviteId) {
        String sql = "SELECT * " +
                "FROM ( " +
                " SELECT CAST( (@rowNO\\:=@rowNo + 1) AS SIGNED ) AS top, temp.* " +
                " FROM ( " +
                "  SELECT ui.`id` AS userId, ui.u_nicheng AS nickName, ui.`u_pic` AS headPortrait, COUNT(1) AS totalInviteNum,  MAX(cl.`sc_time`) nearTime   " +
                "  FROM share_commit cl " +
                "   LEFT JOIN user_info ui ON ui.`id` = cl.`sr_uid` " +
                "  WHERE cl.sc_reg = 1 " +
                "  GROUP BY cl.`sr_uid` " +
                "  ORDER BY totalInviteNum DESC, nearTime ASC " +
                " ) temp, ( " +
                "   SELECT @rowNO\\:= 0 " +
                "  ) b " +
                ") rank " +
                "WHERE rank.userId = ? ";
        BrCodeInviteDto brCodeInviteDto = null;
        try {
            brCodeInviteDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(userInviteId), BrCodeInviteDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过用户ID:%s查询用户分享排名失败", userInviteId));
        }
        return brCodeInviteDto;
    }

    public ShareContentDto queryShareContentByUserId(Integer projectId, Integer userId) {
        String sql = "SELECT p.`p_name` AS projectName, ui.`id` AS userId, ui.u_realname AS userName, ui.`u_pic` AS userHeadPic " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "LEFT JOIN user_info ui ON pop.`user_id` = ui.id " +
                "WHERE p.`id` = ? " +
                "AND pop.`user_id` = ? ";
        ShareContentDto dto = null;
        try {
            dto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(projectId, userId), ShareContentDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s,用户ID:%s查询分享内容失败", projectId, userId));
        }
        return dto;
    }

    public List<IsRegisterDto> judgePhoneIfRegister(Integer projectId, Integer organId, String phones) {
//        List<Integer> userIds = listUserIdByOrgan(projectId, organId);
        List<Integer> userIds = listUserIdByProjectId(projectId);
        List<String> lists = Arrays.asList(phones.split(","));
        List<IsRegisterDto> isRegisterDtos = new ArrayList<>();
        for (String phone : lists) {
            UserInfo userInfo = userInfoDao.getUserInfoByPhone(phone);
            int flag = userInfo == null ? 0 : 1;
            IsRegisterDto isRegisterDto = new IsRegisterDto();
            isRegisterDto.setMobile(phone).setIsRegister(flag).setIsJoinOrgan(0);
            isRegisterDto.setUserId(userInfo == null ? -1 : userInfo.getUser_id());
            //如果已经注册判断是否已经加入该项目
            if (flag == 1 && userIds.contains(userInfo.getId())) {
                isRegisterDto.setIsJoinOrgan(1);
            }
            isRegisterDtos.add(isRegisterDto);
        }
        return isRegisterDtos;
    }

    public boolean saveOrUpdateShareCommit(Integer userId, String commitTel, Integer srChannel) {
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(commitTel);
        if (userInfo != null) {
            throw new BusinessException("该用户已经注册了,请换个好友试试吧");
        }
        ShareCommit shareCommit = new ShareCommit();
        shareCommit.setShareUid(userId);
        shareCommit.setShareType(3);
        shareCommit.setShareCannel(srChannel);
        shareCommit.setCommitTel(commitTel);
        shareCommit.setCommitTime(new Date());
        shareCommit.setCommitType(3);
        shareCommit.setCommitRegister(0);
        shareCommit.setCommitCert(0);
        return shareService.saveOrUpdateShareCommit(shareCommit);
    }

    public synchronized void  joinKtpFamilyByMobile(Integer pId, Integer poId, String mobile, Integer inviteUserId) {

        logger.info("通过手机号加入班组项目ID:{}, 班组ID{}, 手机号{}, 推荐人ID{}",pId, poId, mobile, inviteUserId);
        ProOrgan proOrgan = proOrganDAO.findById(poId);
        if (proOrgan == null || !proOrgan.getProId().equals(pId)) {
            throw new BusinessException("通过班组ID查询不到结果或者班组绑定的项目ID与传入的项目ID不匹配");
        }
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(mobile);
        if (userInfo == null) {
            throw new BusinessException("未注册");
        }
        List<Integer> userIds = organDao.queryUserIdsByProjectId(pId);
        if (userIds.contains(userInfo.getId())) {
            throw new BusinessException("已在项目");
        }
        // 判断是否是孵化中心项目和人员
        Project project = projectService.queryOne(pId);
        if (project.getHatchStatus() != null && !project.getHatchStatus().equals(-1)) {
            if (project.getHatchStatus().equals(1)) {
                if (!judgeUserIsHatch(userInfo.getId())) {
                    throw new BusinessException("您不是孵化中心工人不能加入孵化中心项目哦!");
                }
            } else {
                throw new BusinessException(String.format("项目ID:%s,处于审核或者审核失败,不能添加工人", project.getId()));
            }
        }
        //加入项目
        Integer userId = userInfo.getId();
        ProOrganPer per = proOrganPerDAO.findByUserIdAndPoId(poId, userId);
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
        per.setPType(proOrgan.getPoGzid());
        per.setUserId(userId);
        proOrganPerDAO.save(per);
        logger.info("用户ID:{}加入项目成功", userId);

        //加入群聊和发送IM消息
        UserInfo inviteUser = userInfoDao.getUserInfoById(inviteUserId);
        String name = inviteUser == null ? "建信.开太平" : inviteUser.getU_nicheng();
        String message = name + "成功邀请你加入" + project.getPName() + "项目，进入项目页查看吧！";
        addHuanXinGroup(per, proOrgan, project);
        logger.info("用户ID:{},加入班组群成功", userId);
        //发送IM消息
        MessageDto messageDto = new MessageDto(ktpaideId, Lists.newArrayList(userId), "users");
        messageDto.setMsg("txt", message);
        messageDto.putExt("myUserName", ktpaideName);
        HuanXinRequestUtils.sendExtendMessage(messageDto);
        logger.info("用户ID:{},发送IM消息成功", userId);
        //发送MQ
        sendMq(pId, poId, userId);
    }

    private void sendMq(Integer pId, Integer poId, Integer userId){
        OpenapiInitProject project = openapiInitProjectDao.queryByProjectId(pId);
        if (project != null){
            ImmutableMap<String, Integer> map = ImmutableMap.of("poId", poId, "userId", userId);
            KtpProducerMQ.sendSimpleMessage(RealNameConfig.KTP_NORMAL_TOPIC, RealNameConfig.TAG_PRO_ORG_USER_ADD, JSON.toJSONString(map));
            logger.info("发送mq成功");
        }else {
            logger.info("非实名项目无需发送Mq");
        }
    }

    /**
     * 同步到ktpCloud系统
     * @param pId
     * @param poId
     * @param userId
     */
    private void sendToKtpCloud(Integer pId, Integer poId, Integer userId) {
        logger.info("项目ID: {}, 班组ID: {}, 用户ID:{},远程调用实名系统", pId, poId, userId);
//        if (EnumMap.subclassMap.containsKey(pId)) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("poId", poId);
            data.put("uId", poId);
            data.put("type", "POUSERSAV");
            String result = KtpCloudHelp.sendKtpCloud(RealNameConfig.SYN_POP, data, RequestMethod.POST);
            boolean flag = KtpCloudHelp.analyzeResult(result);
            if (flag){
                logger.info("项目ID: {}, 班组ID: {}, 用户ID:{},远程调用实名系统成功", pId, poId, userId);
            }
//        }
    }

    /**
     * 添加用户到环信群聊
     * @param per
     * @param proOrgan
     * @param project
     */
    private void addHuanXinGroup(ProOrganPer per, ProOrgan proOrgan, Project project) {
        logger.info("添加到环信聊天群 项目{}, 班组{}, 工人{}", GsonUtil.toJson(project), GsonUtil.toJson(proOrgan), GsonUtil.toJson(per));
        if (proOrgan.getPoState().equals(1)) {
            boolean inGroup = HuanXinRequestUtils.isInGroup(project.getGroupId(), String.valueOf(per.getUserId()));
            if (inGroup) {
                groupService.removeUserOfProject(per.getpId(), per.getUserId());
            }
            groupService.addUsersOfProject(per.getpId(), Arrays.asList(per.getUserId()));
        } else if (proOrgan.getPoState().equals(2)) {
            boolean inGroup = HuanXinRequestUtils.isInGroup(proOrgan.getGroupId(), String.valueOf(per.getUserId()));
            if (inGroup) {
                groupService.removeUserOfOrgan(proOrgan.getId(), per.getUserId());
            }
            groupService.addUsersOfOrgan(proOrgan.getId(), Arrays.asList(per.getUserId()));
        } else {
            throw new BusinessException("不正确的机构类型");
        }
    }

    public synchronized String joinKtpFamilyByUserIds(Integer pId, Integer poId, String userIds, Integer inviteUserId) {

        List<String> list = new ArrayList<>();
        ProOrgan proOrgan = proOrganDAO.findById(poId);
        if (proOrgan == null || !proOrgan.getProId().equals(pId)) {
            throw new BusinessException("通过班组ID查询不到结果或者班组绑定的项目ID与传入的项目ID不匹配");
        }
        List<UserInfo> infos = userInfoDao.getUserInfoByUserIdIn(userIds);
        if (CollectionUtils.isEmpty(infos)) {
            throw new BusinessException("未注册");
        }
        String[] split = userIds.split(",");
        if (infos.size() != split.length) {
            throw new BusinessException(String.format("您传入的用户ID串:%s有些不存在", userIds));
        }
        List<Integer> userList = listUserIdByProjectId(pId);
        ArrayList<Integer> userIdList = new ArrayList<>();
        Project project = projectService.queryOne(pId);
        AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(pId);
        UserInfo inviteUser = userInfoDao.getUserInfoById(inviteUserId);

        for (UserInfo info : infos) {
            Integer userId = info.getUser_id();
            if (userList.contains(userId)) {
                list.add(info.getId().toString());
                logger.error("用户ID:{},已在项目", info.getId());
                continue;
            }
            // 判断是否是孵化中心项目和人员
            if (project.getHatchStatus() != null && project.getHatchStatus().equals(1)) {
                if (!judgeUserIsHatch(userId)) {
                    logger.error("孵化中心项目不能添加非孵化中心用户!非孵化中心用户ID:{}", userId);
                    continue;
                }
            }
            // 判断是否是孵化中心项目和人员
            if (project.getHatchStatus() != null && !project.getHatchStatus().equals(-1)) {
                if (project.getHatchStatus().equals(1)) {
                    if (!judgeUserIsHatch(userId)) {
                        logger.error("孵化中心项目不能添加非孵化中心用户!非孵化中心用户ID:{}", userId);
                        continue;
                    }
                } else {
                    logger.error("项目ID:{},处于审核或者审核失败,不能添加工人", project.getId());
                    continue;
                }
            }
            //加入项目
            ProOrganPer per = proOrganPerDAO.findByUserIdAndPoId(poId, userId);
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
            per.setPType(proOrgan.getPoGzid());
            per.setUserId(userId);
            proOrganPerDAO.save(per);
            userIdList.add(userId);
            logger.info("用户ID:{}加入项目成功", userId);

            //加入群聊和发送IM消息
            String name = inviteUser == null ? "建信.开太平" : inviteUser.getU_nicheng();
            String message = name + "成功邀请你加入" + project.getPName() + "项目，进入项目页查看吧！";
            addHuanXinGroup(per, proOrgan, project);
            logger.info("用户ID:{},加入班组群成功", userId);
            //发送IM消息
            MessageDto messageDto = new MessageDto(ktpaideId, Lists.newArrayList(userId), "users");
            messageDto.setMsg("txt", message);
            messageDto.putExt("myUserName", ktpaideName);
            HuanXinRequestUtils.sendExtendMessage(messageDto);
            logger.info("用户ID:{},发送IM消息成功", userId);
            //发送MQ
            sendMq(pId, poId, userId);
        }

        return list.toString();
    }

    public boolean updateCommitRegisterByMobile(String mobile) {
        String hql = "from ShareCommit where  commitTel = ? order by commitTime  ";//shareType = 3 and commitType = 3 and
        List<ShareCommit> shareCommits = queryChannelDao.queryMany(hql, Lists.newArrayList(mobile), ShareCommit.class);
        if (CollectionUtils.isEmpty(shareCommits)) {
            logger.error("通过用户手机号:{} 查询不到结果", mobile);
//            throw new BusinessException(String.format("通过用户手机号:%s查询不到结果", mobile));
            return false;
        }
        ShareCommit one = shareCommits.get(0);
        one.setCommitRegister(1);


        if (shareService.saveOrUpdateShareCommit(one)) {
            //保存积分 --2018-12-27 wuyeming
            Date now = new Date();
            KeyContent keyContent = keyContentDAO.findById(183);
            String count = "+200";
            if (keyContent != null) {
                count = keyContent.getKeyValue();
            }
            UserJf userJf = new UserJf();
            userJf.setUserId(one.getShareUid());
            userJf.setJfShu(count);
            userJf.setJfType(1);
            userJf.setInTime(now);
            userJf.setJfState(183);
            userJf.setJfYue(DateUtil.getFormatDateTime(now, "yyyy-MM"));
            userJfDao.saveOrUpdate(userJf);
        }
        return true;
    }

    private List<Integer> listUserIdByOrgan(Integer projectId, Integer organId) {
        String sql = "SELECT DISTINCT pop.user_id " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE po.id = ? " +
                "AND p.`id` = ?";
        List list = null;
        try {
            list = queryChannelDao.selectMany(sql, Lists.newArrayList(organId, projectId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("根据班主ID:%s,查询该班主员工失败", organId));
        }
        return list;

    }

    public Map<String, List<ProjectFenceDto>> queryProjectFence(Integer projectId, Integer userId) {
        Map<Integer, ProjectFenceDto> map = new HashMap<>();
        List<Integer> projectIds = queryJoinProjectByUserId(userId);
        projectIds.remove(projectId);
        String in = StringUtils.strip(projectIds.toString(), "[]");
        if (StringUtils.isBlank(in)) {
            return ImmutableMap.of("proUserList", new ArrayList<>(map.values()));
        }
        String sql = "SELECT p.`p_name` AS pName, p.`id` AS proId, ui.`id` AS userId, ui.`u_pic` AS uPic, ui.`u_sex` AS uSex " +
                ", ui.`u_realname` AS uRealname, ui.u_nicheng AS uNicheng, kc.`key_name` AS popTypeName " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "LEFT JOIN user_info ui ON ui.`id` = pop.`user_id` " +
                "LEFT JOIN key_content kc ON pop.`pop_type` = kc.`id` " +
                "WHERE p.`id` IN ( " + in + " ) AND pop.`user_id` != " + userId + "  " +
                "GROUP BY  pop.`user_id` ";
        List<ProjectFenceUserInfoDto> fenceDtos = null;
        try {
            fenceDtos = queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(), ProjectFenceUserInfoDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("根据项目ID:%s,查询项目下用户信息失败!", in));
        }
        List<Integer> userIds = listUserIdByProjectId(projectId);
        if (!CollectionUtils.isEmpty(fenceDtos)) {
            for (ProjectFenceUserInfoDto fenceDto : fenceDtos) {
                if (userIds.contains(fenceDto.getUserId())) {
                    fenceDto.setExistReqProject(1);
                } else {
                    fenceDto.setExistReqProject(0);
                }
                if (!map.containsKey(fenceDto.getProId())) {
                    ProjectFenceDto dto = new ProjectFenceDto();
                    dto.setProId(fenceDto.getProId());
                    dto.setpName(fenceDto.getpName());
                    List<ProjectFenceUserInfoDto> proList = dto.getProList();
                    proList.add(fenceDto);
                    fenceDto.setpName(null);
                    fenceDto.setProId(null);
                    map.put(dto.getProId(), dto);
                } else {
                    ProjectFenceDto dto = map.get(fenceDto.getProId());
                    fenceDto.setpName(null);
                    fenceDto.setProId(null);
                    dto.getProList().add(fenceDto);
                    map.put(dto.getProId(), dto);
                }
            }
        }
        return ImmutableMap.of("proUserList", new ArrayList<>(map.values()));
    }


    private List<Integer> queryJoinProjectByUserId(Integer userId) {
        String sql = "SELECT DISTINCT p.`id` " +
                "FROM `pro_organ_per` pop " +
                "INNER JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "INNER JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE  p.`is_del` = 0 AND pop.`user_id` = ? ";
        List list = null;
        try {
            list = queryChannelDao.selectMany(sql, Lists.newArrayList(userId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("根据用户ID:%s,查询该用户所加入的班组失败!", userId));
        }
        return list;
    }

    private List<Integer> listUserIdByProjectId(Integer projectId) {
        String sql = "SELECT DISTINCT pop.`user_id` " +
                "FROM `pro_organ_per` pop " +
                "LEFT JOIN pro_organ po ON po.`id` = pop.`po_id` " +
                "LEFT JOIN project p ON po.`pro_id` = p.`id` " +
                "WHERE pop.pop_state = 0 and p.`id` = ? ";
        List list = null;
        try {
            list = queryChannelDao.selectMany(sql, Lists.newArrayList(projectId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("根据用户ID:%s,查询该用户所加入的班组失败!", projectId));
        }
        return list;
    }

    public Object saveOrUpdateShareCommit(Integer userId, String commitTel, Integer srChannel, Integer proId, Integer organid) {
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(commitTel);
        if (userInfo != null) {
            throw new BusinessException("该用户已经注册了,请换个好友试试吧");
        }
        ShareCommit shareCommit = new ShareCommit();
        shareCommit.setShareUid(userId);
        shareCommit.setShareType(3);
        shareCommit.setCommitProId(proId);
        shareCommit.setShareCannel(srChannel);
        shareCommit.setCommitTel(commitTel);
        shareCommit.setCommitTime(new Date());
        shareCommit.setCommitType(3);
        shareCommit.setCommitRegister(0);
        shareCommit.setCommitOrganid(organid);
        shareCommit.setCommitCert(0);
        return shareService.saveOrUpdateShareCommit(shareCommit);
    }

    public UserInfo getUserInfo(String mobile) {
        UserInfo userInfoByPhone = userInfoDao.getUserInfoByPhone(mobile);
        return userInfoByPhone;
    }

    public void joinKtpFamilyByPhone(Integer pId, Integer poId, String mobile, Integer inviteUserId) {

        ProOrgan proOrgan = proOrganDAO.findById(poId);
        if (proOrgan == null || !proOrgan.getProId().equals(pId)) {
            throw new BusinessException("通过班组ID查询不到结果或者班组绑定的项目ID与传入的项目ID不匹配");
        }
        UserInfo userInfo = userInfoDao.getUserInfoByPhone(mobile);
//        ShareCommit shareCommit = shareService.getShareCommit(mobile);
//        if(shareCommit!=null){
//            throw new BusinessException("已在项目,请勿重复加入，谢谢");
//        }
        if (userInfo == null) {
            throw new BusinessException("未注册");
        }
        List<Integer> userIds = organDao.queryUserIdsByProjectId(pId);

        if (userIds.contains(userInfo.getId())) {
            throw new BusinessException("已在项目");
        }

        //加入项目
        Integer userId = userInfo.getUser_id();
        ProOrganPer per = proOrganPerDAO.findByUserIdAndPoId(poId, userId);
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
        per.setPType(proOrgan.getPoGzid());
        per.setUserId(userId);
        proOrganPerDAO.save(per);
        logger.info("用户ID:{}加入项目成功", userId);

        //加入群聊和发送IM消息
        Project project = projectService.queryOne(pId);
        UserInfo inviteUser = userInfoDao.getUserInfoById(inviteUserId);
        String name = inviteUser == null ? "建信.开太平" : inviteUser.getU_nicheng();
        String message = name + "成功邀请你加入" + project.getPName() + "项目，进入项目页查看吧！";
        addHuanXinGroup(per, proOrgan, project);
        logger.info("用户ID:{},加入班组群成功", userId);
        //发送IM消息
        MessageDto messageDto = new MessageDto(ktpaideId, Lists.newArrayList(userId), "users");
        messageDto.setMsg("txt", message);
        messageDto.putExt("myUserName", ktpaideName);
        HuanXinRequestUtils.sendExtendMessage(messageDto);
        logger.info("用户ID:{},发送IM消息成功", userId);
        //发送MQ
        sendMq(pId, poId, userId);
    }

    public void insertProOrginPer(int commitProId, int commitOrganid, Integer uyserid) {
        ProOrgan proOrgan = proOrganDAO.findById(commitOrganid);
        //加入项目
        ProOrganPer per = proOrganPerDAO.findByUserIdAndPoId(commitOrganid, uyserid);
        if (per == null) {
            per = new ProOrganPer();
            per.setPopIntime(new Timestamp(new Date().getTime()));
        }
        if (proOrgan.getPoState() == 1) {
            per.setPopType(7);
        } else {
            per.setPopType(4);
        }
        per.setpId(commitProId);
        per.setPoId(commitOrganid);
        per.setPopState(0);
        per.setPType(proOrgan.getPoGzid());
        per.setUserId(uyserid);
        proOrganPerDAO.save(per);
        logger.info("用户ID:{}加入项目成功", uyserid);
    }

    public boolean judgeUserIsHatch(Integer userId) {
        String hql = "FROM HatchUserRole  where userId = ? ";
        List<HatchUserRole> one = queryChannelDao.queryMany(hql, Lists.newArrayList(userId), HatchUserRole.class);
        return !CollectionUtils.isEmpty(one);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ImmutableMap<String, Integer> map = ImmutableMap.of("poId", 22, "userId", 48366);
            KtpProducerMQ.sendSimpleMessage(KtpMqContants.KTP_NORMAL_TOPIC, KtpMqContants.TAG_PRO_ORG_USER_ADD, JSON.toJSONString(map));
        }
    }
}
