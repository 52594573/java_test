package com.ktp.project.logic.schedule;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.GZProject.GzKaoQinDto;
import com.ktp.project.entity.AuthRealNameLog;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.AuthRealNameLogService;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.service.realName.impl.ZhzhAuthRealNameService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.HttpClientUtils;
import com.ktp.project.util.RealNameUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@SuppressWarnings("all")
public class AuthRealNameTask {

    private static Logger log = LoggerFactory.getLogger(AuthRealNameTask.class);


    @Resource(name="jmpAuthRealNameService")
    private AuthRealNameApi jmpService;
    @Resource(name="gzAuthRealNameService")
    private AuthRealNameApi  gzService;
    @Autowired
    private QueryChannelDao queryChannelDao;
    @Autowired
    private AuthRealNameLogService logService;
    @Autowired
    private ZhzhAuthRealNameService zhService;


    /**
     * 同步进场人员信息到实名系统
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void synFailKaoQin() {
        log.info("同步进场人员信息到实名系统, 开始时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
        List<AuthRealNameLog> logs = getFailAuthRealNameLog(60);
//        logs.add(logService.findOne(35152));
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(60);
        if (!CollectionUtils.isEmpty(logs)){
            for (AuthRealNameLog log : logs) {
                String reqBody = log.getReqBody();
                if (StringUtils.isNotBlank(reqBody)){
                    GzKaoQinDto dto = JSONObject.parseObject(reqBody, GzKaoQinDto.class);
                    dto.setTimestamp(DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_NORMAL));
                    try {
                        String data = RealNameUtil.GZencodeRequestBody(dto, projectEnum.getKey());
                        sendDate(log.getReqUrl(), data);
                        log.setIsSuccess(1);
                        log.setResMsg("成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.setIsSuccess(0);
                        log.setResMsg(e.getMessage());
                    } finally {
                        logService.saveOrUpdate(log);
                    }
                }
            }
        }
        log.info("同步进场人员信息到实名系统, 结束时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
    }

    private List<AuthRealNameLog> getFailAuthRealNameLog(Integer projectId){
        String sql = "SELECT id, project_id AS projectId, user_id AS userId, req_url AS reqUrl, req_body AS reqBody  " +
                " , is_success AS isSuccess, res_msg AS resMsg, create_time AS createTime, remark  " +
                "FROM auth_real_name_log  " +
                "WHERE (project_id = ?  " +
                " AND is_success = 0  " +
                " AND create_time > DATE_FORMAT(NOW(), '%Y-%m-%d')  " +
                " AND remark = '同步考勤员工信息' AND res_msg = '同步到广州系统失败: 考勤时间超前' ) ";
        
        return queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(projectId), AuthRealNameLog.class);
    }

    //发送处理数据
    public void sendDate(String url ,String datastr){
        try {
            if(url == null || "".equals(url)){
                throw new Exception("发送类型异常");
            }
            String ss = url +"?"+datastr;
            String result = HttpClientUtils.get(ss, new HashMap<>());
            RealNameUtil.GZproResIfSussess(result);
            log.info("同步到广州实名系统成功! url: {}, 参数: {}, 返回结果: {}", url, datastr, result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 同步进场人员信息到实名系统
     */
//    @Scheduled(cron = "0 55 23 * * ?")
//    public void synZhFailKaoQin() {
//        log.info("同步进场人员信息到实名系统, 开始时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
//        List<AuthRealNameLog> logs = getZhFailAuthRealNameLog(3862);
//        for (AuthRealNameLog realNameLog : logs) {
//            Integer projectId = realNameLog.getProjectId();
//            Integer userId = realNameLog.getUserId();
//            if (projectId != null && userId != null){
//                zhService.synWorkerAttendance(projectId, userId);
//            }
//        }
//        log.info("同步进场人员信息到实名系统, 结束时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
//    }
//
//    private List<AuthRealNameLog> getZhFailAuthRealNameLog(Integer projectId){
//        String sql = "SELECT id, project_id AS projectId, user_id AS userId, req_url AS reqUrl, req_body AS reqBody  " +
//                " , is_success AS isSuccess, res_msg AS resMsg, create_time AS createTime, remark  " +
//                "FROM auth_real_name_log  " +
//                "WHERE (project_id = ?  " +
//                " AND is_success = 0  " +
//                " AND create_time > DATE_FORMAT(NOW(), '%Y-%m-%d')  " +
//                " AND remark = '同步考勤员工信息' AND res_msg = 'Socket is closed' ) ";
//
//        return queryChannelDao.selectManyAndTransformer(sql, Lists.newArrayList(projectId), AuthRealNameLog.class);
//    }
}
