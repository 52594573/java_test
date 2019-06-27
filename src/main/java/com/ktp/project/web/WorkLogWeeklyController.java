package com.ktp.project.web;


import com.ktp.project.dto.WorkLogGatherDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.WorkLogWeeklyService;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * author：hjl
 * 工作日志周报
 */
@RestController
@RequestMapping(value = "/api/workLogWeekly", produces = "application/json;charset=UTF-8;")
public class WorkLogWeeklyController {

    @Autowired
    private WorkLogWeeklyService workLogWeeklyService;

    /**
     * 周报统计
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/workLogStatistics", method = RequestMethod.POST)
    @ResponseBody
    public String workLogStatistics(WorkLogGatherDto vo, String startDate, String endDate) {
        try {
            //查询是否有项目id
            if (vo.getProjectId() == null || vo.getProjectId() <= 0) {
                throw new BusinessException("项目ID有误");
            }
            if (endDate == null || StringUtils.isEmpty(endDate)) {
                throw new BusinessException("选择结束时间不能为空");
            }
            if (startDate == null || StringUtil.isEmpty(startDate)) {
                throw new BusinessException("选择开始时间不能为空");
            }
            startDate = startDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            return ResponseUtil.createNormalJson(workLogWeeklyService.querystat(vo.getProjectId(), startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 周报全部班组情况
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/workLogStatisDetail", method = RequestMethod.POST)
    @ResponseBody
    public String workLogStatisDetail(WorkLogGatherDto vo, Integer wl_type, String startDate, String endDate) {
        try {
            //查询是否有项目id
            if (vo.getProjectId() == null || vo.getProjectId() <= 0) {
                throw new BusinessException("项目ID有误");
            }
            if (wl_type == null || wl_type <= 0) {
                throw new BusinessException("类型不能为空");
            }
            if (startDate == null || StringUtil.isEmpty(startDate)) {
                throw new BusinessException("选择开始时间不能为空");
            }
            if (endDate == null || StringUtils.isEmpty(endDate)) {
                throw new BusinessException("选择结束时间不能为空");
            }

            startDate = startDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            return ResponseUtil.createNormalJson(workLogWeeklyService.querystatdetail(vo.getProjectId(), wl_type, startDate, endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 周报 单个班组详情
     *
     * @param vo
     * @param teamid    班组id
     * @param wl_type
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/workLogListbyTeam", method = RequestMethod.POST)
    @ResponseBody
    public String workLogListbyTeam(WorkLogGatherDto vo, Integer teamid, Integer wl_type, String startDate, String endDate, Integer pageNo, Integer pageSize) {
        try {
            //查询是否有项目id
            if (vo.getProjectId() == null || vo.getProjectId() <= 0) {
                throw new BusinessException("项目ID有误");
            }
            if (wl_type == null || wl_type <= 0) {
                throw new BusinessException("类型不能为空");
            }
            if (teamid == null || teamid <= 0) {
                throw new BusinessException("班组ID不能为空");
            }
            if (startDate == null || StringUtil.isEmpty(startDate)) {
                throw new BusinessException("选择开始时间不能为空");
            }
            if (endDate == null || StringUtils.isEmpty(endDate)) {
                throw new BusinessException("选择结束时间不能为空");
            }

            if (pageNo == null || pageNo <= 0) {
                pageNo = 1;
            }
            if (pageSize == null || pageSize <= 0) {
                pageSize = 10;
            }

            startDate = startDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            return ResponseUtil.createNormalJson(workLogWeeklyService.queryWorkLogListbyTeam(vo.getProjectId(), teamid, wl_type, startDate, endDate, pageNo, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


}
