package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.constant.ImgRatioEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.dto.AuthRealName.*;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.FoShanAuthRealNameAbstractService;
import com.ktp.project.service.realName.SdAuthRealNameApi;
import com.ktp.project.util.Base64Util;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.RealNameUtil;
import com.zm.entity.ProOrgan;
import com.zm.entity.UserInfo;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 顺德项目实现类
 */
@Service("sdAuthRealNameService")
@Transactional
public class SdAuthRealNameService extends FoShanAuthRealNameAbstractService implements SdAuthRealNameApi {

    public static void main(String[] args) throws Exception {

        Map<String, Object> paranms = new HashMap<>();
        paranms.put("Token", RealNameConfig.SD_API_TOKEN);
        paranms.put("ProjectNumber", "CD6CB3901809E46C20CE7E8D850F66C2");
        paranms.put("Name", " 建信开太平人脸识别闸机系统二代");
//        paranms.put("IP", "192.168.0.35");
//        paranms.put("Port", "0");
        paranms.put("SN", "0");
        paranms.put("MAC", "0");
        paranms.put("IsInMachine", "1");
        String url= getUrlString(RealNameConfig.SD_ATTENDANCEMACHINE_SAVEORUPDATE,paranms);
        String data = JSONObject.fromObject(paranms).toString();
        String result = HttpClientUtils.post(url, data, CONTENT_TYPE_TEXT, new HashMap<>());
        System.out.println(result);


    }

    @Override
    public void synBuildPo(Integer poId,Integer userId, String type) {
        log.info("同步班组信息开始,班组ID {},用户ID {},类型{},开始时间:{}", poId, userId, type, NumberUtil.formatDateTime(new Date()));
        String url = null;
        String reqBody = null;
        String reqUrl = null;String remark = null;
        AuthRealNameLog logBean = logService.createBaseBean(poId, userId);
        String contentType = null;
        try {
            ProOrgan project = proOrganDAO.findById(poId);
            SdProjectInfoDto projectInfoDto = realNameService.querySdProjectInfoByProjectId(project.getProId());
            switch (type) {
                case "POSAVE":
                    //新增班组
                    contentType = CONTENT_TYPE_JSON;
                    remark = RealNameConfig.SAVEPO;
                    SdProOrganDto dto = queryProOrganInfo(poId);
                    dto.setToken(RealNameConfig.SD_API_TOKEN);
                    dto.setContractID("27028360");//todo
                    dto.setEmerPeople(RealNameConfig.SD_EMERPEOPLE);
                    dto.setEmerTel(RealNameConfig.SD_EMERTEL);
                    dto.setContractSum(RealNameConfig.SD_CONTRACTSUM);
                    dto.setProjectPart(RealNameConfig.SD_PROJECTPART);//
                    dto.setCompanyNumber(projectInfoDto.getCreditCode());// RealNameConfig.SD_BUSINESS_LICENCE  "ktp_xinyongdaima5"
                    dto.setProjectNumber(projectInfoDto.getProjectCode());//todo "CD6CB3901809E46C20CE7E8D850F66C2"
                    RealNameUtil.checkRequestParams(dto);
                    reqBody = jsckSon.writeValueAsString(dto);
                    reqUrl = RealNameConfig.SD_PEOPLE_ADDPROJECTTEAM;
//                    url= getUrlString(reqUrl, JSONObject.fromObject(reqBody));
                    url = RealNameConfig.SD_BASE_PATH + reqUrl;
                    break;
                case "POUPDATE":
                    //修改班组
                    break;
                case "POUSERSAV":
                    //增加班组工人
                    contentType = CONTENT_TYPE_TEXT;
                    remark = RealNameConfig.SUPO;
                    SdProOrganPerDto.WorkerInfo workerInfo = queryWorkerInfo(poId, userId);
                    workerInfo.setEmerPeople(RealNameConfig.SD_EMERPEOPLE);
                    workerInfo.setEmerPhone(RealNameConfig.SD_EMERTEL);//
                    workerInfo.setCompanyName(projectInfoDto.getCompanyNme());// RealNameConfig.KTP_COMPANY_NAME "施工单位测试项目（开太平）" "佛山市保利顺源房地产有限公司"
                    workerInfo.setCompanyNumber(projectInfoDto.getCreditCode());//  RealNameConfig.KTP_COMPANY_CODE  ktp_xinyongdaima5 "91440606MA4UJAUR49"

                    SdProOrganPerDto poDto = new SdProOrganPerDto();
                    poDto.setToken(RealNameConfig.SD_API_TOKEN);
                    poDto.setPostJson(workerInfo);
                    RealNameUtil.checkRequestParams(poDto);
                    reqBody = jsckSon.writeValueAsString(poDto);
                    reqUrl = RealNameConfig.SD_PEOPLE_ADDATTENDANCEPEOPLE;
                    url= getUrlString(RealNameConfig.SD_PEOPLE_ADDATTENDANCEPEOPLE, JSONObject.fromObject(reqBody));
                    break;
                case "POUSERUPDATE":
                    //修改班组工人
                    break;
                default:
                    return;
            }//
            reqUrl = RealNameConfig.SD_BASE_PATH + reqUrl;
            String result = HttpClientUtils.post(url, reqBody, contentType, new HashMap<>());
            RealNameUtil.SDproResIfSussess(result);
        } catch (Exception e) {
            e.printStackTrace();
            logBean.setIsSuccess(0);
            logBean.setResMsg(e.getMessage());
        }finally {
            logBean.setRemark(remark);
            logBean.setReqBody(reqBody);
            logBean.setReqUrl(reqUrl);
            logService.saveOrUpdate(logBean);
        }
        log.info("远程调用顺德实名系统: URL: {},请求参数 {},返回结果 {},结束时间:{}", logBean.getReqUrl(), logBean.getReqBody(), logBean.getResMsg(), NumberUtil.formatDateTime(new Date()));

    }

