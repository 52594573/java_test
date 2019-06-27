package com.ktp.project.service;

import com.ktp.project.dao.BenefitDao;
import com.ktp.project.dao.PushLogBenefitDao;
import com.ktp.project.dao.UserInfoDao;
import com.ktp.project.dto.BenefitDto.CommentAndApply;
import com.ktp.project.dto.BenefitDto.DonateActAndDonRec;
import com.ktp.project.dto.BenefitH5Dto.BenefitActShareDto;
import com.ktp.project.dto.BenefitH5Dto.BenefitDonateShareDto;
import com.ktp.project.dto.BenefitH5Dto.BenefitRecipientDto;
import com.ktp.project.dto.*;
import com.ktp.project.entity.*;
import com.ktp.project.po.DonateActSearchPojo;
import com.ktp.project.util.HttpClientKTPCloundUtils;
import com.ktp.project.util.JPushClientUtil;
import com.zm.entity.KeyContent;
import com.zm.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 公益活动
 * @Author: liaosh
 * @Date: 2018/8/22 0022
 */
@Service
public class BenefitService {
    private static final Logger log = LoggerFactory.getLogger(BenefitService.class);
    @Autowired
    private BenefitDao benefitDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private PushLogBenefitDao pushLogBenefitDao;

    @Value("${jpush.env}")
    private String env;

    /**
     * 公益活动列表
     * benefitList
     *
     * @return:
     */
    @Transactional
    public List<BenefitActivity> getBenefitList(int page, int pageSize, int actStatus) {
        int sum = 0;
        List<BenefitActivity> activities = benefitDao.getBenefitList(page, pageSize, actStatus);
        //统计剩余数量
        for (BenefitActivity activity : activities) {
            sum = benefitDao.getActivityInventoryById(activity.getId());
            activity.setActInventorySum(sum);
        }

        return activities;
    }

    /**
     * 获取单个活动信息
     * benefit
     *
     * @return:
     */
    @Transactional
    public BenefitActivity getBenefit(int actId) {
        int sum = 0;
        BenefitActivity activitie = benefitDao.getBenefit(actId);
        if (activitie == null) {
            return null;
        }
        //统计剩余数量
        sum = benefitDao.getActivityInventoryById(activitie.getId());
        activitie.setActInventorySum(sum);

        return activitie;
    }

    /**
     * 活动详情-进行中-已结束
     *
     * @return:
     */
    @Transactional
    public List<DonateListDto> getDonateList(DonateActSearchPojo searchPojo) {
        List<DonateListDto> listDtos = new ArrayList<DonateListDto>();
        searchPojo.setPage((searchPojo.getPage() - 1) * searchPojo.getPageSize());

        // if(!TextUtils.isEmpty(searchPojo.getDonGoodsSort())){

        //搜索类型转换为数组
        String[] goos_sort = searchPojo.getDonGoodsSort().split(",");
        //转化为集合
        List<String> goos_sortList = Arrays.asList(goos_sort);

        listDtos = benefitDao.queryDonateList(searchPojo, goos_sortList);

        /*if (searchPojo.getDonGoodsSort() != null && !"".equals(searchPojo.getDonGoodsSort())) {
            listDtos = benefitDao.queryDonateList(searchPojo, goos_sort);
        } else {
            listDtos = benefitDao.queryDonateList(searchPojo);
        }*/

        // }


        Long joinSum = 0l;
        Long donSum = 0l;
        //统计捐赠和捐赠成功次数
        for (DonateListDto dto : listDtos) {
            joinSum = benefitDao.queryDonatejoinSum(dto.getUserId());
            donSum = (long) benefitDao.queryDonatedonSum(dto.getUserId());
            dto.setJoinSum(joinSum);
            dto.setDonSum(donSum);
        }

        return listDtos;
    }

    /**
     * 已捐赠成功列表
     *
     * @return:
     */
    @Transactional
    public List<DonateSeccssListDto> getdonateFinishListByActId(int actId) {
        return benefitDao.getdonateFinishList50ByActId(actId);
    }

    /**
     * 某个捐赠的成功捐赠列表
     *
     * @return:
     */
    @Transactional
    public List<DonateSeccssListDto> queryRecipientListByDonId(int donId) {
        return benefitDao.queryRecipientListByDonId(donId);
    }

