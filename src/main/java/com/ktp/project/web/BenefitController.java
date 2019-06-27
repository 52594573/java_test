package com.ktp.project.web;


import com.ktp.project.dao.BenefitDao;
import com.ktp.project.dao.UserJfDao;
import com.ktp.project.dto.DonateListDto;
import com.ktp.project.dto.DonateSeccssListDto;
import com.ktp.project.dto.MyRecipientListDto;
import com.ktp.project.entity.*;
import com.ktp.project.po.DonateActSearchPojo;
import com.ktp.project.service.BenefitService;
import com.ktp.project.service.UserJfService;
import com.ktp.project.util.*;
import com.ktp.project.util.redis.RedisClientTemplate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 公益活动
 * @Author: liaosh
 * @Date: 2018/8/22 0022
 */
@Controller
@RequestMapping(value = "api/benefit", produces = "application/json;charset=UTF-8;")
public class BenefitController {
    private static final Logger log = LoggerFactory.getLogger(BenefitController.class);

    @Autowired
    private BenefitService benefitService;

    @Autowired
    private UserJfDao userJfDao;

    @Autowired
    private BenefitDao benefitDao;

    /**
     * 获取公益活动列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "benefitList", method = RequestMethod.GET)
    @ResponseBody
    public String getBenefitList(@Param(value = "page") int page, @Param(value = "pageSize") int pageSize, @Param(value = "actStatus") int actStatus) {
        log.info("获取活动列表");
        try {
            List<BenefitActivity> benfs = benefitService.getBenefitList(page, pageSize, actStatus);
            for (BenefitActivity benf : benfs) {
                Long deadline = benf.getActETime().getTime() - System.currentTimeMillis();
                if (deadline <= 0) {
                    deadline = 0l;
                } else {
                    deadline = deadline / (1000 * 60 * 60 * 24) + 1l;
                }
                benf.setDeadline(deadline);
            }
            //TODO 暂时处理没有活动时返回一个空对象--完成后需去除
            if (benfs.isEmpty()) {
                benfs.add(new BenefitActivity());
            }
            return ResponseUtil.createNormalJson(benfs);
        } catch (Exception e) {
            log.error("获取个人捐赠列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取个人捐赠列表异常", e);
        }
    }

    //获取单个活动信息
    @RequestMapping(value = "benefit", method = RequestMethod.GET)
    @ResponseBody
    public String getBenefitList(@Param(value = "actId") int actId) {
        log.info("获取活动列表");
        try {
            BenefitActivity benf = benefitService.getBenefit(actId);
            if (benf == null) {
                return null;
            }
            Long deadline = benf.getActETime().getTime() - System.currentTimeMillis();
            if (deadline <= 0) {
                deadline = 0l;
            } else {
                deadline = deadline / (1000 * 60 * 60 * 24) + 1l;
            }
            benf.setDeadline(deadline);

            return ResponseUtil.createNormalJson(benf);
        } catch (Exception e) {
            log.error("获取个人捐赠列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取个人捐赠列表异常", e);
        }
    }

    /**
     * 活动详情列表-进行中-已结束
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "donateList", method = RequestMethod.GET)
    @ResponseBody
    public String getDonateList(DonateActSearchPojo searchPojo) {
        log.info("活动详情列表-进行中-已结束");
        try {
            List<DonateListDto> donateListDtos = benefitService.getDonateList(searchPojo);
            return ResponseUtil.createNormalJson(donateListDtos);
        } catch (Exception e) {
            log.error("活动详情列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "活动详情列表异常", e);
        }
    }

    /**
     * 已捐赠成功列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "donateFinishList", method = RequestMethod.GET)
    @ResponseBody
    public String getdonateFinishListByActId(@Param(value = "actId") int actId) {
        log.info("已捐赠成功列表");
        try {
            List<DonateSeccssListDto> dtos = benefitService.getdonateFinishListByActId(actId);
            return ResponseUtil.createNormalJson(dtos);
        } catch (Exception e) {
            log.error("已捐赠成功列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "已捐赠成功列表异常", e);
        }
    }


    /**
     * 个人中心-领取详情-弹层(获取个人捐赠列表)
     */
    @RequestMapping(value = "getMyDonateList", method = RequestMethod.GET)
    @ResponseBody
    public String getMyDonateList(HttpServletRequest request) {
        log.info("个人捐赠列表");
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int page = Integer.parseInt(request.getParameter("page"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            int actId = Integer.parseInt(request.getParameter("actId"));
            return ResponseUtil.createNormalJson(benefitService.getMyDonateList(userId, actId, page, pageSize));
        } catch (Exception e) {
            log.error("获取个人捐赠列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取个人捐赠列表异常", e);
        }
    }

    /**
     * 物品详情-捐赠者
     */
    @RequestMapping(value = "myDonateDetail", method = RequestMethod.GET)
    @ResponseBody
    public String myDonateDetail(HttpServletRequest request) {
        log.info("个人捐赠列表");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            return ResponseUtil.createNormalJson(benefitService.myDonateDetail(id));
        } catch (Exception e) {
            log.error("获取个人捐赠列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取个人捐赠列表异常", e);
        }
    }

