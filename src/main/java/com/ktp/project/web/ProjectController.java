package com.ktp.project.web;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.dto.ProjectDeptDto;
import com.ktp.project.dto.ProjectDetailDto;
import com.ktp.project.dto.ProjectDto;
import com.ktp.project.dto.file.FileListDto;
import com.ktp.project.exception.TransactionalException;
import com.ktp.project.service.FileService;
import com.ktp.project.service.ProjectService;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.ResponseUtil;
import com.zm.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wuyeming
 * @Date: 2018-10-05 14:10
 */
@Controller("proController")
@RequestMapping(value = "api/project", produces = "application/json;charset=UTF-8;")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService proService;

    @Autowired
    private FileService fileService;


    /**
     * 创建项目，同时也创建项目群
     *
     * @param projectName 项目名称
     * @param userId      负责人ID
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String create(@RequestParam("projectName") String projectName,
                         @RequestParam("userId") int userId,
                         @RequestParam(value = "hatchStatus", defaultValue = "-1") Integer isHatch) {
        try {
            return ResponseUtil.createNormalJson(ImmutableMap.of("projectId", proService.create(projectName, userId, isHatch)), "成功");
        } catch (Exception e) {
            if (e instanceof TransactionalException) {
                try {
                    HuanXinRequestUtils.removeGroup(e.getMessage());  //删除无效群聊
                    return ResponseUtil.createBussniessErrorJson(500, "创建项目失败");
                } catch (Exception ex) {
                    return HuanXinRequestUtils.buildExceptionResponse(e);
                }
            }
            log.error("创建项目异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    /**
     * 保存项目
     *
     * @return java.lang.String
     * @params: [project]
     * @Author: wuyeming
     * @Date: 2018-10-05 15:40
     */
    @RequestMapping(value = "saveProject", method = RequestMethod.POST)
    @ResponseBody
    public String saveProject(Project project) {
        log.info("保存项目");
        try {
            if (StringUtils.isEmpty(project.getId())) {//如果为新增，设置初始化时间
                project.setIsDel(0);
                project.setPState(project.getPState() == null ? 0 : project.getPState());
                project.setPIntime(new Timestamp(System.currentTimeMillis()));
                project.setPCreateType(1);
                project.setHatchStatus(project.getHatchStatus() == null ? -1 : project.getHatchStatus());
            }
            Integer cityId = proService.getCityId(project.getPCity());
            project.setPCity(cityId);
            Integer proId = proService.saveProject(project);
            if (proId != null) {
                return ResponseUtil.createNormalJson(proService.getProjectInfo(proId));
            } else {
                return ResponseUtil.createBussniessErrorJson(400, "失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 获取项目信息
     *
     * @return java.lang.String
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-05 15:49
     */
    @RequestMapping(value = "getProjectInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getProjectInfo(Integer id) {
        log.info("获取项目信息");
        try {
            ProjectDto projectDto = proService.getProjectInfo(id);
            return ResponseUtil.createNormalJson(projectDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 项目统计-获取项目详情
     *
     * @return java.lang.String
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-06 16:43
     */
    @RequestMapping(value = "getProjectDetail", method = RequestMethod.POST)
    @ResponseBody
    public String getProjectDetail(Integer id) {
        log.info("项目统计-获取项目详情");
        try {
            ProjectDetailDto projectDetailDto = proService.getProjectDetail(id);
            return ResponseUtil.createNormalJson(projectDetailDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 项目统计-获取工种信息
     *
     * @return java.lang.String
     * @params: [id]
     * @Author: wuyeming
     * @Date: 2018-10-08 10:21
     */
    @RequestMapping(value = "getDeptList", method = RequestMethod.POST)
    @ResponseBody
    public String getDeptList(Integer id) {
        log.info("项目统计-获取工种信息");
        try {
            List<ProjectDeptDto> list = proService.getDeptList(id);
            return ResponseUtil.createNormalJson(list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


    /**
     * 查询项目文件
     *
     * @return java.lang.String
     * @params: [fs_id, u_id, pro_id, s_key]
     * @Author: wuyeming
     * @Date: 2018-12-18 上午 9:59
     */
    @RequestMapping(value = "getFileList", method = RequestMethod.GET)
    @ResponseBody
    public String getFileList(Integer fs_id, Integer u_id, Integer pro_id, String s_key) {
        try {
            List<FileListDto> fileList = fileService.getFileList(fs_id, u_id, pro_id, s_key);
            Map<String, Object> map = new HashMap<>();
            if (fileList.size() > 0) {
                map.put("allcount", fileList.size());
                map.put("file_list", fileList);
            }
            return ResponseUtil.createNormalJson(map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
}
