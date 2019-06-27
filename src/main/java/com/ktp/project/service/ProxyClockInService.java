package com.ktp.project.service;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.dao.*;
import com.ktp.project.dto.clockin.ProxyClockInDetailsDto;
import com.ktp.project.dto.clockin.UserInfoDto;
import com.ktp.project.entity.ClockInFailure;
import com.ktp.project.entity.ProxyClockIn;
import com.ktp.project.entity.UserJf;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.NumberUtil;
import com.ktp.project.util.ObjectUtil;
import com.ktp.project.util.redis.RedisClientTemplate;
import com.zm.entity.KeyContent;
import com.zm.entity.KeyContentDAO;
import com.zm.entity.ProOrgan;
import com.zm.entity.Project;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代理打卡
 * Created by LinHon 2018/12/4
 */
@Service
@Transactional
public class ProxyClockInService {

    private static final Logger log = LoggerFactory.getLogger(ProxyClockInService.class);

    private String kaoqinSql = "insert into kaoqin%d (k_state , k_time , pro_id , user_id , k_xsd , k_lbsx , k_lbsy , k_pic) values(%s)";

    @Autowired
    private ModifyChannelDao modifyChannelDao;

    @Autowired
    private OrganDao organDao;

    @Autowired
    private ProxyClockInDao proxyClockInDao;

    @Autowired
    private UserJfDao userJfDao;

    @Autowired
    private KeyContentDAO keyContentDAO;

    @Autowired
    private KaoqinDao kaoqinDao;

    @Value("${ktpweb.domain}")
    private String KTPWEB_DOMAIN;


    /**
     * 获取被代理工人列表
     *
     * @param projectId
     * @param userId
     * @return
     */
    public List getUserList(int projectId, int userId) {
        List result = Lists.newArrayList();
        ProOrgan proOrgan = organDao.queryMyProOrgan(projectId, userId);
        if (proOrgan == null) {
            return result;
        }
        if (proOrgan.getPoState() == 1) {
            for (ProOrgan item : organDao.queryManyByProjectId(projectId, 2)) {
                builderUserList(result, item, userId);
            }
        } else {
            builderUserList(result, proOrgan, userId);
        }
        return result;
    }

    private void builderUserList(List result, ProOrgan proOrgan, int userId) {
        List users = organDao.queryUsersOfProxyClockIn(proOrgan.getId(), userId);
        if (users.size() > 0) {
            Map<String, Object> data = Maps.newHashMap();
            data.put("name", proOrgan.getPoName());
            data.put("userList", users);
            result.add(data);
        }
    }


    /**
     * 考勤打卡
     *
     * @param proxyClockIn
     */
    public synchronized Map create(ProxyClockIn proxyClockIn) {
        int kqCount = kaoqinDao.queryKaoqinCount(proxyClockIn.getUserId());//查询当天考勤数
        proxyClockIn.setCreateTime(new Timestamp(System.currentTimeMillis()));

        //添加代理记录
        if (proxyClockIn.getProxyUserId() != 0) {
            modifyChannelDao.save(proxyClockIn);
        }

        //住建项目数据推送
        boolean flag;
        synchronized (this){
            flag = EnumMap.subclassMap.containsKey(proxyClockIn.getProjectId());
        }
        log.info("项目ID: {}, 是否需要同步到实名系统: {}", proxyClockIn.getProjectId(), flag);
        if (flag){
            AuthRealNameApi authRealNameApi = EnumMap.subclassMap.get(proxyClockIn.getProjectId());
            httpClockInAndRedisPush(authRealNameApi, proxyClockIn);
        }else {
            String values = Joiner.on(",").join(proxyClockIn.getStatus(), "'" + proxyClockIn.getCreateTime() + "'", proxyClockIn.getProjectId(), proxyClockIn.getUserId()
                    , proxyClockIn.getSimilarity(), proxyClockIn.getLatitude(), proxyClockIn.getLongitude(), "'" + proxyClockIn.getImage() + "'");
            modifyChannelDao.executeUpdate(kaoqinSql, proxyClockIn.getProjectId(), values);
        }

        if (kqCount < 2) {
            //保存积分 --2018-12-28 wuyeming
            Date now = new Date();
            KeyContent keyContent = keyContentDAO.findById(182);
            String count = "+1";
            if (keyContent != null) {
                count = keyContent.getKeyValue();
            }
            UserJf userJf = new UserJf();
            userJf.setUserId(proxyClockIn.getUserId());
            userJf.setJfShu(count);
            userJf.setJfType(1);
            userJf.setInTime(now);
            userJf.setJfState(182);
            userJf.setJfYue(DateUtil.getFormatDateTime(now, "yyyy-MM"));
            userJfDao.saveOrUpdate(userJf);
        }
        return ImmutableMap.of("longitude", proxyClockIn.getLongitude(), "latitude", proxyClockIn.getLatitude(), "createTime", DateUtil.defaultFormat(proxyClockIn.getCreateTime()));
    }

