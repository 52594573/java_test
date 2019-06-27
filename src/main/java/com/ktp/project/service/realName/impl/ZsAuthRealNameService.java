package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.constant.ImgRatioEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.dao.*;
import com.ktp.project.dto.AuthRealName.*;
import com.ktp.project.entity.GmProjectInfoEntity;
import com.ktp.project.entity.KtpErrorLogEntity;
import com.ktp.project.entity.KtpPoOutEntity;
import com.ktp.project.entity.KtpProjectInfoEntity;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.ZsAuthRealNameApi;
import com.ktp.project.util.Base64Util;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.RealNameUtil;
import com.ktp.project.util.StringUtil;
import com.zm.entity.*;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ktp.project.service.realName.impl.GmAuthRealNameService.getUrlString;

/**
 * 中山项目实现类
 */
@Service("zsAuthRealNameService")
@Transactional
public class ZsAuthRealNameService implements AuthRealNameApi, ZsAuthRealNameApi {

    private static final Logger log = LoggerFactory.getLogger(ZsAuthRealNameService.class);
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
    @Autowired
    private KtpPoOutDao ktpPoOutDao;
    @Autowired
    private ProOrganDAO proOrganDAO;
    @Autowired
    private KaoqinDao kaoqinDao;


    /**
     * 获取拼接地址，并调用
     *
     * @param paranms -参数
     * @param url     -访问接口地址
     * @return
     */
   /* public static String getStringDate(Map<String, Object> paranms, String url) {
        return str.toString();
    }*/


    /**
     * 拼接需要加密的Str
     *
     * @param paranms -参数
     * @return
     */
    public static String getParmStr(Map<String, Object> paranms, String dataStr) {
        StringBuffer str = new StringBuffer();
        str.append(RealNameConfig.ZS_API_KEY).append("api_key").append(RealNameConfig.ZS_API_KEY)
                .append("api_version").append(RealNameConfig.ZS_API_VERSION);
        JSONObject json = JSONObject.fromObject(paranms);
        str.append("body").append(json.toString());
        str.append("timestamp").append(dataStr).append(RealNameConfig.ZS_API_KEY);
        String body = RealNameConfig.ZS_API_KEY + "api_key" + RealNameConfig.ZS_API_KEY + "api_version" + RealNameConfig.ZS_API_VERSION +
                "body" + json.toString() + "timestamp" + dataStr + RealNameConfig.ZS_API_KEY;
        return body;
    }