    /**
     * 进入发布捐赠
     *
     * @param: donate
     * @return:
     */
    @Transactional
    public JobSelection getDonateSelection() {
        // DonateIniDto iniDto = new DonateIniDto();
        //物品类别,捐赠方式，捐赠邮费,物品单位
        List<KeyContent> selectionList = benefitDao.queryBenefitForJob();
        JobSelection jobSelection = new JobSelection();
        List<KeyContent> goodSortList = new ArrayList<>();
        List<KeyContent> wayList = new ArrayList<>();
        List<KeyContent> postageList = new ArrayList<>();
        List<KeyContent> unitList = new ArrayList<>();
        jobSelection.setGoodSortList(goodSortList);
        jobSelection.setWayList(wayList);
        jobSelection.setPostageList(postageList);
        jobSelection.setUnitList(unitList);
        if (selectionList != null && !selectionList.isEmpty()) {
            for (KeyContent keyContent : selectionList) {
                int keyId = keyContent.getKeyId();
                if (keyId == 18) {
                    goodSortList.add(keyContent);
                } else if (keyId == 19) {
                    wayList.add(keyContent);
                } else if (keyId == 20) {
                    postageList.add(keyContent);
                } else if (keyId == 21) {
                    unitList.add(keyContent);
                }
            }
        }

        return jobSelection;
    }

    /**
     * 通过id获取状态
     */

    @Transactional
    public int queryDonateStatusById(int id) {
        int i = 0;
        i = benefitDao.queryDonateStatusById(id);
        //拒绝重新编辑变为审核
        if (i == -1) {
            i = 0;
        }
        return i;
    }

