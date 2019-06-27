package com.ktp.project.web;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.dto.ProOrganDto;
import com.ktp.project.service.OrganService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.ObjectUtil;
import com.ktp.project.util.ResponseUtil;
import com.ktp.project.util.redis.RedisClientTemplate;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPer;
import com.zm.entity.Project;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LinHon 2018/11/20
 */
@RestController
@RequestMapping(value = "api/organ", produces = "application/json;charset=UTF-8;")
public class OrganController {

    private static final Logger log = LoggerFactory.getLogger(OrganController.class);

    @Autowired
    private OrganService organService;


    /**
     * 创建组织机构，不创建群聊
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(ProOrgan proOrgan) {
        try {
            organService.create(proOrgan);
            return ResponseUtil.createNormalJson(ImmutableMap.of("organId", proOrgan.getId()), "成功");
        } catch (Exception e) {
            log.error("创建组织机构", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 删除机构
     *
     * @param organId
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(int organId) {
        try {
            organService.remove(organId);
            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("删除群组异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }

    /**
     *
     * @Description: 修改组织架构
     * @Author: liaosh
     * @Date: 2018/12/25 0025
     */
    @RequestMapping(value = "/updateOrgan", method = RequestMethod.POST)
    public String updateOrgan(ProOrganDto dto){
        try {
            ProOrganPer proOrgan = organService.updateOrgan(dto);

            if(proOrgan==null){
                return ResponseUtil.createBussniessJson("成功");
            }

            //判断是否是广州项目，是就发送
            Project project = organService.getPSent(dto.getPro_id());
            if (EnumMap.subclassMap.containsKey(project.getId())){
                AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(project.getId());
                authRealNameApi.synBuildPo(proOrgan.getPoId(), proOrgan.getUserId(),"POUPDATE");
            }

            return ResponseUtil.createBussniessJson("成功");
        } catch (Exception e) {
            log.error("修改组织架构异常", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


    @RequestMapping(value = "/createBySystem", method = RequestMethod.GET)
    public String createBySystem(@RequestParam("projectId") Integer projectId, @RequestParam(value = "organId", required = false, defaultValue = "-1") Integer organId){
        try {
            return ResponseUtil.createNormalJson(organService.createBySystem(projectId, organId),"成功");
        } catch (Exception e) {
            log.error("创建未分配班组失败", e);
            return HuanXinRequestUtils.buildExceptionResponse(e);
        }
    }


}