    /**
     * 上传外来人员登记记录
     * @param projectId 项目id
     * @param idcardNum 身份证
     * @param name 姓名
     */
    public void uploadRegistrationRecord(String projectId,String  idcardNum,String name) {
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("Token", RealNameConfig.SD_API_TOKEN);
        paranms.put("ProjectNum", "CD6CB3901809E46C20CE7E8D850F66C2");
        paranms.put("IdcardNum", idcardNum);
        paranms.put("Name", name);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        //获取当前时间的年月日
        String timestamp = df.format(time);
        paranms.put("CreateTime", timestamp);
        String url= getUrlString(RealNameConfig.SD_PEOPLE_UPLOADREGISTRATIONRECORD,paranms);
        String data = JSONObject.fromObject(paranms).toString();
        //System.out.println(data);
        String result = HttpClientUtils.post(url, data, CONTENT_TYPE_TEXT, new HashMap<>());
    }

    private SdProOrganDto queryProOrganInfo(Integer poId){
        String sql = "SELECT po.po_name AS TeamName, ui.u_realname AS Name, ui.u_name AS Tel, ui.u_sfz AS IDNum " +
                "FROM pro_organ_per pop " +
                " LEFT JOIN pro_organ po ON po.id = pop.po_id " +
                " LEFT JOIN user_info ui ON pop.user_id = ui.id " +
                "WHERE po.id = ? " +
                " AND pop.pop_type = 8 limit 1 ";
        SdProOrganDto sdProOrganDto = null;
        try {
            sdProOrganDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(poId), SdProOrganDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过班组ID:%s,查询顺德项目班组信心失败", poId));
        }
        return sdProOrganDto;
    }