    /**
     * 个人中心-申请列表  / 个人中心-物品详情
     */
    @RequestMapping(value = "myDonateApplyDetailList", method = RequestMethod.GET)
    @ResponseBody
    public String myDonateApplyDetailList(@Param(value = "userId") int userId, @Param(value = "recStatus") int recStatus,
                                          @Param(value = "page") int page, @Param(value = "pageSize") int pageSize,
                                          @Param(value = "donId") int donId) {
        log.info("个人捐赠列表");
        try {
            return ResponseUtil.createNormalJson(benefitService.myDonateApplyDetailList(userId, recStatus, page, pageSize, donId));
        } catch (Exception e) {
            log.error("获取个人捐赠列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取个人捐赠列表异常", e);
        }
    }

    /**
     * 个人中心-申请列表-批量或者单个捐赠
     */
    @RequestMapping(value = "myDonateDisposeList", method = RequestMethod.POST)
    @ResponseBody
    public String updateRecipientList(@Param(value = "recipients") String recipients) {
        log.info("人中心-申请列表-批量或者单个捐赠");
        if (!TextUtils.isEmpty(recipients)) {
            List<BenefitRecipient> recipientList = GsonUtil.jsonToList(recipients, BenefitRecipient.class);
            log.info("commit recipientList is " + recipientList);

            try {
                if (benefitService.updateRecipientList(recipientList)) {
                    return ResponseUtil.createBussniessJson("成功");
                } else {
                    return ResponseUtil.createBussniessErrorJson(400, "失败");
                }
            } catch (Exception e) {
                log.error("获取个人捐赠列表异常", e);
                return LoanUtils.buildExceptionResponse(log, "获取个人捐赠列表异常", e);
            }

        }

        return "";
    }

