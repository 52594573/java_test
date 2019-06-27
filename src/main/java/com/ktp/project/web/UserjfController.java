package com.ktp.project.web;

import com.ktp.project.service.UserJfService;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-12-28 下午 17:06
 */
@RestController
@RequestMapping(value = "api/userjf", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class UserjfController {
    private static final Logger log = LoggerFactory.getLogger(UserjfController.class);
    @Autowired
    private UserJfService userJfService;

    /**
      * 查询积分总数
      *
      * @return java.lang.String
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019-01-04 上午 9:52
      */
    @RequestMapping(value = "getJfByUserId", method = RequestMethod.GET)
    public String getAdvertiseList(Integer userId) {
        if (userId == null) {
            return ResponseUtil.createBussniessErrorJson(500, "用户id不能为空");
        }
        try {
            int sum = userJfService.getJfByUserId(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("sum", sum);
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
      * 查询积分详情
      *
      * @return java.lang.String
      * @params: [userId]
      * @Author: wuyeming
      * @Date: 2019-01-04 上午 9:52
      */
    @RequestMapping(value = "getJfDetailByUserId", method = RequestMethod.GET)
    public String getJfDetailByUserId(Integer userId) {
        if (userId == null) {
            return ResponseUtil.createBussniessErrorJson(500, "用户id不能为空");
        }
        try {
            List<Map<String, Object>> list = userJfService.getJfDetailByUserId(userId);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
