package com.ktp.project.web;

import com.ktp.project.entity.WorkLogGather;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.WorkLogGatherService;
import com.ktp.project.service.WorkLogService;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@SuppressWarnings("all")
@RequestMapping(value = "/api/workLogGather", produces = "application/json;charset=UTF-8;")
public class WorkLogGatherController {
    @Autowired
    private WorkLogGatherService workLogGatherService;
    @Autowired
    private WorkLogService workLogService;

    /**
     * 工作记录统计接口
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "listByCondition", method = RequestMethod.POST)
    public String listByCondition(WorkLogGather vo) {
        try {
            if (vo.getProjectId() == null || vo.getProjectId() <= 0) {
                throw new BusinessException("项目ID有误!");
            }
            if (vo.getUserId() != null && vo.getUserId() > 0) {
                workLogService.validateUserId(vo.getUserId());
            }
            return ResponseUtil.createNormalJson(workLogGatherService.listByCondition(vo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 工作记录统计月报表统计
     *
     * @return
     */
    @RequestMapping(value = "/createWorkLogGather", method = RequestMethod.GET)
    public String createWorkLogGather() {
        try {
            List<WorkLogGather> workLogGathers = workLogGatherService.listProjectIsAuth();
            Integer year = NumberUtil.getLastYearNumCurrentTime();
            Integer month = NumberUtil.getLastMonthNumCurrentTime();
            if (!CollectionUtils.isEmpty(workLogGathers)) {
                for (WorkLogGather workLogGather : workLogGathers) {
                    workLogGather.setYear(year);
                    workLogGather.setMonth(month + 1);
                    WorkLogGather bo = workLogGatherService.SetParameters(workLogGather);
                    bo.setCreateTime(new Date());
                    workLogGatherService.save(bo);
                }
            }
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }



}
