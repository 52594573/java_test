package com.ktp.project.web;


import com.ktp.project.dto.LaoXiangDto.LaoXiangDto;
import com.ktp.project.service.LaoXiangService;
import com.ktp.project.util.LoanUtils;
import com.ktp.project.util.ResponseUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * @Description: 老乡
 * @Author: liaosh
 * @Date: 2018-11-12
 */
@Controller
@RequestMapping(value = "api/laoxiang", produces = "application/json;charset=UTF-8;")
public class LaoXiangController {
    private static final Logger log = LoggerFactory.getLogger(LaoXiangController.class);

    @Autowired
    private LaoXiangService laoXiangService;

    /**
     * 获取未添加老乡好友接口
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "getLaoXiangChatFriendList", method = RequestMethod.GET)
    @ResponseBody
    public String getEducationList(@Param(value = "page") int page, @Param(value = "pageSize") int pageSize,
                                   @Param(value = "userId") int userId,
                                   @Param(value = "newBeginTime") Date newBeginTime, @Param(value = "newEndTime") Date newEndTime) {
        log.info("获取未添加老乡好友");
        try {
            List<LaoXiangDto> dtos = laoXiangService.getLaoXiangChatFriendList(page, pageSize, userId,newBeginTime,newEndTime);
            return ResponseUtil.createNormalJson(dtos);
        } catch (Exception e) {
            log.error("获取未添加老乡好友异常", e);
            return LoanUtils.buildExceptionResponse(log, "获取未添加老乡好友异常", e);
        }
    }

}