    /**
     * 提交赠信息
     *
     * @param: donate
     * @return:
     */
    @Transactional
    public boolean InsertDonate(BenefitDonate donate) {
        //价格金额小数点最多2位
        donate.setDonPrince(new BigDecimal(donate.getDonPrince()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //获取剩余数量，减去剩余数量
        if (donate.getId() != null && !"".equals(donate.getId())) {
            int inv = benefitDao.getDonateInventoryById(donate.getId());
            inv = 0 - inv;
            benefitDao.updateActInventySum(donate.getDonActId(), inv);
        }
        //显示邮寄的
        benefitDao.insertDonate(donate);
        //benefitDao.updateActInventySum(donate.getDonActId(),donate.getDonSum());审核再添加，本来修改就不正确


        return true;
    }

    /**
     * 审核发布捐赠
     *
     * @param: donate
     * @return:
     */
    @Transactional
    public boolean updateDonateById(int donId) {
        int sum = 0;
        //donStatus为-1，捐赠审核不通过 1：通过
        //捐赠数量
        BenefitDonate donate = benefitDao.getDonateById(donId);
        if (donate == null || "".equals(donate.getActTop())) {
            log.info("审核对象不存在，审核对象状态必须为0");
            return false;
        }
        //这句已经废弃，试用统计
        benefitDao.addActInventorySum(donate.getDonActId(), donate.getDonSum());

        donate.setDonInventory(donate.getDonSum());
        donate.setDonStatus(1);
        //改变donate状态  1:活动id 2修改后状态 3：修改前状态
        if (benefitDao.updateDonate(donate))
            return true;
        return false;
    }

    /**
     * 编辑捐赠信息
     *
     * @param: donate
     * @return:
     */
    @Transactional
    public boolean updateDonate(BenefitDonate donate) {
        return benefitDao.updateDonate(donate);//更新
    }

    /**
     * 获取个人捐赠列表
     */
    @Transactional
    public List<BenefitDonate> getMyDonateList(int userId, int actId, int page, int pageSize) {
        page = (page - 1) * pageSize;
        if (actId == 0) {
            //返回个人所有捐赠
            return benefitDao.getMyDonateList(userId, page, pageSize);
        } else {
            //返回个人某个捐赠
            return benefitDao.getMyDonateListByActId(userId, actId, page, pageSize);
        }
    }

    /**
     * 1.12物品详情-捐赠者
     */
    @Transactional
    public DonateDetailDto myDonateDetail(int id) {
        DonateDetailDto dto = benefitDao.myDonateDetail(id);
        //参加次数
        dto.setJoinSum(benefitDao.queryDonatejoinSum(dto.getDonUserId()));
        //成功捐赠总数
        dto.setDonFinishSum(benefitDao.queryDonatedonSum(dto.getDonUserId()));
        //该发布成功捐赠数量（多少量，不是次）
        dto.setFinishSum((int) benefitDao.queryDonateAllSumById(id));

        return dto;
    }

    /**
     * 个人中心-申请列表
     */
    @Transactional
    public List<DonateApplyDetailDto> myDonateApplyDetailList(int userId, int recStatus, int page, int pageSize, int donId) {
        page = (page - 1) * pageSize;
        if (recStatus == 0) {
            benefitDao.updateDonateApplySum(0, userId);
            return benefitDao.myDonateApplyDetailList(userId, recStatus, page, pageSize, donId);
        } else {
            benefitDao.updateDonateCommentSumByUserId(0, userId);
            return benefitDao.myDonateFinishDetailList(userId, recStatus, page, pageSize, donId);
        }
    }

    /**
     * 个人中心-申请列表-批量或者单个捐赠
     */
    @Transactional
    public boolean updateRecipientList(List<BenefitRecipient> recipients) {
        for (BenefitRecipient recipient : recipients) {
            boolean bool = true;
            recipient.setRecDealTime(new Date());
            synchronized (recipient.getId()) {
                //跟新捐赠表的状态，数量，时间
                if (benefitDao.updateRecipentConsent(recipient) != 1) {
                    return false;
                }
            }
            synchronized (recipient.getRecDonId()) {
                if (benefitDao.updateDonateConsent(recipient) != 1) {
                    return false;
                }
                int actId = benefitDao.getActIdByDonateId(recipient.getRecDonId());
                //更新活动剩余量
                benefitDao.updateDonateActInvByActId(recipient.getRecActualSum(), actId);
            }

        }
        return true;
    }

    /**
     * 个人中心-物品详情-处理（个人中心-领取详情-处理）
     */
    @Transactional
    public boolean myBenefitRecipientDispose(BenefitRecipient recipient) {
        //撤回
        if (recipient.getRecStatus() == 0) {
            //查出提交申请数据
            BenefitRecipient recipientCell = benefitDao.BenefitRecipientById(recipient.getId());
            //回退库存捐赠库存
            benefitDao.BenefitDonateDisposereCall(recipientCell);


            //回退到活动总数量
            int actId = (int) benefitDao.getActIdByDonateId(recipientCell.getRecDonId());
            benefitDao.updateActInventySum(actId, recipientCell.getRecActualSum());


            //更新受曾表的实际捐赠数量和状态
            benefitDao.BenefitRecipientUpdateStatusAndrecActualSum(recipient);

        } else if (recipient.getRecStatus() == 3) {
            BenefitRecipient recipientCell = benefitDao.BenefitRecipientById(recipient.getId());
            BenefitDonate donate = benefitDao.getDonateById(recipientCell.getRecDonId());
            //确认领取
            benefitDao.BenefitRecipientUpdateStatusAndGetTime(recipient.getRecStatus(), recipient.getId(), new Date());

            //推送消息至客户端 --wuyeming - 2018-09-30
            String title = "领取提醒";
            String content = "您寄出的爱心物品已被确认领取啦！";
            BenefitDonate don = this.queryCommentAndApplyById(donate.getDonUserId(), donate.getDonActId(), donate.getId());
            Map<String, String> map = new HashMap<>();
            map.put("pageType", "0");
            map.put("notify", "1");
            map.put("messageType", "donate");//捐赠物品详情页
            map.put("messageId", donate.getDonActId() + "," + donate.getId());
            map.put("num", don.getDonApplySum() + "," + don.getDonCommentSum());
            map.put("pushType", "benefit");
            UserInfo userInfo = userInfoDao.getUserInfoById(donate.getDonUserId());
            if (userInfo != null && userInfo.getLast_device() != null) {
                String OS = userInfo.getLast_device();
                List<String> aliasList = new ArrayList<>();
                aliasList.add("KTP_" + env + "_" + OS + "_" + userInfo.getId());
                int count = JPushClientUtil.getInstance().pushDevice(aliasList, OS, title, content, map, "1");
                pushLogBenefitDao.savePushLogBenefit(3, recipientCell.getId(), recipientCell.getRecUserId(), userInfo.getId(), 0, 1, count);
            }

        } else { //发货
            BenefitRecipient recipientCell = benefitDao.BenefitRecipientById(recipient.getId());
            BenefitDonate donate = benefitDao.getDonateById(recipientCell.getRecDonId());
            int actId = benefitDao.getActIdByDonateId(recipientCell.getRecDonId());
            //受赠人数+1
            benefitDao.updateDonateActRecSum(actId);
            benefitDao.BenefitRecipientUpdateStatus(recipient.getRecStatus(), recipient.getId());

            //推送消息至客户端 --wuyeming - 2018-09-30
//            String content = "恭喜您成功获得爱心物品，记得去确认领取并留言哦！";
            String title = "发货提醒";
            String content = "恭喜您成功获得爱心物品，记得去确认领取并留言哦！";
            BenefitDonate don = this.queryCommentAndApplyById(recipientCell.getRecUserId(), actId, recipientCell.getRecDonId());
            Map<String, String> map = new HashMap<>();
            map.put("pageType", "0");
            map.put("messageType", "recInfo");//物品的领取详情页
            map.put("messageId", recipient.getId() + "");
            map.put("num", don.getDonApplySum() + "," + don.getDonCommentSum());
            map.put("notify", "1");
            map.put("pushType", "benefit");
            UserInfo userInfo = userInfoDao.getUserInfoById(recipientCell.getRecUserId());
            if (userInfo != null && userInfo.getLast_device() != null) {
                String OS = userInfo.getLast_device();
                List<String> aliasList = new ArrayList<>();
                aliasList.add("KTP_" + env + "_" + OS + "_" + userInfo.getId());
                int count = JPushClientUtil.getInstance().pushDevice(aliasList, OS, title, content, map, "1");
                pushLogBenefitDao.savePushLogBenefit(2, recipientCell.getId(), donate.getDonUserId(), userInfo.getId(), 0, 1, count);
            }
        }

        return true;
    }



    /**
     * 评论
     */
    @Transactional
    public boolean evaluate(BenefitEvaluate evaluate) throws Exception {
        try {
            evaluate.setEvaTime(new Date());
            //评论状态默认值为1
            evaluate.setEvaIsDel(1);
            benefitDao.BenefitEvaluateSaveOrUpdate(evaluate);

            //获取数据
            BenefitRecipient recipient = benefitDao.BenefitRecipientById(evaluate.getEvaRecId());
            if (recipient == null || "".equals(recipient)) {
                return false;
            }


            //评论数量
            benefitDao.updateDonateCommentSumId(recipient.getRecDonId());
            //状态
            benefitDao.BenefitRecipientUpdateStatus(4, recipient.getId());

            //推送消息至客户端 --wuyeming - 2018-09-30
            BenefitDonate donate = benefitDao.getDonateById(recipient.getRecDonId());
//            String content = "有人为您的爱心捐赠留言啦，快去看看吧！";
            String title = "留言提醒";
            String content = "有人为您的爱心捐赠留言啦，快去看看吧！";
            BenefitDonate don = this.queryCommentAndApplyById(donate.getDonUserId(), donate.getDonActId(), donate.getId());
            Map<String, String> map = new HashMap<>();
            map.put("pageType", "0");
            map.put("messageType", "donate");//捐赠物品详情页
            map.put("messageId", donate.getDonActId() + "," + donate.getId());
            map.put("num", don.getDonApplySum() + "," + don.getDonCommentSum());
            map.put("notify", "1");
            map.put("pushType", "benefit");
            UserInfo userInfo = userInfoDao.getUserInfoById(donate.getDonUserId());
            if (userInfo != null && userInfo.getLast_device() != null) {
                String OS = userInfo.getLast_device();
                List<String> aliasList = new ArrayList<>();
                aliasList.add("KTP_" + env + "_" + OS + "_" + userInfo.getId());
                int count = JPushClientUtil.getInstance().pushDevice(aliasList, OS, title, content, map, "1");
                pushLogBenefitDao.savePushLogBenefit(4, evaluate.getId(), recipient.getRecUserId(), userInfo.getId(), 0, 1, count);
            }
        } catch (Exception e) {
            throw e;
        }

        return true;

    }

    /**
     * 个人中心-领取详情-弹层
     *
     * @return:
     */
    @Transactional
    public List<MyRecipientListDto> myRecipientList(int page, int pageSize, int userId, int actId) {
        page = (page - 1) * pageSize;
        if (actId == 0) {
            return benefitDao.getRecipientList(page, pageSize, userId);
        } else {
            return benefitDao.getRecipientListByActId(page, pageSize, userId, actId);
        }


    }

    /**
     * 个人领取列表
     *
     * @return:
     */
    @Transactional
    public List<DonateSeccssListDto> getMyReceiveListByUserId(int page, int pageSize, int userId) {
        page = (page - 1) * pageSize;
        return benefitDao.getMyReceiveListByUserId(page, pageSize, userId);
    }

    /**
     * 个人中心-领取详情-（申请中心-已经寄出-受助成功）
     *
     * @return:
     */
    @Transactional
    public DonateSeccssListDto myReceiveDetail(int id) {
        return benefitDao.myReceiveDetail(id);
    }

    /**
     * 获取个人受赠列表
     */
    @Transactional
    public List<BenefitRecipient> getmyRecipientList(int userId, int page, int pageSize) {
        return benefitDao.getmyRecipientList(userId, page, pageSize);
    }

    /**
     * 申请领取（获取捐赠表信息）
     */
    @Transactional
    public BenefitDonate getapplyDonate(int id) {
        return benefitDao.getapplyDonate(id);
    }

    /**
     * 提交申请领取
     */
    @Transactional
    public int InsertRecipient(BenefitRecipient recipient) {

        BenefitDonate donate = benefitDao.getDonateById(recipient.getRecDonId());

        if (donate.getDonStatus() == 3) {
            return 3;
        } else if (recipient.getRecSum() > donate.getDonSum()) {
            return 2;
        } else if (donate.getDonStatus() == 0) {
            return 0;
        }
        //默认设置0
        recipient.setRecActualSum(0);
        //是否隐藏默认为不隐藏
        recipient.setRecIsDel(1);

        boolean fa = benefitDao.InsertRecipient(recipient);
        //评论数量+1
        int sum = benefitDao.updateDonateApplySumByDonId(1, recipient.getRecDonId());
        if (fa && sum == 1) {
            //推送消息至客户端 --wuyeming - 2018-09-29
//            String content = "有人申请领取您的爱心物品，快去处理吧！";
            String title = "申请提醒";
            String content = "有人申请领取您的爱心物品，快去处理吧！";
            BenefitDonate don = this.queryCommentAndApplyById(donate.getDonUserId(), donate.getDonActId(), donate.getId());
            Map<String, String> map = new HashMap<>();
            map.put("pageType", "0");
            map.put("messageType", "apply");//个人捐赠物品详情页的申请列表
            map.put("messageId", donate.getDonActId() + "," + donate.getId());
            map.put("num", don.getDonApplySum() + "," + don.getDonCommentSum());
            map.put("notify", "1");
            map.put("pushType", "benefit");
            UserInfo userInfo = userInfoDao.getUserInfoById(donate.getDonUserId());
            if (userInfo != null && userInfo.getLast_device() != null) {
                String OS = userInfo.getLast_device();
                List<String> aliasList = new ArrayList<>();
                aliasList.add("KTP_" + env + "_" + OS + "_" + userInfo.getId());
                int count = JPushClientUtil.getInstance().pushDevice(aliasList, OS, title, content, map, "1");
                pushLogBenefitDao.savePushLogBenefit(1, donate.getId(), recipient.getRecUserId(), userInfo.getId(), 0, 1, count);
            }
            return 1;
        }
        return 0;
    }

    /**
     *个人受赠详情
     */
   /* @Transactional
    public BenefitRecipient getMyReceiveDetail(int id){
        return benefitDao.getMyReceiveDetail(id);
    }*/

    /**
     * 进入编辑
     */
    @Transactional
    public BenefitDonate joinCompile(int donId) {
        //状态3是编辑中
        //benefitDao.joinCompile(3, donId);
        //10分钟内不比较状态改变
        //timing(donId, 600000l);
        return benefitDao.getDonateById(donId);

    }

    //定时
    public void timing(int donId, Long times) {
        Thread userInfoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("倒计时10分钟内不提交编辑信息将改变状态为进行中");
                try {
                    Thread.sleep(times);
                    benefitDao.updateDonateCompileToRun(3, donId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        userInfoThread.start();
        Thread.interrupted();

    }

    /**
     * 退出编辑
     */
    @Transactional
    public boolean exitCompile(int donId) {
        //状态3是编辑中
        if (benefitDao.updateDonateCompileToRun(3, donId) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /*---------------------------------------H5分享----------------------------------------------*/

    /**
     * 分享活动
     */
    @Transactional
    public BenefitActShareDto getActivityShare(int actId) {
        BenefitActShareDto shareDto = new BenefitActShareDto();
        //通过活动id获取活动
        BenefitActivity activity = benefitDao.getActById(actId);
        //剩余数量
        int sum = benefitDao.getActivityInventoryById(activity.getId());
        activity.setActInventorySum(sum);

        //该活动成功捐赠列表（有图片的5条）
        List<DonateSeccssListDto> listDtos = new ArrayList<DonateSeccssListDto>();
        listDtos = benefitDao.getdonateFinishListByActIdPicture(actId);

        if (listDtos.size() != 5) {
            //该活动成功捐赠列表（无论有没有图片，都返回没图片的5条）
            listDtos = benefitDao.getdonateFinishListByActId(actId);
            //没评论的没有返回，设置为空字符串
            for (DonateSeccssListDto dto : listDtos) {
                if (dto.getEvaPicture() == null) {
                    dto.setEvaPicture("");
                }
            }
        }

        //获取捐赠列表
        List<BenefitDonate> donates = benefitDao.getdonateListByActId(actId);

        shareDto.setActivity(activity);
        shareDto.setDonates(donates);
        shareDto.setRecipients(listDtos);
        return shareDto;
    }


    /**
     * 捐赠详情分享页
     */
    @Transactional
    public BenefitDonateShareDto getDonateShare(int actId, int donId) {
        BenefitDonateShareDto shareDto = new BenefitDonateShareDto();
        //通过活动id获取活动
        BenefitActivity activity = benefitDao.getActById(actId);
        //剩余数量
        int sum = benefitDao.getActivityInventoryById(activity.getId());
        activity.setActInventorySum(sum);

        //该捐赠详情
        DonateDetailDto dto = benefitDao.myDonateDetail(donId);
        //参加次数
        dto.setJoinSum(benefitDao.queryDonatejoinSum(dto.getDonUserId()));
        //成功捐赠总数
        dto.setDonFinishSum(benefitDao.queryDonatedonSum(dto.getDonUserId()));
        //该发布成功捐赠数量（多少量，不是次）
        dto.setFinishSum((int) benefitDao.queryDonateAllSumById(donId));

        //头像地址处理
        if (dto.getDonHead() != null && !"".equals(dto.getDonHead())) {
            if (dto.getDonHead().indexOf("http") == -1) {
                dto.setDonHead("https://tcs.ktpis.com" + dto.getDonHead());
            }
        } else {
            if (dto.getDonHead() != null) {
                // if(comment.getuSex()==1){//男
                dto.setDonHead("https://images.ktpis.com/images/pic/20181122154936224290102181.png");
                //}
                /*if(comment.getuSex()==2){//女
                    comment.setuUrl("https://images.ktpis.com/images/pic/20181122154936205290107119.png");
                }*/
            }
        }

        //获取捐赠列表
        List<DonateApplyDetailDto> detailDtos = benefitDao.myDonateFinishDetailList(donId);

        for (DonateApplyDetailDto dd : detailDtos) {
            if (!dd.getRecHead().contains("ktpis") || !dd.getRecHead().contains("https")) {
                dd.setRecHead("https://t.ktpis.com" + dd.getRecHead());
            }
        }

        shareDto.setActivity(activity);
        shareDto.setDonateDetailDto(dto);
        shareDto.setRecipientDetails(detailDtos);

        return shareDto;
    }

    /**
     * 领取详情分享页
     */
    @Transactional
    public BenefitRecipientDto getRecipientShare(int actId, int donId) {
        BenefitRecipientDto shareDto = new BenefitRecipientDto();
        //通过活动id获取活动
        BenefitActivity activity = benefitDao.getActById(actId);
        //剩余数量
        int sum = benefitDao.getActivityInventoryById(activity.getId());
        activity.setActInventorySum(sum);

        //该捐赠详情
        List<BenefitDonate> donateList = benefitDao.getdonateListByActId(actId);

        //获取捐赠列表
        List<DonateApplyDetailDto> detailDtos = benefitDao.myDonateFinishDetailList(donId);

        if (detailDtos != null && detailDtos.size() > 0) {
            int recId = detailDtos.get(0).getRecId();

            //获取捐赠详情
            DonateSeccssListDto listDto = benefitDao.myReceiveDetail(recId);

            if (listDto != null && listDto.getDonHead() != null) {
                if (!listDto.getDonHead().contains("ktpis") || !listDto.getDonHead().contains("https")) {
                    listDto.setDonHead("https://tcs.ktpis.com" + listDto.getDonHead());
                }

                if (!listDto.getRecHead().contains("ktpis") || !listDto.getRecHead().contains("https")) {
                    listDto.setRecHead("https://tcs.ktpis.com" + listDto.getRecHead());
                }
            }


//        //查询评论列表
//        List<BenefitEvaluate> evaList = benefitDao.getEvaByRecId(recId);

            shareDto.setActivity(activity);
//        shareDto.setEvaList(evaList);
            shareDto.setRecipientDetail(listDto);
//        shareDto.setDonates(donateList);
        }


        return shareDto;
    }

    /**
     * 通过活动id和捐赠id获取该活动捐赠成功列表和捐赠id中所捐赠成功列表详情
     */
    @Transactional
    public DonateActAndDonRec getActAndDonRecipientList(int actId, int donId, int page, int pageSize) {
        page = (page - 1) * pageSize;
        DonateActAndDonRec andDonRec = new DonateActAndDonRec();
        //通过活动id获取活动所捐赠成功列表
        andDonRec.setActRecipientList(benefitDao.getdonateFinishListByActId(page, pageSize, actId));

        //通过捐赠id获取该捐赠捐赠成功的列表
        andDonRec.setDonRecipientList(benefitDao.getdonRecDetail(page, pageSize, donId));

        return andDonRec;
    }

    /**
     * 获取用户总捐赠未查看评论数，总申请数
     */
    @Transactional
    public BenefitDonate queryCommentAndApplyById(int userId, int actId, int donId) {
        BenefitDonate donate = new BenefitDonate();
        //获取用户未查看评论总数和申请总数
        if (actId == 0 && donId == 0) {
            CommentAndApply commentAndApply = benefitDao.queryCommentAndApplyByUserId(userId);
            if (commentAndApply == null) {
                return null;
            }
            donate.setDonApplySum(commentAndApply.getDonApplySum().intValue());
            donate.setDonCommentSum(commentAndApply.getDonCommentSum().intValue());
        } else if (actId != 0 && donId == 0) {//获取用户某活动未查看评论总数和申请总数
            CommentAndApply commentAndApply = benefitDao.queryCommentAndApplyByUserIdAndActId(userId, actId);
            if (commentAndApply == null) {
                return null;
            }
            donate.setDonApplySum(commentAndApply.getDonApplySum().intValue());
            donate.setDonCommentSum(commentAndApply.getDonCommentSum().intValue());
        } else {
            CommentAndApply commentAndApply = benefitDao.queryCommentAndApplyByUserIdAndActIdAndDonId(userId, donId);
            if (commentAndApply == null) {
                return null;
            }
            donate.setDonApplySum(commentAndApply.getDonApplySum().intValue());
            donate.setDonCommentSum(commentAndApply.getDonCommentSum().intValue());
        }

        return donate;
    }


}