    /**
     * 申请处理（个人中心-领取详情-处理）
     */
    @RequestMapping(value = "myBenefitRecipientDispose", method = RequestMethod.POST)
    @ResponseBody
    public String myBenefitRecipientDispose(HttpServletRequest request, BenefitRecipient recipient) {
        log.info("个人中心-物品详情-处理");
        try {
            if (benefitService.myBenefitRecipientDispose(recipient)) {
                return ResponseUtil.createBussniessJson("成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error("个人中心-物品详情-处理异常", e);
            return LoanUtils.buildExceptionResponse(log, "个人中心-物品详情-处理异常", e);
        }
    }

    /**
     * 评论
     */
    @RequestMapping(value = "evaluate", method = RequestMethod.POST)
    @ResponseBody
    public String evaluate(HttpServletRequest request, BenefitEvaluate evaluate) {
        log.info("个人中心-物品详情-处理");
        try {
            if (benefitService.evaluate(evaluate)) {
                BenefitRecipient recipient = benefitDao.BenefitRecipientById(evaluate.getEvaRecId());

                try {
                    String saveIntegalTask = HttpClientKTPCloundUtils.saveIntegalTask(recipient.getRecDonId(), 205);
                    System.out.println("==" + saveIntegalTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ResponseUtil.createBussniessJson("评论成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "评论失败,请检查数据是否正确");
            }
        } catch (Exception e) {
            log.error("个人中心-物品详情-处理异常", e);
            return LoanUtils.buildExceptionResponse(log, "个人中心-物品详情-处理异常", e);
        }
    }

    /**
     * 个人中心-领取详情-弹层
     *
     * @param:
     * @return:myDonateDetail
     */
    @RequestMapping(value = "myRecipientList", method = RequestMethod.GET)
    @ResponseBody
    public String myRecipientList(@Param(value = "page") int page, @Param(value = "pageSize") int pageSize,
                                  @Param(value = "userId") int userId, @Param(value = "actId") int actId) {
        log.info("个人中心-领取详情-弹层");
        try {
            List<MyRecipientListDto> myRecipientList = benefitService.myRecipientList(page, pageSize, userId, actId);
            return ResponseUtil.createNormalJson(myRecipientList);
        } catch (Exception e) {
            log.error("个人中心-领取详情-弹层异常", e);
            return LoanUtils.buildExceptionResponse(log, "个人中心-领取详情-弹层异常", e);
        }
    }


    /**
     * 个人领取列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "getMyReceiveListByUserId", method = RequestMethod.GET)
    @ResponseBody
    public String getMyReceiveListByUserId(@Param(value = "page") int page, @Param(value = "pageSize") int pageSize, @Param(value = "userId") int userId) {
        log.info("个人领取列表");
        try {
            return ResponseUtil.createNormalJson(benefitService.getMyReceiveListByUserId(page, pageSize, userId));
        } catch (Exception e) {
            log.error("个人领取列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "个人领取列表异常", e);
        }
    }

    /**
     * 个人中心-领取详情
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "myReceiveDetail", method = RequestMethod.GET)
    @ResponseBody
    public String myReceiveDetail(@Param(value = "id") int id) {
        log.info("个人中心-领取详情-（申请中心-已经寄出-受助成功）");
        try {
            //DonateSeccssListDto myRecipientList = benefitService.myReceiveDetail(id);
            return ResponseUtil.createNormalJson(benefitService.myReceiveDetail(id));
        } catch (Exception e) {
            log.error("个人中心-领取详情-（申请中心-已经寄出-受助成功）异常", e);
            return LoanUtils.buildExceptionResponse(log, "个人中心-领取详情-（申请中心-已经寄出-受助成功）异常", e);
        }
    }

    /**
     * 进入发布捐赠
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "getDonateSelection", method = RequestMethod.GET)
    @ResponseBody
    public String getDonateSelection(HttpServletRequest request) {
        log.info("进入发布捐赠获取数据");
        try {
            return ResponseUtil.createNormalJson(benefitService.getDonateSelection());
        } catch (Exception e) {
            log.error("进入发布捐赠获取数据异常", e);
            return LoanUtils.buildExceptionResponse(log, "进入发布捐赠获取数据异常", e);
        }

    }


    /**
     * 发布捐赠
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "donateSubmit", method = RequestMethod.POST)
    @ResponseBody
    public String InsertDonate(HttpServletRequest request, BenefitDonate donate) {
        log.info("捐赠添加");
        try {
            //邮费文字转换
            if (donate.getDonPostage() != null && !"".equals(donate.getDonPostage()) && donate.getDonPostage().indexOf("对方支付邮费") != -1) {
                donate.setDonPostage("邮费自付,150,2");
                donate.setDonPostage(donate.getDonPostage().trim());
            } else if (donate.getDonPostage() != null && !"".equals(donate.getDonPostage()) && donate.getDonPostage().indexOf("我支付邮费") != -1) {
                donate.setDonPostage("免邮费,149,1");
                donate.setDonPostage(donate.getDonPostage().trim());
            }


            donate.setDonTime(new Date());
            donate.setDonInventory(donate.getDonSum());
            donate.setDonCommentSum(0);//未查看评论
            donate.setDonApplySum(0);//未查看申请人数

            //编辑提交不用审核，拒绝提交为未审核


            if (donate.getId() != null && !"".equals(donate.getId())) {
                //通过捐赠id获取状态
                donate.setDonStatus(benefitService.queryDonateStatusById(donate.getId()));
            } else {
                donate.setDonStatus(0);
                //拒绝原因初始为空
                donate.setDonRejectReason("");
            }

            if (benefitService.InsertDonate(donate)) {
                String saveIntegalTask = HttpClientKTPCloundUtils.saveIntegalTask(donate.getDonUserId(), 203);
                BaseEntity baseEntity = GsonUtil.fromJson(saveIntegalTask, BaseEntity.class);
                if (baseEntity.getStatus().getCode() == 10 && baseEntity.getBusinessStatus().getCode() == 100) {//成功
                    return ResponseUtil.createNormalJson(null, "成功", baseEntity.getBusinessStatus().getIntegralmsg());
                }
                return ResponseUtil.createBussniessJson("成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }

        } catch (Exception e) {
            log.error("提交捐赠信息异常", e);
            return LoanUtils.buildExceptionResponse(log, "提交捐赠信息异常", e);
        }
    }


    /**
     * 修改捐赠信息
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "donateModification", method = RequestMethod.POST)
    @ResponseBody
    public String updateDonate(HttpServletRequest request, BenefitDonate donate) {
        log.info("捐赠修改");
        try {
            donate.setDonTime(new Date());
            donate.setDonInventory(donate.getDonSum());
            if (benefitService.updateDonate(donate)) {
                return ResponseUtil.createBussniessJson("成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error("修改捐赠信息异常", e);
            return LoanUtils.buildExceptionResponse(log, "修改捐赠信息异常", e);
        }

    }


    /**
     * 申请捐助（获取捐赠表信息）
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "applyRecipient", method = RequestMethod.GET)
    @ResponseBody
    public String applyRecipient(HttpServletRequest request, @Param(value = "id") int id) {
        log.info("申请领取（获取捐赠表信息）");
        try {
            return ResponseUtil.createNormalJson(benefitService.getapplyDonate(id));
        } catch (Exception e) {
            log.error("申请领取(受赠)异常", e);
            return ResponseUtil.createBussniessErrorJson(400, "失败");
        }
    }

    /**
     * 提交申请捐助
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "applyRecipientSubmit", method = RequestMethod.POST)
    @ResponseBody
    public String InsertDonate(HttpServletRequest request, BenefitRecipient recipient) {
        log.info("提交申请领取");
        try {
            recipient.setRecTime(new Date());
            recipient.setRecStatus(0);
            int sta = benefitService.InsertRecipient(recipient);
            if (sta == 1) {
                return ResponseUtil.createBussniessJson("成功");
            } else if (sta == 2) {
                return ResponseUtil.createBussniessErrorJson(400, "提交失败，库存不足");
            } else if (sta == 3) {
                return ResponseUtil.createBussniessErrorJson(400, "提交失败，请稍后再试");
            } else if (sta == 0) {
                return ResponseUtil.createBussniessErrorJson(400, "捐赠物审核中，请稍后再试");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error("提交申请领取异常", e);
            return LoanUtils.buildExceptionResponse(log, "提交申请领取异常", e);
        }
    }

    /**
     * 进入编辑
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "joinCompile", method = RequestMethod.POST)
    @ResponseBody
    public String joinCompile(HttpServletRequest request, @Param(value = "donId") int donId) {
        log.info("进入编辑");

        try {
            return ResponseUtil.createNormalJson(benefitService.joinCompile(donId));
        } catch (Exception e) {
            log.error("申请领取(受赠)异常", e);
            return ResponseUtil.createBussniessErrorJson(400, "失败");
        }
    }

    /**
     * 退出编辑
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "exitCompile", method = RequestMethod.POST)
    @ResponseBody
    public String exitCompile(HttpServletRequest request, @Param(value = "donId") int donId) {
        log.info("进入编辑");
        try {
            if (benefitService.exitCompile(donId)) {
                return ResponseUtil.createBussniessJson("成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error("进入编辑异常", e);
            return LoanUtils.buildExceptionResponse(log, "进入编辑异常", e);
        }
    }

    /**
     * 审核发布捐赠
     *
     * @param: int donId;//捐赠id
     * int donStatus;//处理 -1:拒绝 1:审核通过
     * @return:
     */
    @RequestMapping(value = "auditDonate", method = RequestMethod.POST)
    @ResponseBody
    public String updateDonateById(@Param(value = "donId") int donId) {
        log.info("审核发布捐赠");
        try {
            if (benefitService.updateDonateById(donId)) {
                return ResponseUtil.createBussniessJson("成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败，审核对象状态必须为0");
            }
        } catch (Exception e) {
            log.error("进入编辑异常", e);
            return LoanUtils.buildExceptionResponse(log, "进入编辑异常", e);
        }
    }

    /**
     * 通过活动id和捐赠id获取该活动捐赠成功列表和捐赠id中所捐赠成功列表详情
     *
     * @param: int donId;//捐赠id
     * @param: int actId;//活动id
     * @return:动捐赠成功列表 ，捐赠id中所捐赠成功列表详情
     */
    @RequestMapping(value = "actAndDonRecipientList", method = RequestMethod.GET)
    @ResponseBody
    public String getActAndDonRecipientList(@Param(value = "donId") int donId, @Param(value = "actId") int actId
            , @Param(value = "page") int page, @Param(value = "pageSize") int pageSize) {
        log.info("通过活动id和捐赠id获取该活动捐赠成功列表和捐赠id中所捐赠成功列表详情");
        try {
            return ResponseUtil.createNormalJson(benefitService.getActAndDonRecipientList(actId, donId, page, pageSize));
        } catch (Exception e) {
            log.error("通过活动id和捐赠id获取该活动捐赠成功列表和捐赠id中所捐赠成功列表详情异常", e);
            return LoanUtils.buildExceptionResponse(log, "失败", e);
        }
    }

    /**
     * 获取用户总捐赠未查看评论数，总申请数
     */
    @RequestMapping(value = "queryCommentAndApplyByUserId", method = RequestMethod.GET)
    @ResponseBody
    public String queryCommentAndApplyById(@Param(value = "userId") int userId, @Param(value = "actId") int actId, @Param(value = "donId") int donId) {
        log.info("获取用户总捐赠未查看评论数，总申请数");
        try {
            return ResponseUtil.createNormalJson(benefitService.queryCommentAndApplyById(userId, actId, donId));
        } catch (Exception e) {
            log.error("获取用户总捐赠未查看评论数，总申请数异常", e);
            return LoanUtils.buildExceptionResponse(log, "失败", e);
        }
    }

    /**
     * 某个捐赠的成功捐赠列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "queryRecipientListByDonId", method = RequestMethod.GET)
    @ResponseBody
    public String queryRecipientListByDonId(@Param(value = "donId") int donId) {
        log.info("某个捐赠的成功捐赠列表");
        try {
            List<DonateSeccssListDto> dtos = benefitService.queryRecipientListByDonId(donId);
            return ResponseUtil.createNormalJson(dtos);
        } catch (Exception e) {
            log.error("某个捐赠的成功捐赠列表", e);
            return LoanUtils.buildExceptionResponse(log, "某个捐赠的成功捐赠列表", e);
        }
    }


}
