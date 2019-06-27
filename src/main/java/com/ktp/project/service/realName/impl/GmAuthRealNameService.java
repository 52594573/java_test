package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.dao.GmProjectDao;
import com.ktp.project.dao.KtpErrorLogDao;
import com.ktp.project.dao.ProjectDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.AuthRealName.*;
import com.ktp.project.entity.GmProjectInfoEntity;
import com.ktp.project.entity.KtpCompanyInfoEntity;
import com.ktp.project.entity.KtpErrorLogEntity;
import com.ktp.project.entity.KtpProjectInfoEntity;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.GmAuthRealNameApi;
import com.ktp.project.util.*;
import com.zm.entity.ProOrgan;
import com.zm.entity.ProOrganPerDAO;
import com.zm.entity.Project;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 高明项目实现类
 */
@Service("gmAuthRealNameService")
@Transactional
public class GmAuthRealNameService implements AuthRealNameApi, GmAuthRealNameApi {

    private static final Logger log = LoggerFactory.getLogger(GmAuthRealNameService.class);
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private GmProjectDao gmProjectDao;
    @Autowired
    private ProOrganPerDAO proOrganPerDAO;
    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private KtpErrorLogDao ktpErrorLogDao;

    /**
     * 获取拼接地址，并调用
     *
     * @param paranms -参数
     * @param url     -访问接口地址
     * @return
     */
    public static String getUrlString(Map<String, Object> paranms, String url) {
        StringBuffer str = new StringBuffer();
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();
            //获取当前时间的年月日
            String timestamp = df.format(time);
            //加密的字段
            String parmStr = getParmStr(paranms, timestamp);
            System.out.println("Str===" + parmStr);
            str.append(RealNameConfig.GM_BASE_PATH).append(url).append("?").
                    append("api_key=").append(URLEncoder.encode(RealNameConfig.GM_API_KEY, "UTF-8"))
                    .append("&api_version=").append(URLEncoder.encode(RealNameConfig.GM_API_VERSION, "UTF-8"))
                    .append("&timestamp=").append(URLEncoder.encode(timestamp, "UTF-8"))
                    .append("&signature=").append(Md5Util.encryption(parmStr).toUpperCase());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("url===" + str.toString());
        return str.toString();
    }

    /**
     * 拼接需要加密的Str
     *
     * @param paranms -参数
     * @return
     */
    public static String getParmStr(Map<String, Object> paranms, String dataStr) {
        StringBuffer str = new StringBuffer();
        str.append(RealNameConfig.GM_API_KEY).append("api_key").append(RealNameConfig.GM_API_KEY)
                .append("api_version").append(RealNameConfig.GM_API_VERSION);
        JSONObject json = JSONObject.fromObject(paranms);
        str.append("body").append(json.toString());
        str.append("timestamp").append(dataStr).append(RealNameConfig.GM_API_KEY);
        String body = RealNameConfig.GM_API_KEY + "api_key" + RealNameConfig.GM_API_KEY + "api_version" + RealNameConfig.GM_API_VERSION +
                "body" + json.toString() + "timestamp" + dataStr + RealNameConfig.GM_API_KEY;
        return body;
    }

