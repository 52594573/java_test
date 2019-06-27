package com.ktp.project.web;


import com.ktp.project.dto.WorkLogGatherDto;
import com.ktp.project.entity.Page;
import com.ktp.project.entity.WorkLogDaoBean;
import com.ktp.project.entity.WorkLogParamer;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.WorkLogService;
import com.ktp.project.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/workLog", produces = "application/json;charset=UTF-8;")
public class WorkLogController {

    @Autowired
    private WorkLogService workLogService;

    /**
     * 针对质量员，检查员：安全,行为,质量行为详情列表接口
     *
     * @param vo
     * @param page
     * @return
     */
    @RequestMapping(value = "/listWorkLogByMore", method = RequestMethod.POST)
    @ResponseBody
    public String listWorkLogByMore(WorkLogGatherDto vo, Page<WorkLogGatherDto> page) {
        try {
            if (vo.getProjectId() == null || vo.getProjectId() <= 0) {
                throw new BusinessException("项目ID有误");
            }
            if (vo.getUserId() != null && vo.getUserId() > 0) {
                workLogService.validateUserId(vo.getUserId());
            }
            page.setT(vo);
            return ResponseUtil.createNormalJson(workLogService.listWorkLogByMore(page));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    //保存·工作纪录  发布工作日志
    @RequestMapping(value = "/work_log_add")
    @ResponseBody
    public String saveworklog(WorkLogDaoBean wl, String gr_uid, String wl_pic) {
        try {
            return ResponseUtil.createNormalJson(workLogService.saveworklog(wl, gr_uid, wl_pic));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    //查询工作日志详情
    @RequestMapping(value = "/worklogDetail", method = RequestMethod.POST)
    @ResponseBody
    public String worklogDetail(WorkLogParamer wl) {
        try {
            return ResponseUtil.createNormalJson(workLogService.worklogDetail(wl.getId() + ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    //更新全部日志
    @RequestMapping(value = "/updateAllworkLog", method = RequestMethod.POST)
    @ResponseBody
    public String updateAllworkLog() {
        try {
            workLogService.updateAllworkLog();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    //更新今天全部日志
    @RequestMapping(value = "/updateAllworkLogBytime", method = RequestMethod.POST)
    @ResponseBody
    public String updateAllworkLogBytime(String startDate, String endDate) {
        try {

            startDate = startDate + " 00:00:00";
            endDate = endDate + " 00:00:00";
            workLogService.updateAllworkLogBytime(startDate, endDate);
            return ResponseUtil.createNormalJson(200, "start update worklog...");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    //更新日志
    @RequestMapping(value = "/mustupdateAllworkLogbytime", method = RequestMethod.POST)
    @ResponseBody
    public String mustupdateAllworkLogbytime(String startDate, String endDate) {
        try {
            startDate = startDate + " 00:00:00";
            endDate = endDate + " 00:00:00";
            workLogService.mustupdateAllworkLogbytime(startDate, endDate);
            return ResponseUtil.createNormalJson(200, "start update worklog...");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    //更新日志
    @RequestMapping(value = "/mustupdateAllworkLogbyProject", method = RequestMethod.POST)
    @ResponseBody
    public String mustupdateAllworkLogbyProject(Integer proid) {
        try {
            workLogService.mustupdateAllworkLogbyProject(proid);
            return ResponseUtil.createNormalJson(200, "start update worklog...");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


}



