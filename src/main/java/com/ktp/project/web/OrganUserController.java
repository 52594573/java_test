package com.ktp.project.web;

import com.google.common.collect.ImmutableMap;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.service.OrganService;
import com.ktp.project.service.OrganUserService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.ResponseUtil;
import com.zm.entity.ProOrgan;
import com.zm.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2018年12月25日17:29:00
 */
@RestController
@RequestMapping(value = "api/organUser", produces = "application/json;charset=UTF-8;")
public class OrganUserController {

    private static final Logger log = LoggerFactory.getLogger(OrganUserController.class);

    @Autowired
    private OrganUserService organUserService;
    @Autowired
    private OrganService organService;


    /**
     * 设置班组长
     *
     * @return
     */
    @RequestMapping(value = "/setOrganLeader", method = RequestMethod.POST)
    public String create( int pro_id, int po_id, int user_id) {
        try {

            organUserService.setOrganLeader(pro_id,po_id,user_id);

            //判断是否是广州项目，是就发送
           // Project project = organService.getPSent(pro_id);
            if (EnumMap.subclassMap.containsKey(pro_id)){

                AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(pro_id);
                authRealNameApi.synBuildPo(po_id, user_id,"POSAVE");
                //return ResponseUtil.createTongBulJson(ImmutableMap.of("organId", pro_id), "成功");

            }
            return ResponseUtil.createNormalJson(ImmutableMap.of("organId", pro_id), "成功");
        } catch (Exception e) {
            log.error("设置班组长", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
    /**
     *设置班组长同步触发
     */
    @RequestMapping(value = "/setOrganLeaderSend", method = RequestMethod.POST)
    public String setOrganLeaderSend( int pro_id, int po_id, int user_id) {
        try {
            //判断是否是广州项目，是就发送
            if (EnumMap.subclassMap.containsKey(pro_id)){

                AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(pro_id);
                authRealNameApi.synBuildPo(po_id, user_id,"POSAVE");
            }else{
                return ResponseUtil.createBussniessErrorJson(500, "项目推送目标没设置");
            }
            return ResponseUtil.createNormalJson(ImmutableMap.of("organId", pro_id), "成功");
        } catch (Exception e) {
            log.error("设置班组长", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }
    /**
     * 设置班组长
     *
     * @return
     */
    @RequestMapping(value = "/joinOrOutWork", method = RequestMethod.POST)
    public String joinOrOutWork( int poId, int userId, String type) {
        try {
            ProOrgan proOrgan = organService.queryOne(poId);
            if (EnumMap.subclassMap.containsKey(proOrgan.getProId())){
                AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(proOrgan.getProId());
                authRealNameApi.synBuildPoUserJT(poId, userId,type, null);
            }
            return ResponseUtil.createNormalJson("成功");
        } catch (Exception e) {
            log.error("设置班组长", e);
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }



}
