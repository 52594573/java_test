package com.ktp.project.web;

import com.ktp.project.dto.WordListDto;
import com.ktp.project.entity.*;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AnnouncementService;
import com.ktp.project.service.MallGoodService;
import com.ktp.project.util.LoanUtils;
import com.ktp.project.util.Page;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公告接口
 * lsh
 * 2018年11月20日16:35:04
 */
@Controller
@RequestMapping(value = "api/announcement", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class AnnouncementController {
    private static final Logger log = LoggerFactory.getLogger(AnnouncementController.class);

    @Autowired
    private AnnouncementService announcementService;

    @RequestMapping(value = "getAnnouncementList", method = RequestMethod.GET)
    @ResponseBody
    public String getAnnouncementList(@Param(value = "page") int page,
                                      @Param(value = "pageSize") int pageSize, @Param(value = "proId") int proId,
                                      @Param(value = "userId") int userId) {
        log.info("获取公告列表");
        try {
            List<WordListDto> wordLists = announcementService.getAnnouncementList(page, pageSize, proId,userId);

            return ResponseUtil.createNormalJson(wordLists);
        } catch (Exception e) {
            log.error("获取公告列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取公告列表异常", e);
        }
    }


    @RequestMapping(value = "getAnnouncementListS", method = RequestMethod.GET)
    @ResponseBody
    public String getAnnouncementListS(@Param(value = "userId") Integer userId) {
        log.info("获取首页公告");
        try {
            return ResponseUtil.createNormalJson(announcementService.getAnnouncementListS());
        } catch (Exception e) {
            log.error("获取首页公告异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取公告列表异常", e);
        }
    }

    @RequestMapping(value = "getAnnouncementListShou", method = RequestMethod.GET)
    @ResponseBody
    public String getAnnouncementListShou(Page page,@Param(value = "userId") Integer userId) {
        log.info("获取首页公告列表");
        try {
            return ResponseUtil.createNormalJson(announcementService.getAnnouncementListShou(page));
        } catch (Exception e) {
            log.error("获取首页公告列表异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取公告列表异常", e);
        }
    }

    /**
     * 发布公告
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "insertAnnouncement", method = RequestMethod.POST)
    @ResponseBody
    public String insertAnnouncement(HttpServletRequest request, WordList wordList) {
        log.info("发布公告");
        try {
            if (announcementService.insertWordList(wordList)==1) {
                return ResponseUtil.createBussniessJson("成功");
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }

        } catch (Exception e) {
            log.error("发布公告异常", e);
            return LoanUtils.buildExceptionResponse(log, "发布公告异常", e);
        }
    }

    /**
     * 公告详情
     * @param id
     * @return
     */
    @RequestMapping(value = "getAnnouncementDetails", method = RequestMethod.GET)
    @ResponseBody
    public String getAnnouncementDetails( @Param(value = "id") int id) {
        log.info("获取公告详情");
        try {
            WordListDto dto = announcementService.getAnnouncementDetails(id);
            return ResponseUtil.createNormalJson(dto);
        } catch (Exception e) {
            log.error("获取公告详情异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取公告详情异常", e);
        }
    }

}
