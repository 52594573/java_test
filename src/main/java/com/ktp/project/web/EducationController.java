package com.ktp.project.web;


import com.ktp.project.dto.education.*;
import com.ktp.project.entity.BaseEntity;
import com.ktp.project.entity.EductionCommentEntity;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.HttpClientKTPCloundUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import com.ktp.project.service.EducationService;
import com.ktp.project.util.LoanUtils;
import com.ktp.project.util.ResponseUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description: 教育模块
 * @Author: liaosh
 * @Date: 2018-11-2
 */
@Controller
@RequestMapping(value = "api/education", produces = "application/json;charset=UTF-8;")
public class EducationController {
    private static final Logger log = LoggerFactory.getLogger(EducationController.class);

    @Autowired
    private EducationService educationService;

    /**
     * 获取教育视频列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "educationList", method = RequestMethod.GET)
    @ResponseBody
    public String getEducationList(@Param(value = "page") int page, @Param(value = "pageSize") int pageSize,
                                   @Param(value = "userId") int userId, @Param(value = "typeId") int typeId
            , @RequestParam(value = "searchKey", required = false) String searchKey) {
        log.info("获取视频列表");
        try {
            if (searchKey == null) {
                searchKey = "";
            }
            List<EducationListDto> dtos = educationService.getEducationList(page, pageSize, userId, typeId, searchKey);
            return ResponseUtil.createNormalJson(dtos);
        } catch (Exception e) {
            log.error("获取视频列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取视频列表异常", e);
        }
    }

    /**
     * 获取视频详细信息
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "videoDetail", method = RequestMethod.GET)
    @ResponseBody
    public String getVideoDetail(@Param(value = "id") int id, @Param(value = "userId") int userId) {
        log.info("获取视频详细信息");
        try {
            EducationDetailDto dto = educationService.getVideoDetail(id, userId);
            return ResponseUtil.createNormalJson(dto);
        } catch (Exception e) {
            log.error("获取视频详细信息异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取视频详细信息异常", e);
        }
    }

    /**
     * 获取视频类型
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "videoType", method = RequestMethod.GET)
    @ResponseBody
    public String getvideoType() {
        log.info("获取视频类型");
        try {
            List<EducationKeyContentDto> content = educationService.getvideoType();
            return ResponseUtil.createNormalJson(content);
        } catch (Exception e) {
            log.error("获取视频类型异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取视频类型异常", e);
        }
    }

    /**
     * 点赞接口
     */
    @RequestMapping(value = "videoLike", method = RequestMethod.POST)
    @ResponseBody
    public String saveVideoLike(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId,
                                @Param(value = "status") Integer status) {
        log.info("点赞接口");
        try {
            EductionLikeDto entity = new EductionLikeDto();
            entity.setId(id);
            entity.setvLike(educationService.saveVideoLike(id, userId, status));
            return ResponseUtil.createNormalJson(entity);

        } catch (Exception e) {
            log.error("点赞接口异常", e);
            return LoanUtils.buildExceptionResponse(log, "点赞接口异常", e);
        }
    }

    /**
     * 评论接口
     */
    @RequestMapping(value = "videoComment", method = RequestMethod.POST)
    @ResponseBody
    public String saveVideoComment(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId,
                                   @Param(value = "cContent") String cContent) {
        log.info("评论接口");
        EductionCommentEntity entity = new EductionCommentEntity();
        entity.setId(id);
        try {
            if (educationService.saveVideoComment(id, userId, cContent)) {
                return ResponseUtil.createNormalJson(entity);
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error("评论接口异常", e);
            return LoanUtils.buildExceptionResponse(log, "评论接口异常", e);
        }
    }


    /**
     * 观看视频接口
     */
    @RequestMapping(value = "videoLook", method = RequestMethod.POST)
    @ResponseBody
    public String saveVideoLook(@Param(value = "id") Integer id, @Param(value = "userId") Integer userId,
                                @Param(value = "tLong") Integer tLong) {
        log.info("点赞接口");
        try {
            if (educationService.saveVideoLook(id, userId, tLong)) {
                String saveIntegalTask = HttpClientKTPCloundUtils.saveIntegalTask(userId, 213);
                BaseEntity baseEntity = GsonUtil.fromJson(saveIntegalTask, BaseEntity.class);
                if (baseEntity.getStatus().getCode() == 10 && baseEntity.getBusinessStatus().getCode() == 100) {//成功
                    return ResponseUtil.createNormalJson(null, "成功", baseEntity.getBusinessStatus().getIntegralmsg());
                }
                return ResponseUtil.createNormalJson(null, "成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error("观看视频接口异常", e);
            return LoanUtils.buildExceptionResponse(log, "观看视频接口异常", e);
        }
    }

    /**
     * 获取视频列表
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "getVideoComment", method = RequestMethod.GET)
    @ResponseBody
    public String getVideoComment(@Param(value = "id") Integer id, @Param(value = "page") int page, @Param(value = "pageSize") int pageSize) {
        log.info("获取视频列表");
        try {
            List<EducationCommentDto> entities = educationService.getVideoComment(id, page, pageSize);
            return ResponseUtil.createNormalJson(entities);
        } catch (Exception e) {
            log.error("获取视频列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取视频列表异常", e);
        }
    }

    /**
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/share/details", method = RequestMethod.GET)
    public String shareDetails(Model model, Integer id, Integer userId) {
        try {
            if (id == null) {
                throw new RuntimeException("缺少参数");
            }
            EducationH5Dto dto = educationService.getEducationShareH5ById(id);
            model.addAttribute("dto", dto);
            return "share/eduction_video";
        } catch (Exception e) {
            log.error("分享朋友圈异常", e.getMessage());
            return "error/500";
        }
    }
}