    private SdProOrganPerDto.WorkerInfo queryWorkerInfo(Integer poId, Integer uId){
        String sql = "SELECT ui.u_sfz AS IdNum, ui.u_realname AS Name, ui.u_sex AS Sex " +
                " , IFNULL(knation.key_name, '未知') AS Nation " +
                " , DATE_FORMAT(ui.u_birthday, '%Y-%m-%d') AS Birthday " +
                " , IFNULL(uwork.w_resi, '未知地址') AS Address " +
                " , IFNULL(uwork.w_native, '未知籍贯') AS Native, ui.u_name AS Phone " +
                " , CASE uwork.w_edu " +
                "  WHEN uwork.w_edu IN (89, 90, 91, 92) THEN 1 " +
                "  WHEN 93 THEN 2 " +
                "  WHEN 94 THEN 3 " +
                "  WHEN 95 THEN 4 " +
                "  WHEN 96 THEN 5 " +
                "  ELSE 6 " +
                " END AS Culture, 1 AS Health " +
                " , CASE pop.pop_type " +
                "  WHEN 0 THEN 197 " +
                "  WHEN 19 THEN 4 " +
                "  WHEN 20 THEN 32 " +
                "  WHEN 21 THEN 64 " +
                "  WHEN 22 THEN 134 " +
                "  WHEN 23 THEN 2 " +
                "  WHEN 24 THEN 2 " +
                "  WHEN 117 THEN 202 " +
                "  WHEN 118 THEN 196 " +
                "  WHEN 119 THEN 195 " +
                "  WHEN 120 THEN 192 " +
                "  WHEN 121 THEN 81 " +
                "  WHEN 142 THEN 197 " +
                "  WHEN 179 THEN 197 " +
                "  WHEN 180 THEN 22 " +
                "  WHEN 181 THEN 21 " +
                "  ELSE 2 " +
                " END AS WorkerType " +
                "FROM pro_organ_per pop " +
                " LEFT JOIN user_info ui ON pop.user_id = ui.id " +
                " LEFT JOIN user_work uwork ON ui.id = uwork.u_id " +
                " LEFT JOIN key_content knation ON uwork.w_nation = knation.id " +
                "WHERE pop.po_id = ? " +
                " AND pop.user_id = ?  limit 1 ";
        SdProOrganPerDto.WorkerInfo sdProOrganPerDto = null;
        try {
            sdProOrganPerDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(poId, uId), SdProOrganPerDto.WorkerInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s,用户ID:%s查询顺德工人信息失败", poId, uId));
        }
        return sdProOrganPerDto;
    }


    /**
     * 项目进度获取
     * @param projectId 项目id
     */
    public String getProjectStep(String projectId) {
        StringBuffer url = new StringBuffer();
        url.append(RealNameConfig.SD_BASE_PATH).append(RealNameConfig.SD_PEOPLE_GETPROJECTSTEP).append("?ProjectNum=").append("CD6CB3901809E46C20CE7E8D850F66C2");
        String result = HttpClientUtils.get(url.toString(),new HashMap<>());
        return  result;
    }

    /**
     * 更新项目进度
     * @param projectId 项目id
     * @param projectStep 进度类型
     */
    public void updateProjectStep(String projectId,Integer projectStep) {
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("Token", RealNameConfig.SD_API_TOKEN);
        paranms.put("ProjectNum", "CD6CB3901809E46C20CE7E8D850F66C2");
        paranms.put("ProjectStep", projectStep);
        paranms.put("inspectNum", "53");
        String url= getUrlString(RealNameConfig.SD_PEOPLE_UPDATEPROJECTSTEP,paranms);
        String data = JSONObject.fromObject(paranms).toString();
        String result = HttpClientUtils.post(url, data, CONTENT_TYPE_TEXT, new HashMap<>());
        System.out.println(result);

    }


