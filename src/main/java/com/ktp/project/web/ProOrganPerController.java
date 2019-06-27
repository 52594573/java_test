package com.ktp.project.web;

import com.ktp.project.constant.EnumMap;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.ProOrganPerService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.impl.GzProjectTempService;
import com.ktp.project.util.Page;
import com.ktp.project.util.ResponseUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/proOrganPer", produces = "application/json;charset=UTF-8;")
public class ProOrganPerController {

    @Autowired
    private ProOrganPerService proOrganPerService;
    @Autowired
    private GzProjectTempService gzProjectTempService;

    /**
     * 通过身份证号码或者扫描二维码
     * 新增班组工人
     * @return
     */
    @RequestMapping(value = "createByIdCardOrErCode", method = RequestMethod.POST)
    public String createByIdCardOrErCode(@RequestParam(value = "userId", defaultValue = "-1") Integer userId,
                                         @RequestParam("pro_id") Integer pro_id,
                                         @RequestParam("po_id") Integer po_id,
                                         @RequestParam(value = "in_u_id", required = false) String in_u_id,
                                         @RequestParam(value = "in_u_sfz", required = false) String in_u_sfz,
                                         @RequestParam(value = "in_p_type", required = false) String in_p_type){
        try {
            if (StringUtils.isBlank(in_u_sfz) && StringUtils.isBlank(in_u_id)){
                throw new BusinessException("in_u_id和in_u_sfz不可以同时为空");
            }
            if (StringUtils.isBlank(in_u_id)){
                in_u_id = "-1";
            }
            if (StringUtils.isBlank(in_p_type)){
                in_p_type = "-1";
            }
            proOrganPerService.createByIdCardOrErCode(userId, pro_id, po_id, Integer.parseInt(in_u_id), in_u_sfz, Integer.parseInt(in_p_type));
            return ResponseUtil.createNormalAspJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 删除班组工人
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String delete(@RequestParam("u_id") Integer u_id,@RequestParam("pro_id") Integer pro_id,
                         @RequestParam("del_u_id") Integer del_u_id, @RequestParam("del_po_id") Integer del_po_id){
        try {
            proOrganPerService.delete(u_id, pro_id, del_u_id, del_po_id);
            return ResponseUtil.createNormalAspJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 查询所有班组员工信息
     * @return
     */
    @RequestMapping(value = "pageProOrganPer", method = RequestMethod.GET)
    public String pageProOrganPerByProOrganId(@RequestParam(value = "proOrganId", required = false) Integer proOrganId, Page page){
        try {
            return ResponseUtil.createNormalAspJson(gzProjectTempService.pageProOrganPerByProOrganId(proOrganId, page));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 查询所有考勤
     * @return
     */
    @RequestMapping(value = "workerKaoQin", method = RequestMethod.GET)
    public String workerKaoQin( Page page){
        try {
            return ResponseUtil.createNormalAspJson(gzProjectTempService.workerKaoQin(page));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 查询所有考勤
     * @return
     */
    @RequestMapping(value = "listProOrgan", method = RequestMethod.GET)
    public String listProOrgan( Page page){
        try {
            return ResponseUtil.createNormalAspJson(gzProjectTempService.listProOrgan(page));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 同步考勤
     * @return
     */
    @RequestMapping(value = "/synKaoQin", method = RequestMethod.POST)
    public String synKaoQin( Integer projectId, Integer kaoQinId ){
        try {
            if (EnumMap.subclassMap.containsKey(projectId)){
                AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(projectId);
                authRealNameApi.synWorkerAttendance(projectId, kaoQinId);
            }
            return ResponseUtil.createNormalAspJson(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }





}