    private void httpClockInAndRedisPush(AuthRealNameApi authRealNameApi, ProxyClockIn proxyClockIn) {
        JSONObject request = JSONObject.fromObject(proxyClockIn);
        request.put("createTime", DateUtil.format(proxyClockIn.getCreateTime(), DateUtil.FORMAT_DATE_TIME));
        String response = HttpClientUtils.post(KTPWEB_DOMAIN + "/clockIn/create", request.toString(), "application/json", null);
        log.info("远程调用KTPWE项目: 项目ID: [  {} ] 请求参数:[  {}  ], 返回结果:[  {}  ]", proxyClockIn.getProjectId(), request.toString(), response);
        JSONObject responseJson = JSONObject.fromObject(response);
        if (!Boolean.parseBoolean(responseJson.get("status").toString())) {
            throw new RuntimeException("http添加考勤数据失败");
        }
        try {
            int projectId = proxyClockIn.getProjectId();
            int clockInId = Integer.parseInt(responseJson.get("clockInId").toString());
            String dateTime = NumberUtil.formatDateTime(new Date());
            log.info("同步考勤数据到实名系统: 参数: [   项目ID: {}, 考勤ID: {}   ], 推送时间: {}", projectId, clockInId, dateTime);
            authRealNameApi.synWorkerAttendance(projectId, clockInId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("同步考勤数据到实名系统出现异常,项目ID: {}, 异常信息: {}",proxyClockIn.getProjectId(), e.getMessage());
        }
/*        try {
            JSONObject json = JSONObject.fromObject(ImmutableMap.of("projectId", proxyClockIn.getProjectId(), "clockInId", responseJson.get("clockInId").toString()));
            RedisClientTemplate.lpush(ObjectUtil.objectToBytes("clockInList"), ObjectUtil.objectToBytes(json.toString()));
        } catch (Exception e) {
            log.error("redis推送失败", e);
        }*/
    }


    /**
     * 保存打卡故障记录
     *
     * @param proxyClockIn
     * @param errorMsg
     */
    /*public void clockInFailure(ProxyClockIn proxyClockIn, String errorMsg) {
        ClockInFailure fail = new ClockInFailure();
        fail.setProjectId(proxyClockIn.getProjectId());
        fail.setUserId(proxyClockIn.getUserId());
        fail.setSimilarity(proxyClockIn.getSimilarity());
        fail.setLongitude(proxyClockIn.getLongitude().doubleValue());
        fail.setLatitude(proxyClockIn.getLatitude().doubleValue());
        fail.setImage(proxyClockIn.getImage());
        fail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fail.setErrorMsg(errorMsg);
        fail.setType(proxyClockIn.getStatus());
        fail.setStatus(1);
        modifyChannelDao.save(fail);
    }*/


    /**
     * 取得代理考勤用户列表
     *
     * @param projectId
     * @param userId
     * @return
     */
    public List getProxyClockInList(int projectId, int userId) {
        List result = Lists.newArrayList();
        ProOrgan proOrgan = organDao.queryMyProOrgan(projectId, userId);
        if (proOrgan == null) {
            return result;
        }
        if (proOrgan.getPoState() == 1) {
            for (ProOrgan item : organDao.queryManyByProjectId(projectId, 2)) {
                builderProxyClockInList(result, item, userId);
            }
        } else {
            builderProxyClockInList(result, proOrgan, userId);
        }
        return result;
    }

    private void builderProxyClockInList(List result, ProOrgan proOrgan, int userId) {
        List users = Lists.newArrayList();
        List<UserInfoDto> usersOfOrgan = organDao.queryUsersOfProxyClockIn(proOrgan.getId(), userId);
        List<Integer> usersOfProxyClockIn = proxyClockInDao.queryMyProxyClockInUserIds(proOrgan.getProId(), userId);
        for (UserInfoDto item : usersOfOrgan) {
            if (usersOfProxyClockIn.contains(item.getUserId())) {
                users.add(item);
            }
        }
        if (users.size() > 0) {
            Map<String, Object> data = Maps.newHashMap();
            data.put("name", proOrgan.getPoName());
            data.put("userList", users);
            result.add(data);
        }
    }


    /**
     * 获取打卡详情列表
     *
     * @return:
     */
    @Transactional
    public ProxyClockInDetailsDto getProxyClockInDetail(int proxyUserId, String proDate, int userId, int proId) {
        if (proDate == null || "".equals(proDate)) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            proDate = sdf.format(date);
        }


        try {
            proDate += " 00:00:00";
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date bDate = format.parse(proDate);
            //下一天
            Calendar cld = Calendar.getInstance();
            cld.setTime(bDate);
            cld.add(Calendar.DATE, 1);
            //获得下一天日期字符串
            Date eDate = cld.getTime();

            List<ProxyClockIn> clockIns = new ArrayList<>();
            ProxyClockInDetailsDto dto = new ProxyClockInDetailsDto();

            //查看自己打卡
            if (userId == 0) {
                clockIns = proxyClockInDao.getPoClockInList(proxyUserId, bDate, eDate, proId);
                dto.setClockIns(clockIns);
            } else {
                //代理打卡
                clockIns = proxyClockInDao.getPoClockInList(proxyUserId, bDate, eDate, userId, proId);
                if (clockIns != null && clockIns.size() != 0) {
                    //今天有在这个手机打卡，就可以就看到所有今天打卡
                    clockIns = proxyClockInDao.getPoClockInList(userId, bDate, eDate, proId);
                    dto.setClockIns(clockIns);
                } else {
                    //第一次打卡在本机，到代理打卡看不到上下打卡结果
                    clockIns = proxyClockInDao.getPoClockInList(userId, bDate, eDate, proId);
                }

            }

            //判断上下班
            if (clockIns.size() % 2 == 1) {
                dto.setOnOroff(2);
            } else {
                dto.setOnOroff(1);
            }
            dto.setServerTime(new Date());
            //获取项目范围
            Project project = proxyClockInDao.getprojectFWById(proId);
            dto.setProjectFW(project.getPLbsFw());
            dto.setpLongitude(project.getPLbsY());
            dto.setpLatitude(project.getPLbsX());
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("转化异常");
        }
        return null;
    }
}