    /**
     * 上传人员进退场
     * @param projectId 项目id
     * @param userId 用户id
     * @param type 进退场类型
     */
    @Override
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type, Integer teamId) {

        String reqBody = null;
        String reqUrl = null;
        AuthRealNameLog logBean = logService.createBaseBean(teamId, userId, RealNameConfig.EPO);
        try {
            SdJoinOrOutWorker dto = querySdJoinOrOutWorker(projectId, teamId, userId);
            dto.setEmerPeople(RealNameConfig.SD_EMERPEOPLE);
            dto.setEmerTel(RealNameConfig.SD_EMERTEL);
            dto.setToken(RealNameConfig.SD_API_TOKEN);
            dto.setContractID("27028360");//todo
            dto.setProjectNumber("CD6CB3901809E46C20CE7E8D850F66C2");
            dto.setProjectPart("哈哈哈");
            if (type.equals("JOIN")){
                dto.setHappenTime(dto.getJoinTime());
                dto.setIn(true);
            }else {
                dto.setHappenTime(dto.getOutTime());
                dto.setIn(false);
            }
            dto.setOutTime(null);dto.setJoinTime(null);
            reqBody = jsckSon.writeValueAsString(dto);
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(reqBody);
            jsonObject.remove("joinTime");jsonObject.remove("outTime");
            System.out.println(jsonObject.toJSONString());
            reqUrl = RealNameConfig.SD_PEOPLE_ADDPROJECTPEOPLE;
            String url= getUrlString(RealNameConfig.SD_PEOPLE_ADDPROJECTPEOPLE, JSONObject.fromObject(reqBody));
            String result = HttpClientUtils.post(url, reqBody, CONTENT_TYPE_TEXT, new HashMap<>());
            RealNameUtil.SDproResIfSussess(result);
        } catch (Exception e) {
            e.printStackTrace();
            logBean.setIsSuccess(0);
            logBean.setResMsg(e.getMessage());
        }finally {
            logBean.setReqBody(reqBody);
            logBean.setReqUrl(RealNameConfig.SD_BASE_PATH + reqUrl);
            logService.saveOrUpdate(logBean);
        }

    }

    private SdJoinOrOutWorker querySdJoinOrOutWorker(Integer pId, Integer poId, Integer uId){
        String sql = "SELECT ui.u_sfz AS IDcardNum, DATE_FORMAT(pop_endtime, '%Y-%m-%d %H:%i:%s') AS outTime " +
                " , DATE_FORMAT(pop_intime, '%Y-%m-%d %H:%i:%s') AS joinTime, ub.u_bankname AS Bank " +
                " , pop.popBankid AS Account, po.po_name AS TeamName " +
                "FROM pro_organ_per pop " +
                " LEFT JOIN user_info ui ON pop.user_id = ui.id " +
                " LEFT JOIN pro_organ po ON pop.po_id = po.id " +
                " LEFT JOIN user_bank ub " +
                " ON ub.u_id = pop.user_id " +
                "  AND pop.popBankid = ub.u_bankid " +
                "WHERE po.id = ? " +
                " AND po.pro_id = ? " +
                " AND pop.user_id = ? " +
                "LIMIT 1 ";
        SdJoinOrOutWorker sdJoinOrOutWorker = null;
        try {
            sdJoinOrOutWorker = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(poId, pId, uId), SdJoinOrOutWorker.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过项目ID:%s,班组ID:%s,用户ID:%s,查询顺德员工进退场信息失败", pId, poId, uId));
        }
        return sdJoinOrOutWorker;
    }

    private SdFaceGatherDto queryFaceGatherInfo(Integer userId){
        String sql = "SELECT u_cert_pic imgBase64 ,u_sfz IDCardNum from user_info where id = ? and u_cert = 2 and u_cert_type = 2 ";
        SdFaceGatherDto sdFaceGatherDto = null;
        try {
            sdFaceGatherDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(userId), SdFaceGatherDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.format("通过用户ID:%s,查询顺德人脸认证信息失败", userId));
        }
        return sdFaceGatherDto;
    }

    /**
     * 添加/更新采集人脸图片ID
     * @param userId 用户ID
     */
    @Override
    public void saveOrUpdateFaceAcquisition(Integer userId) {

        String reqBody = null;
        AuthRealNameLog logBean = null;
        try {
            String reqUrl = RealNameConfig.SD_BASE_PATH + RealNameConfig.SD_PEOPLE_UPLOADCOLLECTEDPHOTO;
            logBean = logService.createBaseBean(userId, userId, reqUrl, RealNameConfig.KAOQIN);
            UserInfo user = userInfoDao.getUserInfoById(userId);
            if ( !user.getU_cert().equals(2) || !user.getU_cert_type().equals(2)){
                throw new BusinessException(String.format("用户ID:%s,没有认证或者认证方式不对!", userId));
            }
            SdFaceGatherDto sdFaceGatherDto = queryFaceGatherInfo(userId);
            sdFaceGatherDto.setFaceID(1);
            sdFaceGatherDto.setImgBase64(Base64Util.compressBase64(sdFaceGatherDto.getImgBase64(), ImgRatioEnum.SD_BASE64_PHOTO));
            sdFaceGatherDto.setToken(RealNameConfig.SD_API_TOKEN);
            reqBody = jsckSon.writeValueAsString(sdFaceGatherDto);//?Token=

            String result = HttpClientUtils.post(reqUrl, reqBody, CONTENT_TYPE_JSON, new HashMap<>());
            RealNameUtil.SDproResIfSussess(result);
        } catch (Exception e) {
            e.printStackTrace();
            assert logBean != null;
            logBean.setIsSuccess(0);
            logBean.setResMsg(e.getMessage());
        }finally {
            assert logBean != null;
            logBean.setReqBody(reqBody);
            logService.saveOrUpdate(logBean);
            log.info("远程调用顺德实名系统: URL: {},请求参数 {},返回结果 {},结束时间:{}", logBean.getReqUrl(), logBean.getReqBody(), logBean.getResMsg(), NumberUtil.formatDateTime(new Date()));
        }
    }

    /**
     * 添加/更新考勤机
     * @param projectId
     */
    @Override
    public void saveOrUpdateAttendanceMachine(Integer projectId) {
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("Token", RealNameConfig.SD_API_TOKEN);
        paranms.put("ProjectNumber", "CD6CB3901809E46C20CE7E8D850F66C2");
        paranms.put("Name", "考勤机名称");
        paranms.put("IP", "考勤机IP地址");
        paranms.put("Port", "考勤机IP端口，没有固定端口则填0");
        paranms.put("IsInMachine", "出入考勤机标识，对应数据字典13");
        String url= getUrlString(RealNameConfig.SD_ATTENDANCEMACHINE_SAVEORUPDATE,paranms);
        String data = JSONObject.fromObject(paranms).toString();
        String result = HttpClientUtils.post(url, data, CONTENT_TYPE_TEXT, new HashMap<>());
        System.out.println(result);
    }

    /**
     * 批量上传考勤记录
     * @param projectId
     */
    @Override
    public void synWorkerAttendanceMore(Integer projectId) {
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("Token", RealNameConfig.SD_API_TOKEN);
        paranms.put("ProjectNumber", "CD6CB3901809E46C20CE7E8D850F66C2");
        List<Map<String,Object>> dataList = new LinkedList<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("IDcardNum", "人员身份证号");
        dataMap.put("AttendanceTime", "考勤时间");
        dataMap.put("AttendanceType", "考勤类型");
        dataMap.put("IsInAttendance", "是否为进入工地标识");
        dataList.add(dataMap);
        paranms.put("Data", dataList);
        String data = JSONObject.fromObject(paranms).toString();
        String url= getUrlString(RealNameConfig.SD_ATTENDANCE_UPLOADATTENDANCEITEM,paranms);
        String result = HttpClientUtils.post(url, data, CONTENT_TYPE_TEXT, new HashMap<>());
        System.out.println(result);
    }

    /**
     * 上传文件信息（已提供前端调用接口-sd_upload.jsp）-目前弃用
     *
     */
    @Override
    public void uploadFile() {
//        "http://183.239.33.103:8888/services/rest/document/upload"
        String path = "";
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("账号", RealNameConfig.SD_API_TOKEN);
        paranms.put("密码", RealNameConfig.SD_API_TOKEN);
        paranms.put("filedata", path);
        paranms.put("filename", "文件名");
        paranms.put("folderId", "4753305");
        String data = JSONObject.fromObject(paranms).toString();
        String url= getUrlString(RealNameConfig.SD_REST_DOCUMENT_UPLOAD,paranms);
        String result = HttpClientUtils.post(url, data, CONTENT_TYPE_TEXT, new HashMap<>());
    }

    @Override
    public void createFileDirForProject(Integer projectId) {
        Map<String, Object> map = new HashMap<>();
        map.put("账号", "");
        map.put("密码", "");
        map.put("父文件夹ID", "");
        map.put("账号", "");
    }

    @Override
    public String getpSent() {
        return "SD";
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }
}
