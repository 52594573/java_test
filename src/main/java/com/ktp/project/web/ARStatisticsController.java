package com.ktp.project.web;

import com.ktp.project.entity.ARStatistics;
import com.ktp.project.service.ARStatisticsService;
import com.ktp.project.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Author: tangbin
 * @Date: 2018/12/17 16:26
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/api/arstatistics", produces = "application/json;charset=UTF-8;")
public class ARStatisticsController {

    @Autowired
    private ARStatisticsService arStatisticsService;

    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    @ResponseBody
    public String getAll(Integer projectId,String startDate,String endDate) {
        try{
            Map<String, Object> result = arStatisticsService.getXiangqin1(projectId, startDate, endDate);
            return ResponseUtil.createNormalJson(result);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
    }
    }

    /***
     * 考勤记录统计详情接口
     * @param projectId
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/getDetails",method = RequestMethod.POST)
    @ResponseBody
    public String getDetails(Integer projectId,String startDate,String endDate){
        try {
            Map<String, Object> result = arStatisticsService.getXiangqin(projectId, startDate, endDate);
            return ResponseUtil.createNormalJson(result);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
    }
    }
}