    /**
     * 发送调用的地址,并记录错误日志
     *
     * @param paranms -参数
     * @return
     */
    public JSONObject sendUrl(Map<String, Object> paranms, String url, String method, Integer projectId) {
        String data = JSONObject.fromObject(paranms).toString();
        String result = HttpClientUtils.post(url, data, "text/html;charset=UTF-8", new HashMap<>());
        JSONObject object = new JSONObject();
        try {
            result = URLDecoder.decode(result, "UTF-8");
            object = JSONObject.fromObject(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        System.out.println("参数==" + paranms.toString());
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            String msg = String.valueOf(object.get("detail_message"));
            if ("uploadRosterInfo".equals(method)) {
                paranms = new HashMap<>();
            }
            saveOrUpdateGmProject(method, url, msg, projectId, paranms.toString());
            // RealNameUtil.GMproResIfSussess(result);
            return object;
        }
        return object;
    }

    /**
     * 保存错误日志信息
     */
    public void saveOrUpdateGmProject(String method, String url, String msg, Integer project_id, String parm_str) {
        KtpErrorLogEntity entity = new KtpErrorLogEntity();
        entity.setApi_method(method);
        entity.setApi_url(url);
        entity.setError_info(msg);
        entity.setProject_id(project_id);
        entity.setRegion_code("gm");
        entity.setParm_str(parm_str);
        ktpErrorLogDao.saveOrUpdateGmProject(entity);
    }

    /**
     * 获取数据字典
     *
     * @param type -数据类型
     * @return list  符合条件的list
     */
    public static List getBaseDataDictionary(int type) {
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("type", type);
        String url = getUrlString(paranms, RealNameConfig.GM_DICTIONARY_URL);
        String data = JSONObject.fromObject(paranms).toString();
        String result = HttpClientUtils.post(url, data, "text/html;charset=UTF-8", new HashMap<>());
        JSONObject object = JSONObject.fromObject(result);
        //JSONObject object = sendUrl( paranms,  url,"getBaseDataDictionary",0);
        List result_data = (List) object.get("result_data");
        return result_data;
    }

    public static void main(String[] args) {
        //System.out.println(getBaseDataDictionary(22));
        //String s= "18811361543_123456_v1.0";
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("phone", "18811361543");
        paranms.put("userImea", "123456");
        paranms.put("version", "v1.0");
        String data = JSONObject.fromObject(paranms).toString();

        try {
            data = URLEncoder.encode(data, "UTF-8");
            System.out.println("加密" + data);
            data = URLDecoder.decode(data, "UTF-8");
            System.out.println("解密" + data);
            JSONObject object = JSONObject.fromObject(data);
            System.out.println("解密key" + object.get("phone"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // System.out.println("加密"+ Md5Util.encryption(s).toUpperCase());


    }


    /**
     * 获得工种类型
     *
     * @param workType
     * @return
     */
    public static Integer getworkType(Integer workType) {
        Integer newWorkType = 174;
        if (null == workType) {
            return newWorkType;
        }
        if (workType == 19) {
            newWorkType = 159;
        } else if (workType == 20) {
            newWorkType = 137;
        } else if (workType == 21) {
            newWorkType = 139;
        } else if (workType == 22) {
            newWorkType = 138;
        } else if (workType == 23) {
            newWorkType = 154;
        }
        return newWorkType;
    }

    /**
     * 活动教育类型
     *
     * @param levelType ktp系统教育等级
     * @return 高明教育等级
     */
    public static Integer getCultureLevelType(Integer levelType) {
        Integer newLevelType = 291;
        if (null == levelType) {
            return newLevelType;
        }
        if (levelType == 89) {
            newLevelType = 283;
        } else if (levelType == 90) {
            newLevelType = 284;
        } else if (levelType == 91) {
            newLevelType = 285;
        } else if (levelType == 92) {
            newLevelType = 286;
        } else if (levelType == 93) {
            newLevelType = 287;
        } else if (levelType == 94) {
            newLevelType = 288;
        } else if (levelType == 95) {
            newLevelType = 289;
        } else if (levelType == 96) {
            newLevelType = 290;
        }
        return newLevelType;
    }

    /**
     * 同步企业信息
     */
    @Override
    public void synCompanyInfo(String corpCode, String corpName, String areaCode, String registerDate) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("corpCode", corpCode);
        paranms.put("corpName", corpName);
        paranms.put("areaCode", areaCode);
        paranms.put("registerDate", registerDate);
        String url = getUrlString(paranms, RealNameConfig.GM_COMPANYINFO_URL);
        sendUrl(paranms, url, "synCompanyInfo", 0);
    }

    @Override
    public String getpSent() {
        return "GM";
    }

    /**
     * 上传参建单位
     *
     * @param projectId 项目id
     */
    public void uploadParticipateInfo(Integer projectId) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("corpCode", gmProjectInfo.getCorpCode());
        paranms.put("corpName", gmProjectInfo.getCorpName());
        paranms.put("corpType", 131);
        paranms.put("entryTime", gmProjectInfo.getCorpIntime());
        String url = getUrlString(paranms, RealNameConfig.GM_PARTICIPATEINFO_URL);
        sendUrl(paranms, url, "uploadParticipateInfo", projectId);
    }

    /**
     * 上传项目信息
     */
    @Override
    public void synWorkinfo(Integer projectId) {
        Project project = projectDao.getProjectById(projectId);
        List<KtpProjectInfoEntity> entityList = gmProjectDao.getProjectInfo(projectId);
        Map<String, Object> paranms = new LinkedHashMap<>();
        for (KtpProjectInfoEntity entity : entityList) {
            paranms.put("contractorCorpCode", entity.getContractor_corp_code());
            paranms.put("contractorCorpName", entity.getContractor_corp_name());
            paranms.put("buildCorpName", entity.getBuild_corp_name());
            paranms.put("buildCorpCode", entity.getBuild_corp_code());
            JSONArray bookCotentjson = JSONArray.fromObject(entity.getBuilderLicenses());
            paranms.put("builderLicenses", bookCotentjson);
            paranms.put("areaCode", entity.getArea_code());
        }
        paranms.put("name", project.getPName());
        paranms.put("category", 72);
        paranms.put("prjStatus", 87);
        paranms.put("thirdPartyProjectCode", String.valueOf(projectId));
        String url = getUrlString(paranms, RealNameConfig.GM_PROJECTINFO_URL);
        JSONObject object = sendUrl(paranms, url, "synWorkinfo", projectId);
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            return;
        }
        //记录上传得高明得项目
        GmProjectInfoEntity gmProjectInfoEntity = new GmProjectInfoEntity();
        String resData = String.valueOf(object.get("result_data"));
        gmProjectInfoEntity.setCorp_code(paranms.get("buildCorpCode").toString());
        gmProjectInfoEntity.setCorp_name(paranms.get("buildCorpName").toString());
        gmProjectInfoEntity.setProject_code(resData);
        gmProjectInfoEntity.setProject_id(projectId);
        gmProjectInfoEntity.setRegion_code("gm");
        gmProjectInfoEntity.setCorp_intime(project.getPIntime());
        gmProjectDao.saveOrUpdateGmProject(gmProjectInfoEntity);

    }

    /**
     * 上传和修改班组信息
     *
     * @param projectId 项目id
     * @param teamId    班组id
     * @param type      类型（POSAVE-新增，POUPDATE-修改）
     */
    @Override
    public void uploadTeamInfo(Integer projectId, Integer teamId, String teamName, String type) {
        Map<String, Object> paranms = new HashMap<>();
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("上传和修改班组信息,通过项目ID:%s,查询不到项目信息!", projectId));
        }
        JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
        String url;
        switch (type) {
            case "POSAVE":
                //新增班组
                paranms.put("projectCode", gmProjectInfo.getProjectCode());
                paranms.put("corpCode", gmProjectInfo.getCorpCode());
                paranms.put("corpName", gmProjectInfo.getCorpName());
                paranms.put("entryTime", gmProjectInfo.getCorpIntimeDay());
                url = RealNameConfig.GM_TEAMINFO_URL;
                break;
            case "POUPDATE":
                //修改班组
                paranms.put("teamCode", teamCode.get(String.valueOf(teamId)));
                url = RealNameConfig.GM_UPDATE_TEAMINFO_URL;
                break;
            default:
                return;
        }
        paranms.put("teamName", teamName);
        url = getUrlString(paranms, url);
        JSONObject jsonObject = sendUrl(paranms, url, "uploadTeamInfo", projectId);
        String code = String.valueOf(jsonObject.get("result"));
        if (!"true".equals(code)) {
            return;
        }
        //新增班组时记录班组code
        if ("POSAVE".equals(type)) {
            //71094FDD6D1B4071B697583BC1B62033
            Map teamCodeMap = new LinkedHashMap();
            String result_data = (String) jsonObject.get("result_data");
            teamCodeMap.putAll(teamCode);
            teamCodeMap.put(String.valueOf(teamId), result_data);
            String data = JSONObject.fromObject(teamCodeMap).toString();
            GmProjectInfoEntity gmProjectInfoEntity = new GmProjectInfoEntity();
            gmProjectInfoEntity.setId(gmProjectInfo.getId());
            //gmProjectInfoEntity.setCorp_code(gmProjectInfo.getCorpCode());
            //gmProjectInfoEntity.setCorp_name(gmProjectInfo.getCorpName());
            //gmProjectInfoEntity.setProject_code(gmProjectInfo.getProjectCode());
            //gmProjectInfoEntity.setProject_id(gmProjectInfo.getProjectId());
            gmProjectInfoEntity.setTeam_code(data);
            //gmProjectInfoEntity.setRegion_code(gmProjectInfo.getRegionCode());
            // gmProjectInfoEntity.setCorp_intime(gmProjectInfo.getCorp_intime_date());
            gmProjectDao.updateGmProject(gmProjectInfoEntity);
        }
    }

    /**
     * \
     * 上传项目人员信息
     *
     * @param projectId 项目id
     * @param teamId    班组id
     */
    public void uploadRosterInfo(Integer projectId, Integer teamId, Integer uerId) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("上传项目人员信息,通过项目ID:%s,查询不到项目信息!", projectId));
        }
        Map<String, Object> paranms = new HashMap<>();
        if (gmProjectInfo.getTeamCode() == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到班组信息!", projectId));
        }
        JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("corpCode", gmProjectInfo.getCorpCode());
        paranms.put("corpName", gmProjectInfo.getCorpName());
        paranms.put("teamCode", teamCode.get(String.valueOf(teamId)));

        GmOrganInfo gmOrganInfo = proOrganPerDAO.getGmOrganInfo(teamId);
        int isTeamLeader = 0;
        //该班组下该用户为班组长
        if (uerId.equals(gmOrganInfo.getPo_fzr())) {
            isTeamLeader = 1;
        }
        //查询用户信息
        List<GmUserInfo> userInfoList = proOrganPerDAO.getUserInfo(teamId, uerId);
        List<Map<String, Object>> workerList = new LinkedList<>();
        String headImage;
        for (GmUserInfo gmUserInfo : userInfoList) {
            Map<String, Object> workerMap = new LinkedHashMap<>();
            workerMap.put("workerName", gmUserInfo.getU_realname());
            workerMap.put("isTeamLeader", isTeamLeader);
            workerMap.put("idCardType", 46);
            workerMap.put("idCardNumber", gmUserInfo.getU_sfz());
            workerMap.put("workType", getworkType(gmUserInfo.getW_gzid()));
            workerMap.put("workRole", 176);
            if (gmUserInfo.getKey_name() != null) {
                workerMap.put("nation", gmUserInfo.getKey_name());
            } else {
                workerMap.put("nation", "汉");
            }
            if (gmUserInfo.getW_resi() != null && !"".equals(gmUserInfo.getW_resi())) {
                workerMap.put("address", gmUserInfo.getW_resi());
            } else {
                workerMap.put("address", "无");
            }
            headImage = gmUserInfo.getU_pic();
            if (StringUtil.isEmpty(headImage)) {
                headImage = "https://images.ktpis.com/pic1.png";
            } else {
                boolean status = gmUserInfo.getU_pic().contains("https:");
                //无前缀时加上前缀
                if (!status) {
                    headImage = "https://t.ktpis.com" + headImage;
                }
                //解析异常时给默认头像
                try {
                    Base64Util.Image2Base64(headImage, 0.5F);
                } catch (Exception e) {
                    headImage = "https://images.ktpis.com/pic1.png";
                }
            }
            workerMap.put("headImage", Base64Util.Image2Base64(headImage, 0.5F));
            workerMap.put("politicsType", 282);
            if (gmUserInfo.getU_name() != null && !"".equals(gmUserInfo.getU_name())) {
                workerMap.put("cellPhone", gmUserInfo.getU_name());
            } else {
                workerMap.put("cellPhone", "无");
            }
            workerMap.put("cultureLevelType", getCultureLevelType(gmUserInfo.getW_edu()));
            if (gmUserInfo.getW_zs() != null && !"".equals(gmUserInfo.getW_zs())) {
                workerMap.put("grantOrg", gmUserInfo.getW_zs());
            } else {
                workerMap.put("grantOrg", "无");
            }
            workerList.add(workerMap);
        }
        paranms.put("workerList", workerList);
        String url = getUrlString(paranms, RealNameConfig.GM_WORKINFO_URL);
        sendUrl(paranms, url, "uploadRosterInfo", projectId);
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }

    @Override
    public void synBuildPo(Integer projectId, Integer userId, String type) {
    }

    /**
     * 上传人员进退场
     *
     * @param projectId 项目id
     * @param userId    用户id
     * @param type      进退场类型
     */
    @Override
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type, Integer teamId) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
        List<GmUserInfo> proOrganPerList = proOrganPerDAO.getUserInfo(teamId, userId);
        if (proOrganPerList != null && !proOrganPerList.isEmpty()) {
            for (GmUserInfo gmUserInfo : proOrganPerList) {
                Map<String, Object> paranms = new HashMap<>();
                paranms.put("projectCode", gmProjectInfo.getProjectCode());
                paranms.put("corpCode", gmProjectInfo.getCorpCode());
                paranms.put("corpName", gmProjectInfo.getCorpName());
                paranms.put("teamCode", teamCode.get(String.valueOf(teamId)));
                List<Map<String, Object>> addWorkerLists = new ArrayList<>();
                Map<String, Object> workerMap = new HashMap<>();
                workerMap.put("idCardType", 46);
                workerMap.put("idCardNumber", gmUserInfo.getU_sfz());
                //设置要获取到什么样的时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (gmUserInfo.getPop_state() == 0) {
                    workerMap.put("type", 296);
                    workerMap.put("date", sdf.format(gmUserInfo.getPop_intime()));
                } else {
                    workerMap.put("type", 297);
                    //如果为空时默认场时间2010-12-31
                    if (gmUserInfo.getPop_endtime() == null) {
                        workerMap.put("date", "2010-12-31");
                    } else {
                        workerMap.put("date", sdf.format(gmUserInfo.getPop_endtime()));
                    }
                }
                addWorkerLists.add(workerMap);
                paranms.put("workerList", addWorkerLists);
                String url = getUrlString(paranms, RealNameConfig.GM_JOINOROUT_URL);
                sendUrl(paranms, url, "synBuildPoUserJT", projectId);
            }
        } else {
            throw new BusinessException(String.format("通过用户ID:%s,班组id查询不到用户信息!", teamId, userId));
        }


    }

    /**
     * 上传考勤信息
     *
     * @param projectId 项目id
     * @param kaoQinId  考勤id
     */
    @Override
    public void synWorkerAttendance(Integer projectId, Integer kaoQinId) {
        Map<String, Object> paranms = new HashMap<>();
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
        GmKaoQinDto gmKaoQinDto = queryGmKaoQinInfo(projectId, kaoQinId);
        //获取班组id
        int teamId = proOrganPerDAO.findTeamId(projectId, gmKaoQinDto.getUserId());
        //获取班组code
        paranms.put("teamCode", teamCode.get(teamId + ""));
        List<Map<String, Object>> addDataLists = new ArrayList<>();
        Map<String, Object> workerMap = new HashMap<>();
        workerMap.put("idCardType", 46);
        workerMap.put("idCardNumber", gmKaoQinDto.getBuilderIdcard());
        workerMap.put("direction", Integer.valueOf(gmKaoQinDto.getCheckType()));
        workerMap.put("date", gmKaoQinDto.getAtteTime() + ".000");
        addDataLists.add(workerMap);
        paranms.put("dataList", addDataLists);
        String url = getUrlString(paranms, RealNameConfig.GM_ATTENDANCE_URL);
        sendUrl(paranms, url, "synWorkerAttendance", projectId);
    }

    /**
     * 获取考勤信息
     *
     * @param projectId 项目id
     * @param kaoqinId  考勤id
     * @return 考勤对象
     */
    private GmKaoQinDto queryGmKaoQinInfo(Integer projectId, Integer kaoqinId) {
        String sql = "SELECT ui.u_sfz AS builderIdcard, date_format(kq.`k_time`,'%Y-%m-%d %H:%i:%S')  AS atteTime ," +
                "  CASE WHEN kq.`k_state`=1 or  kq.`k_state`=3 or kq.`k_state`=5  THEN '303' ELSE '304' " +
                " END AS checkType , ui.id as  userId FROM kaoqin" + projectId + "  kq " +
                "    LEFT JOIN user_info ui ON kq.`user_id` = ui.`id`  " +
                "   where   kq.id =" + kaoqinId;
        GmKaoQinDto gmKaoQinDto;
        try {
            gmKaoQinDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(), GmKaoQinDto.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(String.format("通过项目ID:%s, 考勤ID:%s查询结果失败", projectId, kaoqinId));
        }
        return gmKaoQinDto;
    }

    /**
     * 查询项目所有考勤
     *
     * @param projectId 项目id
     * @return 所有考勤信息
     */
    public List<KqIdAndUserId> queryMyProxyClockInUserIds(int projectId) {
        String hql = "select  id as kqId , user_id as userId from kaoqin" + projectId + " where pro_id = ? ";
        return queryChannelDao.selectManyAndTransformer(hql, Lists.newArrayList(projectId), KqIdAndUserId.class);
    }

    /**
     * 上传劳动合同
     *
     * @param projectId    项目id
     * @param idCardNumber 身份证号
     */
    public void uploadContract(Integer projectId, String idCardNumber) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        String s = "https://t.ktpis.com/upload/ufile/201799/201799164226551891869.doc";
        byte[] encodedBytes = java.util.Base64.getEncoder().encode(s.getBytes());
        String bsseCode = new String(encodedBytes, java.nio.charset.Charset.forName("UTF-8"));
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        List<Map<String, Object>> contractList = new ArrayList<>();
        Map<String, Object> workerMap = new HashMap<>();
        workerMap.put("corpCode", gmProjectInfo.getCorpCode());
        workerMap.put("corpName", gmProjectInfo.getCorpName());
        workerMap.put("idCardType", 46);
        workerMap.put("idCardNumber", idCardNumber);
        workerMap.put("startDate", "2018-01-01");
        workerMap.put("endDate", "2018-12-31");
        workerMap.put("contractPeriodType", 299);
        List<Map<String, Object>> attachments = new ArrayList<>();
        Map<String, Object> attachmentsMap = new HashMap<>();
        attachmentsMap.put("name", "工人合同附件");
        attachmentsMap.put("data", bsseCode);
        attachments.add(attachmentsMap);
        workerMap.put("attachments", attachments);
        contractList.add(workerMap);
        paranms.put("contractList", contractList);
        String url = getUrlString(paranms, RealNameConfig.GM_CONTRACT_URL);
        sendUrl(paranms, url, "uploadContract", projectId);
    }

    /**
     * 上传工资单信息
     *
     * @param projectId 项目id
     * @paramb
     */
    public void ploadPayroll(Integer projectId, Integer teamId, String idCardNumber, String bankCardNumber, String thirdPayRollCode) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        String s = "https://t.ktpis.com/upload/ufile/201799/201799164226551891869.doc";
        byte[] encodedBytes = java.util.Base64.getEncoder().encode(s.getBytes());
        String bsseCode = new String(encodedBytes, java.nio.charset.Charset.forName("UTF-8"));
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("corpCode", gmProjectInfo.getCorpCode());
        paranms.put("corpName", gmProjectInfo.getCorpName());
        JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
        paranms.put("teamCode", teamCode.get(String.valueOf(teamId)));
        paranms.put("payMonth", "2018-12");
        List<Map<String, Object>> attachments = new ArrayList<>();
        Map<String, Object> attachmentsMap = new HashMap<>();
        attachmentsMap.put("name", "工资单附件");
        attachmentsMap.put("data", bsseCode);
        attachments.add(attachmentsMap);
        paranms.put("attachments", attachments);
        String url = getUrlString(paranms, RealNameConfig.GM_PAYROLL_URL);
        JSONObject object = sendUrl(paranms, url, "ploadPayroll", projectId);
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            return;
        }
        String resData = String.valueOf(object.get("result_data"));
        //上传工资单详情
        paranms = new HashMap<>();
        paranms.put("PayrollCode", resData);
        List<Map<String, Object>> detailList = new ArrayList<>();
        Map<String, Object> detailMap = new HashMap<>();
        detailMap.put("idCardType", 46);
        detailMap.put("idCardNumber", idCardNumber);
        detailMap.put("days", 20);
        detailMap.put("workHours", 160);
        detailMap.put("payRollBankCardNumber", bankCardNumber);
        detailMap.put("payRollBankCode", 182);
        detailMap.put("payRollBankName", "建设银行");
        detailMap.put("payBankCardNumber", bankCardNumber);
        detailMap.put("payBankCode", 182);
        detailMap.put("payBankName", "建设银行");
        detailMap.put("totalPayAmount", new BigDecimal(5000.00));
        detailMap.put("actualAmount", new BigDecimal(4900.00));
        detailMap.put("isBackPay", 0);
        detailMap.put("balanceDate", "2018-12-30");
        detailMap.put("thirdPayRollCode", thirdPayRollCode);
        detailList.add(detailMap);
        paranms.put("detailList", detailList);
        url = getUrlString(paranms, RealNameConfig.GM_PAYROLLDETAIL_URL);
        sendUrl(paranms, url, "uploadPayrollDetail", projectId);
    }

    /**
     * 上传培训课程信息
     *
     * @param projectId 项目id
     * @paramb
     */
    public void uploadTrainInfo(Integer projectId, String idCardNumber) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "gm");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("trainingDate", "2018-12-22");
        paranms.put("trainingDuration", 8);
        paranms.put("trainingName", "保利文玥花园项目工程技能培训");
        paranms.put("trainingTypeCode", 319);
        String url = getUrlString(paranms, RealNameConfig.GM_TRAININFO_URL);
        JSONObject object = sendUrl(paranms, url, "uploadTrainInfo", projectId);
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            return;
        }
        String resData = String.valueOf(object.get("result_data"));
        //上传工资单详情
        paranms = new HashMap<>();
        paranms.put("trainCode", resData);
        List<Map<String, Object>> workersList = new ArrayList<>();
        Map<String, Object> workersMap = new HashMap<>();
        workersMap.put("idCardType", 46);
        workersMap.put("idCardNumber", idCardNumber);
        workersMap.put("isPass", 1);
        workersList.add(workersMap);
        paranms.put("workers", workersList);
        url = getUrlString(paranms, RealNameConfig.GM_TRAINWORKERINFO_URL);
        sendUrl(paranms, url, "uploadTrainWorkerInfo", projectId);
    }


    /**
     * 上传所有企业信息
     */
    public void synAllCompanyInfo() {
        List<KtpCompanyInfoEntity> companyInfoList = gmProjectDao.getKtpCompanyInfo();
        if (companyInfoList != null) {
            for (KtpCompanyInfoEntity entity : companyInfoList) {
                synCompanyInfo(entity.getCorp_code(), entity.getCorp_name(), entity.getArea_code(), entity.getRegister_date());
            }
        }
    }

    /**
     * 根据项目id上传所有的项目相关的信息
     */
    public void synAllInfoByProjectId(Integer projectId) {

        try {
            //上传企业信息
            // synAllCompanyInfo();

            //上传项目信息
            // synWorkinfo(projectId);
            //上传参建信息
            // uploadParticipateInfo(projectId);

            List<TeamAndUserInfo> teamAndUserInfoList = gmProjectDao.getTeamAndUserInfo(projectId);
            //记录所有班组id
            Map<String, String> teamMap = new LinkedHashMap<>();
            //记录所有用户
            Map<String, String> userMap = new LinkedHashMap<>();
            for (TeamAndUserInfo eamAndUserInfo : teamAndUserInfoList) {
                teamMap.put(eamAndUserInfo.getTeamId().toString(), eamAndUserInfo.getPoName());
                userMap.put(eamAndUserInfo.getUserId().toString(), eamAndUserInfo.getUserId().toString());
            }
            //上传班组信息
       /*     for (String  teamId: teamMap.keySet()) {
                uploadTeamInfo(projectId,  Integer.valueOf(teamId), teamMap.get(teamId),  "POSAVE");
            }*/
            Integer teamId;
            Integer userId;
            //上传项目人员信息,人员进退场信息
    /*        for (TeamAndUserInfo eamAndUserInfo:teamAndUserInfoList) {
                teamId= Integer.valueOf(eamAndUserInfo.getTeamId());
                userId=Integer.valueOf(eamAndUserInfo.getUserId());
                //项目信息
                uploadRosterInfo( projectId,  teamId , userId);
                //进退场
               synBuildPoUserJT(projectId,  userId, "",teamId);
            }*/
            //上传项目下所有考勤
       /*     List<KqIdAndUserId>   kqIdAndUserIdList= queryMyProxyClockInUserIds(projectId);
            for (KqIdAndUserId kqIdAndUserId:kqIdAndUserIdList) {
                //判断该用户是否在该项目下，不在,不上传
                if(userMap.get(String.valueOf(kqIdAndUserId.getUserId())) !=null){
                    synWorkerAttendance( projectId,kqIdAndUserId.getKqId());
                }
            }*/
            //上传劳动合同
            uploadContract(projectId, "511025198803190640");
            //  ploadPayroll( projectId, 702,"512928195607151631","6217007200044971425",  "100861");
            // uploadTrainInfo( projectId, "512928195607151631");
        } catch (Exception e) {
            log.error("同步高明数据失败异常{}", e);
        }
    }

}
