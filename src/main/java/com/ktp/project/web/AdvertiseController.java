package com.ktp.project.web;

import com.ktp.project.dto.AdvertiseDto;
import com.ktp.project.service.AdvertiseService;
import com.ktp.project.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author: wuyeming
 * @Date: 2018-12-28 下午 16:22
 */
@RestController
@RequestMapping(value = "api/advertise", produces = {"text/html;charset=UTF-8;", "application/json;"})
public class AdvertiseController {

    private static final Logger log = LoggerFactory.getLogger(AdvertiseController.class);
    @Autowired
    private AdvertiseService advertiseService;

    /**
      * 查询广告列表
      *
      * @return java.lang.String
      * @params: []
      * @Author: wuyeming
      * @Date: 2019-01-15 上午 11:33
      */
    @RequestMapping(value = "getAdvertiseList", method = RequestMethod.GET)
    public String getAdvertiseList(Integer index, Integer size) {
        try {
            List<AdvertiseDto> list = advertiseService.getAdvertiseListMy(index, size);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 查询首页广告列表
     *
     * @return java.lang.String
     * @params: []
     * @Author: tb
     * @Date: 2019-01-15 上午 11:33
     */
    @RequestMapping(value = "getAdvertiseListMy", method = RequestMethod.GET)
    public String getAdvertiseListMy(Integer index, Integer size) {
        try {
            List<AdvertiseDto> list = advertiseService.getAdvertiseList(index, size);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 孵化中心简介项目广告
     * @return
     */
    @RequestMapping(value = "listHatchAdvertise", method = RequestMethod.GET)
    public String listHatchAdvertise(){
        try {
            List<AdvertiseDto> list = advertiseService.listHatchAdvertise();
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