    /**
     * 发appid=qg-100238&data=%7B%22corpCode%22%3A%229144078319428766XQ%22%2C%22corpName%22%3A%22%E5%BC%80%E5%B9%B3%E4%BD%8F%E5%AE%85%E5%BB%BA%E7%AD%91%E5%B7%A5%E7%A8%8B%E9%9B%86%E5%9B%A2%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%22%2C%22areaCode%22%3A%22440608%22%2C%22registerDate%22%3A%221933-05-26%22%7D&format=json&method=Corp.Upload&nonce=91bb0093-f608-4f94-8a1f-8e09b6bf5a55&timestamp=20190114124158&version=1.0&sign=05b9791645d8376f13d5680047ec93359d667115a984fc7ed59ffd35b170f765送调用的地址,并记录错误日志
     *
     * @param -参数
     * @return
     */
    public JSONObject sendUrl(String data, String url) {
        String result = HttpClientUtils.post(url, data, "application/x-www-form-urlencoded; charset=UTF-8", new HashMap<>());
        JSONObject object = new JSONObject();
        try {
            result = URLDecoder.decode(result, "UTF-8");
            object = JSONObject.fromObject(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info(result);
        /*String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            String msg = String.valueOf(object.get("detail_message"));
            saveOrUpdateGmProject(method,url,msg,projectId,data);
            RealNameUtil.GMproResIfSussess(result);
            return object;
        }*/
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
        entity.setRegion_code("zs");
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
       /* Map<String, Object> paranms = new HashMap<>();
        paranms.put("type", type);
        String url = getUrlString(paranms, RealNameConfig.GM_DICTIONARY_URL);
        String data = JSONObject.fromObject(paranms).toString();
        String result = HttpClientUtils.post(url, data, "text/html;charset=UTF-8", new HashMap<>());
        JSONObject object = JSONObject.fromObject(result);
        //JSONObject object = sendUrl( paranms,  url,"getBaseDataDictionary",0);
        List result_data = (List) object.get("result_data");
        return result_data;*/
        return null;
    }

    /**
     * @param paranms 上传数据
     * @param method  方法名称
     * @Description:
     * @Author: liaosh
     * @Date: 2019/1/12 0012
     */
    public String getBody(Map<String, Object> paranms, String method) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.now();
        //获取当前时间的年月日
        String timestamp = df.format(time);
        //加密的字段
        Map<String, Object> paranms1 = new LinkedHashMap<>();
        //paranms1.put("appSecret", RealNameConfig.ZS_API_KEY);
        String data = JSONObject.fromObject(paranms).toString();

        paranms1.put("appid", RealNameConfig.ZS_APPID);
        paranms1.put("data", data);
        paranms1.put("format", "json");
        paranms1.put("method", method);
        paranms1.put("nonce", UUID.randomUUID().toString());
        paranms1.put("timestamp", timestamp);
        paranms1.put("version", RealNameConfig.ZS_API_VERSION);
        /*签名开始*/
        StringBuffer str = new StringBuffer();
        for (String key : paranms1.keySet()) {
            if ("appid".equals(key)) {
                str.append(key).append("=").append(paranms1.get(key));
            } else {
                str.append("&").append(key).append("=").append(paranms1.get(key));
            }
        }
        //带上appSecret加密
        String mykey = str + "&appSecret=" + RealNameConfig.ZS_API_KEY;
        String sign = mykey.toLowerCase();
        /*签名结束*/
        paranms1.put("sign", getSHA256StrJava(sign));
        //签名之后urlencode
        try {
            data = URLEncoder.encode(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        paranms1.put("data", data);
        str = new StringBuffer();
        //为了带上sign
        for (String key : paranms1.keySet()) {
            if ("appid".equals(key)) {
                str.append(key).append("=").append(paranms1.get(key));
            } else {
                str.append("&").append(key).append("=").append(paranms1.get(key));
            }
        }


        //String result = HttpClientUtils.post(RealNameConfig.ZS_BASE_PATH, str.toString(), " application/x-www-form-urlencoded; charset=UTF-8", new HashMap<>());

        return str.toString();
    }

    /**
     * 　　* 利用java原生的摘要实现SHA256加密
     * 　　* @param str 加密后的报文
     * 　　* @return
     */
    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 　　* 将byte转为16进制
     * 　　* @param bytes
     * 　　* @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
//1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 获得工种类型
     *
     * @param workType
     * @return
     */
    public static String getworkType(Integer workType) {
        String newWorkType = "1000";
        if (null == workType) {
            return newWorkType;
        }
        if (workType == 19) {
            newWorkType = "240";
        } else if (workType == 20) {
            newWorkType = "020";
        } else if (workType == 21) {
            newWorkType = "040";
        } else if (workType == 22) {
            newWorkType = "030";
        } else if (workType == 23) {
            newWorkType = "190";
        }
        return newWorkType;
    }

    /**
     * 活动教育类型
     *
     * @param levelType ktp系统教育等级
     * @return 中山教育等级
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
     *批量考勤
     */
    @Override
    public void kaoqingAll(Integer projectId) {
        List<Integer> lists = kaoqinDao.queryIdByPro(projectId);
        Integer i = 0;
        for(Integer list:lists){
            try {
                i++;
                synWorkerAttendance(projectId,list);
            }catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("总数："+i);
        }
    }

    /**
     * 上传考勤
     * @param projectId
     * @param kaoQinId
     */
    @Override
    public void synWorkerAttendance(Integer projectId, Integer kaoQinId)  {
        try {
            Map<String, Object> paranms = new HashMap<>();
            GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
            if (gmProjectInfo == null) {
                throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
            }
            paranms.put("projectCode", gmProjectInfo.getProjectCode());
            JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
            GmKaoQinDto gmKaoQinDto = queryZsKaoQinInfo(projectId, kaoQinId);
            //久远数据，userId==null
            if (gmKaoQinDto.getUserId() == null || gmKaoQinDto.getUserId() == 0) {
                throw new NullPointerException("中山项目考勤同步查询为空");
            }
            //获取班组id
            int teamId = proOrganPerDAO.findTeamId(projectId, gmKaoQinDto.getUserId());
            String teamSysNo = gmProjectDao.findteamSysNo(teamId);
            if (teamSysNo == null) {
                throw new NullPointerException("teamSysNo为空");
            }
            //根据teamId后去teamSysNo

            //获取班组code
            paranms.put("teamSysNo", teamSysNo);
            List<Map<String, Object>> addDataLists = new ArrayList<>();
            Map<String, Object> workerMap = new HashMap<>();
            workerMap.put("idCardType", "01");
            String s = null;
            try {
                s = RealNameUtil.encryptData(gmKaoQinDto.getBuilderIdcard(), RealNameConfig.ZS_API_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            workerMap.put("idCardNumber", s);
            String direction = "01";
            if ("304".equals(gmKaoQinDto.getCheckType())) {
                direction = "02";
            }
            workerMap.put("direction", direction);
            workerMap.put("date", gmKaoQinDto.getAtteTime());
            addDataLists.add(workerMap);
            paranms.put("dataList", addDataLists);
            String data = getBody(paranms, RealNameConfig.ZS_ATTENDANCE_URL);
            sendUrl(data, RealNameConfig.ZS_BASE_PATH);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     *查询考勤信息
     */
    @Override
    public void workercontract(String projectCode,Integer pageIndex,Integer pageSize,String teamSysNo,String date) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", pageIndex);
        paranms.put("pageSize", pageSize);
        paranms.put("projectCode", projectCode);
        paranms.put("teamSysNo", teamSysNo);
        paranms.put("date", date);

        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_CONTRACT_URL);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    /**
     *查询考勤信息
     */
    @Override
    public void querysynWorkerAttendance(String projectCode,Integer pageIndex,Integer pageSize,String date) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", pageIndex);
        paranms.put("pageSize", pageSize);
        paranms.put("projectCode", projectCode);
        paranms.put("date", date);
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_CONTRACT_URL);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    /**
     * 获取考勤信息
     *
     * @param projectId 项目id
     * @param kaoqinId  考勤id
     * @return 考勤对象
     */
    private GmKaoQinDto queryZsKaoQinInfo(Integer projectId, Integer kaoqinId) {
        GmKaoQinDto gmKaoQinDto;
        String sql = "SELECT ui.u_sfz AS builderIdcard, date_format(kq.`k_time`,'%Y-%m-%d %H:%i:%S')  AS atteTime ," +
                "  CASE WHEN kq.`k_state`=1 or  kq.`k_state`=3 or kq.`k_state`=5  THEN '303' ELSE '304' " +
                " END AS checkType , ui.id as  userId FROM kaoqin" + projectId + "  kq " +
                "    LEFT JOIN user_info ui ON kq.`user_id` = ui.`id`  " +
                "   where   kq.id =" + kaoqinId;
        try {
            gmKaoQinDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(), GmKaoQinDto.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(String.format("通过项目ID:%s, 考勤ID:%s查询结果失败", projectId, kaoqinId));
        }
        return gmKaoQinDto;
    }

    @Override
    public void synBuildPo(Integer projectId, Integer userId, String type) {

    }

    /***
     * 上传项目人员进退场
     * @param projectId
     * @param userId
     * @param type
     * @param teamId
     */
    @Override
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type, Integer teamId) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
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
                workerMap.put("idCardType", "01");
                String s = null;
                try {
                    s = RealNameUtil.encryptData(gmUserInfo.getU_sfz(), RealNameConfig.ZS_API_KEY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                workerMap.put("idCardNumber", s);
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
                /*String data = JSONObject.fromObject(paranms).toString();*/
                String data = getBody(paranms, RealNameConfig.ZS_JOINOROUT_METHOD);
                sendUrl(data,RealNameConfig.ZS_BASE_PATH);
            }
        } else {
            throw new BusinessException(String.format("通过用户ID:%s,班组id查询不到用户信息!", teamId, userId));
        }

    }

    @Override
    public void synAllCompanyInfo() {

    }

    /**
     * 根据项目id上传所有的项目相关的信息
     */
    @Override
    public void synAllInfoByProjectId(Integer projectId) {
        try {
            List<TeamAndUserInfo> teamAndUserInfoList = gmProjectDao.getTeamAndUserInfo(projectId);
            //记录所有班组id
            Map<String, String> teamMap = new LinkedHashMap<>();
            //记录所有用户
            Map<String, String> userMap = new LinkedHashMap<>();
            for (TeamAndUserInfo eamAndUserInfo : teamAndUserInfoList) {
                teamMap.put(eamAndUserInfo.getTeamId().toString(), eamAndUserInfo.getPoName());
                userMap.put(eamAndUserInfo.getUserId().toString(), eamAndUserInfo.getUserId().toString());
            }
            //上传劳动合同
            uploadContract(projectId, "511025198803190640");
            //上传工人工资
            ploadPayroll( projectId, 702,"512928195607151631","6217007200044971425",  "100861");
            //上传培训项目
            uploadTrainInfo( projectId, "512928195607151631");
        } catch (Exception e) {
            log.error("同步中山数据失败异常{}", e);
        }
    }

    /**
     * 上传劳动合同
     *
     * @param projectId    项目id
     * @param idCardNumber 身份证号
     */
    public void uploadContract(Integer projectId, String idCardNumber) throws UnsupportedEncodingException {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
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
        workerMap.put("idCardType", "01");
        String ss = null;
        try {
            ss = RealNameUtil.encryptData(idCardNumber, RealNameConfig.ZS_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        workerMap.put("idCardNumber",ss);
        workerMap.put("startDate", "2018-01-01");
        workerMap.put("endDate", "2018-12-31");
        workerMap.put("contractPeriodType", 0);
        List<Map<String, Object>> attachments = new ArrayList<>();
        Map<String, Object> attachmentsMap = new HashMap<>();
        attachmentsMap.put("name", "工人合同附件");
        attachmentsMap.put("data", bsseCode);
        attachments.add(attachmentsMap);
        workerMap.put("attachments", attachments);
        contractList.add(workerMap);
        paranms.put("contractList", contractList);
        String data = getBody(paranms, RealNameConfig.ZS_WORKERCONTRACT_URL);
        sendUrl(data,RealNameConfig.ZS_BASE_PATH);
    }


    /**
     * 上传工资单信息
     *
     * @param projectId 项目id
     * @paramb
     */
    public void ploadPayroll(Integer projectId, Integer teamId, String idCardNumber, String bankCardNumber, String thirdPayRollCode) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
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
        paranms.put("teamSysNo", teamId);
        paranms.put("payMonth", "2018-12");
        List<Map<String, Object>> attachments = new ArrayList<>();
        Map<String, Object> attachmentsMap = new HashMap<>();
        attachmentsMap.put("name", "工资单附件");
        attachmentsMap.put("data", "data:img/jpg;base64,"+bsseCode);
        attachments.add(attachmentsMap);
        paranms.put("attachments", attachments);
        List<Map<String, Object>> detailList = new ArrayList<>();
        Map<String, Object> detailMap = new HashMap<>();
        String ss = null;
        try {
            ss = RealNameUtil.encryptData(idCardNumber, RealNameConfig.ZS_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        detailMap.put("idCardNumber", ss);
        detailMap.put("idCardType", "01");
        detailMap.put("workHours", 160);
        detailMap.put("days", 20);
        detailMap.put("payRollBankCode", "105");
        String sss = null;
        try {
            sss = RealNameUtil.encryptData(bankCardNumber, RealNameConfig.ZS_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        detailMap.put("payRollBankCardNumber", sss);
        detailMap.put("payRollBankName", "建设银行");
        String s1 = null;
        try {
            s1 = RealNameUtil.encryptData(bankCardNumber, RealNameConfig.ZS_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        detailMap.put("payBankCardNumber", s1);
        detailMap.put("payBankCode", "105");
        detailMap.put("payBankName", "建设银行");
        detailMap.put("totalPayAmount", new BigDecimal(5000.00));
        detailMap.put("actualAmount", new BigDecimal(4900.00));
        detailMap.put("isBackPay", 0);
        detailMap.put("thirdPayRollCode", thirdPayRollCode);
        detailMap.put("balanceDate", "2018-12-30");
        detailList.add(detailMap);
        paranms.put("detailList", detailList);
        String data = getBody(paranms, RealNameConfig.ZS_WORKERWAGE_URL);
        sendUrl(data,RealNameConfig.ZS_BASE_PATH);
    }




    /**
     * 上传培训课程信息
     *
     * @param projectId 项目id
     * @paramb
     */
    public void uploadTrainInfo(Integer projectId, String idCardNumber) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        Map<String, Object> paranms = new HashMap<>();
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("trainingDate", "2018-12-22");
        paranms.put("trainingName", "保利文玥花园项目工程技能培训");
        paranms.put("trainingDuration", 8);
        paranms.put("trainingTypeCode", 003004);
        List<Map<String, Object>> workersList = new ArrayList<>();
        Map<String, Object> workersMap = new HashMap<>();
        workersMap.put("idCardType", "01");
        workersMap.put("idCardNumber", idCardNumber);
        workersMap.put("isPass", 1);
        workersList.add(workersMap);
        paranms.put("workers", workersList);
        String data = getBody(paranms, RealNameConfig.ZS_PROJECTCONTRINNNG_URL);
        sendUrl(data,RealNameConfig.ZS_BASE_PATH);
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
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_COMPANYINFO_METHOD);

        Object object = sendUrl(body, RealNameConfig.ZS_BASE_PATH);

        //成功或者失败接口
    }

    @Override
    public void corpQuery(String corpCode) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", 0);
        paranms.put("pageSize", 10);
        paranms.put("corpCode", corpCode);
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_CORP_METHOD);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    /**
     * 上传参建单位
     *
     * @param projectId 项目id
     */
    public void uploadParticipateInfo(Integer projectId) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("corpCode", gmProjectInfo.getCorpCode());
        paranms.put("corpName", gmProjectInfo.getCorpName());
        paranms.put("corpType", "008");
        //paranms.put("entryTime", gmProjectInfo.getCorpIntime());
        String body = getBody(paranms, RealNameConfig.ZS_PROJECTSUBCONTRACTOR_METHOD);
        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    /**
     * @Description: 上传项目信息
     * @Author: liaosh
     * @Date: 2019/1/12 0012
     */
    @Override
    public void synWorkinfo(Integer projectId) {
        Project project = projectDao.getProjectById(projectId);
        List<KtpProjectInfoEntity> entityList = gmProjectDao.getProjectInfo(projectId);
        Map<String, Object> paranms = new LinkedHashMap<>();
        for (KtpProjectInfoEntity entity : entityList) {
            paranms.put("contractorCorpCode", entity.getContractor_corp_code());
            paranms.put("contractorCorpName", entity.getContractor_corp_name());
            //paranms.put("buildCorpName", entity.getBuild_corp_name());
           // paranms.put("buildCorpCode", entity.getBuild_corp_code());
            JSONArray bookCotentjson = JSONArray.fromObject(entity.getBuilderLicenses());
            paranms.put("builderLicenses", bookCotentjson);
            paranms.put("areaCode", "4420");//entity.getArea_code());
        }
        paranms.put("name", project.getPName());
        paranms.put("category", "01");
        paranms.put("prjStatus", "003");
        // paranms.put("thirdPartyProjectCode", String.valueOf(projectId));
        // String url = getUrlString(paranms, RealNameConfig.GM_PROJECTINFO_URL);
        String body = getBody(paranms, RealNameConfig.ZS_PROJECTINFO_METHOD);
        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
        //失败不处理
        JSONObject object = sendUrl(body, RealNameConfig.ZS_BASE_PATH);
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            return;
        }
        //记录上传得中山得项目
        GmProjectInfoEntity gmProjectInfoEntity = new GmProjectInfoEntity();
        //String resData = String.valueOf(object.get("result_data"));
        gmProjectInfoEntity.setCorp_code(paranms.get("buildCorpCode").toString());
        gmProjectInfoEntity.setCorp_name(paranms.get("buildCorpName").toString());
        ///gmProjectInfoEntity.setProject_code(resData);
        gmProjectInfoEntity.setProject_id(projectId);
        gmProjectInfoEntity.setRegion_code("zs");
        gmProjectInfoEntity.setCorp_intime(project.getPIntime());
        gmProjectDao.saveOrUpdateGmProject(gmProjectInfoEntity);
    }

    /**
     * @Description: 上传项目信息
     * @Author: liaosh
     * @Date: 2019/1/12 0012
     */
    @Override
    public void sgsynWorkinfo(Integer projectId) {
        Project project = projectDao.getProjectById(projectId);
        List<KtpProjectInfoEntity> entityList = gmProjectDao.getProjectInfo(projectId);
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
        Map<String, Object> paranms = new LinkedHashMap<>();
        for (KtpProjectInfoEntity entity : entityList) {
            paranms.put("projectCode", gmProjectInfo.getProjectCode());
            //paranms.put("buildCorpName", entity.getBuild_corp_name());
            //paranms.put("buildCorpCode", entity.getBuild_corp_code());
            JSONArray bookCotentjson = JSONArray.fromObject(entity.getBuilderLicenses());
            paranms.put("builderLicenses", bookCotentjson);
            paranms.put("areaCode", "4420");
        }
        paranms.put("name", project.getPName());
        paranms.put("category", "01");
        paranms.put("prjStatus", "003");
        // paranms.put("thirdPartyProjectCode", String.valueOf(projectId));
        // String url = getUrlString(paranms, RealNameConfig.GM_PROJECTINFO_URL);
        String body = getBody(paranms, RealNameConfig.ZS_PROJECTUPDATE_METHOD);
        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
        //失败不处理
        /*JSONObject object = sendUrl(body, RealNameConfig.ZS_BASE_PATH);
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            return;
        }*/
    }

    @Override
    public void projectquery(String contractorCorpCode) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", 0);
        paranms.put("pageSize", 10);
        paranms.put("contractorCorpCode", contractorCorpCode);
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_PROJECTQUERY_METHOD);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    @Override
    public void poquery(String projectCode,Integer pageIndex,Integer pageSize) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", pageIndex);
        paranms.put("pageSize", pageSize);
        paranms.put("projectCode", projectCode);
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_TEAMQUERY_METHOD);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    @Override
    public void queryuser(String projectCode) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", 0);
        paranms.put("pageSize", 10);
        paranms.put("projectCode", projectCode);
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_PROJECTWORKERQUERY_METHOD);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    @Override
    public void querycanjian(String projectCode) {
        Map<String, Object> paranms = new LinkedHashMap<>();
        paranms.put("pageIndex", 0);
        paranms.put("pageSize", 10);
        paranms.put("projectCode", projectCode);
        //获取生成发送body
        String body = getBody(paranms, RealNameConfig.ZS_CONTRACTOR_METHOD);

        sendUrl(body, RealNameConfig.ZS_BASE_PATH);
    }

    @Override
    public void uploadTeamAll(Integer projectId) {
        List<ProOrgan> lists = proOrganDAO.findByProId(projectId);
        for(ProOrgan list:lists){
            if(list.getPoState()==2){
                uploadTeamInfo(list.getProId(),list.getId(),list.getPoName(),"POSAVE");
            }
            //System.out.println(list.getPoName());
        }

    }

    @Override
    public void uploadTeamInfo(Integer projectId, Integer teamId, String teamName, String type) {
        Map<String, Object> paranms = new HashMap<>();
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
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
                url = RealNameConfig.ZS_TEAMINFO_METHOD;
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
        String body = getBody(paranms, url);
        JSONObject object = sendUrl(body, RealNameConfig.ZS_BASE_PATH);

       /* if("POSAVE".equals(type)){
            String team_sysNo = String.valueOf(object.get("requestSerialCode"));
            saveOrUpdateKtpPoOut(teamId,team_sysNo,"zs");
        }*/

       /* //新增班组时记录班组code
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
        }*/
    }
    /**
     * 保存班组code
     */
    public void saveOrUpdateKtpPoOut(Integer poId, String team_sysNo,String code) {
        KtpPoOutEntity entity = new KtpPoOutEntity();
        entity.setPoId(poId);
        entity.setTeamSysNo(team_sysNo);
        entity.setRegionCode(code);
        ktpPoOutDao.saveOrUpdateKtpPoOut(entity);
    }

    /**
     *批量上传人员
     */
    @Override
    public void uploadProjectUserAll(Integer projectId) {
        List<ProOrgan> lists = proOrganDAO.queryOrganProjectId(projectId,2);

        for(ProOrgan list:lists){
            List<ProOrganPer> proOrganPers = proOrganPerDAO.findByPoId(list.getId());
            for(ProOrganPer proOrganPer:proOrganPers){
                try {
                    uploadRosterInfo(list.getProId(),list.getId(),proOrganPer.getUserId());
                }catch (Exception e){
                    log.info("班组id："+list.getId()+"，工人id："+proOrganPer.getUserId()+"上传失败");
                }
            }
        }

    }

    @Override
    public void uploadRosterInfo(Integer projectId, Integer teamId, Integer uerId) {
        GmProjectInfo gmProjectInfo = gmProjectDao.getGmProjectInfo(projectId, "zs");
        if (gmProjectInfo == null) {
            throw new BusinessException(String.format("上传项目人员信息,通过项目ID:%s,查询不到项目信息!", projectId));
        }
        Map<String, Object> paranms = new HashMap<>();
        /*if (gmProjectInfo.getTeamCode() == null) {
            throw new BusinessException(String.format("通过项目ID:%s,查询不到班组信息!", projectId));
        }*/
        JSONObject teamCode = JSONObject.fromObject(gmProjectInfo.getTeamCode());
        paranms.put("projectCode", gmProjectInfo.getProjectCode());
        paranms.put("corpCode", gmProjectInfo.getCorpCode());
        paranms.put("corpName", gmProjectInfo.getCorpName());

        ProOrgan proOrgan = gmProjectDao.getTeamname(teamId);
        //是否是管理
        Integer workRole = 10;
        if (proOrgan != null && !StringUtil.isEmpty(proOrgan.getPoName())) {
            paranms.put("teamName", proOrgan.getPoName());
            if(proOrgan.getPoState()==2){
                workRole = 20;
            }
        }
        PoInfo poInfo = gmProjectDao.getTeamSysNo(teamId,"zs");
        if (poInfo != null && !StringUtil.isEmpty(poInfo.getTeamSysNo())) {
            paranms.put("teamSysNo", poInfo.getTeamSysNo());
        }

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
            workerMap.put("idCardType",  "01");
            try {
                workerMap.put("idCardNumber", RealNameUtil.encryptData(gmUserInfo.getU_sfz(), RealNameConfig.ZS_API_KEY));
            } catch (Exception e) {
                e.printStackTrace();
            }
            workerMap.put("workType", getworkType(gmUserInfo.getW_gzid()));
            workerMap.put("workRole", workRole);
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
                    //Base64Util.Image2Base64(headImage, 0.2F);
                    Base64Util.compressBase64(headImage, ImgRatioEnum.ZS_PHOTO);
                } catch (Exception e) {
                    headImage = "https://images.ktpis.com/pic1.png";
                }
            }
            String head = "data:image/png;base64,";
            /*if(headImage.indexOf(".jpg")!=-1 || headImage.indexOf(".img")!=-1){
                head = "data:img/jpg;base64,";
            }else{
                head = "data:image/png;base64,";
            }*/
            headImage = Base64Util.compressBase64(headImage, ImgRatioEnum.ZS_PHOTO);
            if("noPic".equals(headImage)){
                headImage = Base64Util.compressBase64("https://images.ktpis.com/pic1.png", ImgRatioEnum.ZS_PHOTO);
            }
            headImage = head+headImage;

            workerMap.put("headImage", headImage);
            //写定群众
            workerMap.put("politicsType", 13 + "");
            if (gmUserInfo.getU_name() != null && !"".equals(gmUserInfo.getU_name())) {
                workerMap.put("cellPhone", gmUserInfo.getU_name());
            } else {
                workerMap.put("cellPhone", "13300000000");
            }
            workerMap.put("cultureLevelType","01"); //getCultureLevelType(gmUserInfo.getW_edu())
            if (gmUserInfo.getW_zs() != null && !"".equals(gmUserInfo.getW_zs())) {
                workerMap.put("grantOrg", gmUserInfo.getW_zs());
            } else {
                workerMap.put("grantOrg", "无");
            }
            workerList.add(workerMap);
        }
        paranms.put("workerList", workerList);
        String body = getBody(paranms, RealNameConfig.ZS_PROJECTWORKERADD_METHOD);
        sendUrl(body, RealNameConfig.ZS_BASE_PATH);

    }

    @Override
    public String getpSent() {
        return "zs";
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }


}
