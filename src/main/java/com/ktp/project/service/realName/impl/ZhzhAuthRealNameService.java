package com.ktp.project.service.realName.impl;

import com.google.common.collect.Lists;
import com.ktp.project.constant.ImgRatioEnum;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.constant.RealNameEnum;
import com.ktp.project.dao.*;
import com.ktp.project.dto.AuthRealName.*;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.entity.KtpErrorLogEntity;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.ZhzhAuthRealNameApi;
import com.ktp.project.util.*;
import com.zm.entity.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 中山项目实现类
 */
@Service("zhzhAuthRealNameService")
@Transactional
public class ZhzhAuthRealNameService implements AuthRealNameApi, ZhzhAuthRealNameApi {

    private static final Logger log = LoggerFactory.getLogger(ZhzhAuthRealNameService.class);
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
    @Resource
    private Client client;
    @Autowired
    private AuthRealNameLogService logService;


    /**
     * 发appid=qg-100238&data=%7B%22corpCode%22%3A%229144078319428766XQ%22%2C%22corpName%22%3A%22%E5%BC%80%E5%B9%B3%E4%BD%8F%E5%AE%85%E5%BB%BA%E7%AD%91%E5%B7%A5%E7%A8%8B%E9%9B%86%E5%9B%A2%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%22%2C%22areaCode%22%3A%22440608%22%2C%22registerDate%22%3A%221933-05-26%22%7D&format=json&method=Corp.Upload&nonce=91bb0093-f608-4f94-8a1f-8e09b6bf5a55&timestamp=20190114124158&version=1.0&sign=05b9791645d8376f13d5680047ec93359d667115a984fc7ed59ffd35b170f765送调用的地址,并记录错误日志
     *
     * @param -参数
     * @return
     */
    public JSONObject sendUrl(String data, String url)throws Exception {
        String result = "";

            result = HttpClientUtils.post(url, data, "text/html;charset=UTF-8", new HashMap<>());



        JSONObject object = new JSONObject();
        /*try {
            result = URLDecoder.decode(result, "UTF-8");
            object = JSONObject.fromObject(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info(result);
        String code = String.valueOf(object.get("result"));
        if (!"true".equals(code)) {
            String msg = String.valueOf(object.get("message"));
            saveOrUpdateGmProject("",url,msg,0,data);
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
        entity.setRegion_code("zhzh");
        entity.setParm_str(parm_str);
        ktpErrorLogDao.saveOrUpdateGmProject(entity);
    }



    /**
     * @param paranms 上传数据
     * @Description:
     * @Author: liaosh
     * @Date: 2019/1/12 0012
     */
    public String getBody(Map<String, Object> paranms) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        //获取当前时间的年月日
        String timestamp = df.format(time);
        //加密的字段
        Map<String, Object> paranms1 = new LinkedHashMap<>();
        String data = JSONObject.fromObject(paranms).toString();
        StringBuffer str = new StringBuffer();
        StringBuffer sign_key = new StringBuffer();
        try {

        paranms1.put("api_key", RealNameConfig.ZHZH_APPID);
        paranms1.put("api_version",RealNameConfig.ZHZH_API_VERSION);
        paranms1.put("body", data);
        paranms1.put("timestamp", timestamp );

        /*签名开始*/


            for (String key : paranms1.keySet()) {
                    str.append(key).append(paranms1.get(key));
            }

        //前后带上api_key
        sign_key.append(RealNameConfig.ZHZH_APPID);
        sign_key.append(str);
        sign_key.append(RealNameConfig.ZHZH_APPID);

        /*签名结束*/
        String signature = Md5Util.encryption1(sign_key.toString());
        log.info("加密："+sign_key.toString());
        //签名之后urlencode
            //data = URLEncoder.encode(sign_key.toString(),"UTF-8")+signature;
         paranms1.put("signature", signature);

         //要urlencode
            paranms1.put("api_key", URLEncoder.encode(RealNameConfig.ZHZH_APPID,"UTF-8"));
            paranms1.put("api_version",URLEncoder.encode(RealNameConfig.ZHZH_API_VERSION ,"UTF-8"));
            paranms1.put("timestamp", URLEncoder.encode(timestamp ,"UTF-8"));
         //重新格式化str
         str = new StringBuffer();
            //拼成串
            for (String key : paranms1.keySet()) {
                if ("body".equals(key)) {
                    continue;
                }
                if ("api_key".equals(key)) {
                    str.append(key).append("=").append(paranms1.get(key));
                } else {
                    str.append("&").append(key).append("=").append(paranms1.get(key));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //paranms1.put("signature", signature);

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
                Thread.sleep(10000);
            }catch (Exception e) {
                e.printStackTrace();
            }
            log.info("工人id："+list);
            log.info("总数："+i);
        }
    }

    @Override
    public void querycanjian(String projectCode) {

    }

    @Override
    public void corpQuery(String corpCode) {

    }

    @Override
    public void uploadTeamInfo(Integer projectId, Integer teamId, String teamName, String type) {

    }

    @Override
    public void uploadTeamAll(Integer projectId) {

    }

    /**
     * 上传考勤
     * @param projectId
     * @param kaoQinId
     */
    @Override
    public void synWorkerAttendance(Integer projectId, Integer kaoQinId)  {

        ZHZHKaoQinDto zhzhKaoQinDto = queryZsKaoQinInfo(projectId, kaoQinId);
        if(zhzhKaoQinDto==null ){
            throw new BusinessException(String.format("通过项目ID:%s,查询不到项目信息!", projectId));
        }
        if(zhzhKaoQinDto.getPictureUrl()==null || "".equals(zhzhKaoQinDto.getPictureUrl())){
            throw new BusinessException(String.format("通过项目ID:%s中考勤信息：%s,考勤图片为空!", projectId,kaoQinId));
        }
        client.sendKaoqin(projectId,zhzhKaoQinDto.getKaoqinTime(),zhzhKaoQinDto.getSfz(),zhzhKaoQinDto.getPictureUrl());

    }

    /**
     * 获取考勤信息
     *
     * @param projectId 项目id
     * @param kaoqinId  考勤id
     * @return 考勤对象
     */
    private ZHZHKaoQinDto queryZsKaoQinInfo(Integer projectId, Integer kaoqinId) {
        ZHZHKaoQinDto zhzhKaoQinDto;
        String sql = "SELECT ui.u_sfz AS sfz, date_format(kq.`k_time`,'%Y%m%d%H%i%S')  AS kaoqinTime ," +
                " kq.k_pic as  pictureUrl FROM kaoqin" + projectId + "  kq " +
                "    LEFT JOIN user_info ui ON kq.`user_id` = ui.`id`  " +
                "   where   kq.id =" + kaoqinId;
        try {
            zhzhKaoQinDto = queryChannelDao.selectOneAndTransformer(sql, Lists.newArrayList(), ZHZHKaoQinDto.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(String.format("通过项目ID:%s, 考勤ID:%s查询结果失败", projectId, kaoqinId));
        }
        return zhzhKaoQinDto;
    }

    @Override
    public void synBuildPo(Integer projectId, Integer userId, String type) {

    }

    @Override
    public void synBuildPoUserJT(Integer projectId, Integer userId, String type, Integer teamId) {

    }

    @Override
    public void synAllCompanyInfo() {

    }

    @Override
    public void synAllInfoByProjectId(Integer projectId) {

    }


    /**
    *
    * @Description: 整个项目人员采集上传
    * @Author: liaosh
    * @Date: 2019/1/23 0023
    */
    @Override
    public void CaijiAll(Integer projectId) {
        List<Integer> lists = gmProjectDao.queryIdByPro(projectId);
        Integer i = 0;
        for(Integer list:lists){
            try {
                i++;
                uploadWorkerFeature(list,"");
            }catch (Exception e) {
                e.printStackTrace();
            }
            log.info("总数："+i);
        }
    }

    /**
     *采集人员信息
     * */
    public void uploadWorkerFeature(Integer userId,String tup1) {
        AuthRealNameLog authRealNameLog = logService.createBaseBean(3862, userId, "同步珠海整合采集人员");
        log.info("用户id："+userId);
        try {
            ZhzhCaiJiDto zhzhCaiJiDto = gmProjectDao.getZhCJ(userId);
            if (zhzhCaiJiDto == null) {
                throw new BusinessException(String.format("通过项目ID:%s,查询不到采集人员信息!", userId));
            }
            try {
                //空值处理
                zhzhCaiJiDto = CaiJiNull(zhzhCaiJiDto);
            }catch (Exception e){
                e.printStackTrace();
            }
            Map<String, Object> paranms = new HashMap<>();
            paranms.put("Name", zhzhCaiJiDto.getName());
            paranms.put("CerfNum",zhzhCaiJiDto.getCerfNum());//"440823199307076233");
            paranms.put("Nation",zhzhCaiJiDto.getNation());
            paranms.put("Native", zhzhCaiJiDto.getNative());
            paranms.put("Sex","1");// zhzhCaiJiDto.getSex());
            paranms.put("IdCardAddress",zhzhCaiJiDto.getIdCardAddress());
            paranms.put("Birthday", zhzhCaiJiDto.getByear()+"-"+zhzhCaiJiDto.getBmonth()+"-"+zhzhCaiJiDto.getBday());
            paranms.put("CollectPhoto",Base64Util.compressBase64("https://images.ktpis.com/images/pic/20190121181647391469859183.jpg"/*zhzhCaiJiDto.getCollectPhoto()*/, ImgRatioEnum.ZHZH_PHOTO));//Base64Util.compressBase64(zhzhCaiJiDto.getCollectPhoto(), ImgRatioEnum.ZHZH_PHOTO));
            paranms.put("IdCardPhoto", Base64Util.compressBase64(zhzhCaiJiDto.getIdCardPhoto(), ImgRatioEnum.ZHZH_PHOTO));//Base64Util.compressBase64("https://images.ktpis.com/952619_201810161750118901793.jpg", ImgRatioEnum.ZHZH_PHOTO));
            paranms.put("IssuingAuthority", zhzhCaiJiDto.getIssuingAuthority());
            paranms.put("ValidityPeriodBe", zhzhCaiJiDto.getStime()+"-"+zhzhCaiJiDto.getEtime());
            String urldata = getBody(paranms);
            String data = JSONObject.fromObject(paranms).toString();
            urldata = RealNameConfig.ZHZH_BASE_PATH203+RealNameConfig.ZHZH_UPLOADWORKERFEATURE_URL + "?" +urldata;
            //urldata = "http://157.122.146.230:9203"+RealNameConfig.ZHZH_UPLOADWORKERFEATURE_URL + "?" +urldata;

            sendUrl(data,urldata);
        } catch (Exception e) {
            authRealNameLog.setIsSuccess(0);
            authRealNameLog.setResMsg(e.getMessage());
            e.printStackTrace();
        } finally {
            authRealNameLog.setReqUrl(RealNameConfig.ZHZH_BASE_PATH203);
            authRealNameLog.setReqBody("");
            logService.saveOrUpdate(authRealNameLog);
        }
    }

    @Override
    public void synCompanyInfo(String corpCode, String corpName, String areaCode, String registerDate) {

    }

    @Override
    public void uploadParticipateInfo(Integer projectId) {

    }

    @Override
    public void synWorkinfo(Integer projectId) {

    }

    @Override
    public void sgsynWorkinfo(Integer projectId) {

    }

    @Override
    public void projectquery(String contractorCorpCode) {

    }

    @Override
    public void poquery(String projectCode, Integer pageIndex, Integer pageSize) {

    }

    @Override
    public void queryuser(String projectCode) {

    }

    @Override
    public void workercontract(String projectCode, Integer pageIndex, Integer pageSize, String teamSysNo, String date) {

    }

    //空值处理
    public ZhzhCaiJiDto CaiJiNull(ZhzhCaiJiDto zhzhCaiJiDto) throws Exception {
        if(StringUtil.isEmpty(zhzhCaiJiDto.getNation())){
            zhzhCaiJiDto.setNation("汉族");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getNative())){
            zhzhCaiJiDto.setNative("无");
        }
        //身份证地址
        if(StringUtil.isEmpty(zhzhCaiJiDto.getIdCardAddress())){
            zhzhCaiJiDto.setIdCardAddress("无");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getByear())){
            zhzhCaiJiDto.setByear("2000");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getBmonth())){
            zhzhCaiJiDto.setBmonth("01");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getBday())){
            zhzhCaiJiDto.setBday("01");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getCollectPhoto())){
            zhzhCaiJiDto.setCollectPhoto("https://images.ktpis.com/images/pic/20190121181647391469859183.jpg");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getIdCardPhoto())){
            throw new  Exception("身份证照片为空");
        }
        //发证机关
        if(StringUtil.isEmpty(zhzhCaiJiDto.getIssuingAuthority())){
            zhzhCaiJiDto.setIssuingAuthority("无");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getStime())){
            zhzhCaiJiDto.setStime("2018.01.01");
        }
        if(StringUtil.isEmpty(zhzhCaiJiDto.getEtime())){
            zhzhCaiJiDto.setEtime("2020.01.01");
        }
        return zhzhCaiJiDto;
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

    }


    @Override
    public String getpSent() {
        return "zhzh";
    }

    @Override
    public void authRealNameByType(Integer projectId, Integer unknownId, RealNameEnum realNameEnum) {

    }


}
