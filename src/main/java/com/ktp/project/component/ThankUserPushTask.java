package com.ktp.project.component;

import com.ktp.project.service.UserService;
import com.ktp.project.util.HuanXinRequestUtils;
import com.ktp.project.util.JPushClientUtil;
import com.zm.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.Timestamp;
import java.util.*;

/**
 * @Author: lsh
 * @Date: 2018年12月7日15:13:38
 */
@Component
public class ThankUserPushTask {
    private static final Logger log = LoggerFactory.getLogger(ThankUserPushTask.class);
    @Autowired
    private UserService userService;
    @Value("${ktp.team.id}")
    private String teamId;
    @Value("${ktp.team.name}")
    private String teamName;
    /**
     *感恩周年推送
     */

    @Scheduled(cron = "0 0 10 * * ?")//每天早上10点推送
    //@Scheduled(cron = "0 1/1 * * * ?")//每隔一分钟推送
    public void task() {
        List<Object> integers =new ArrayList<>();
        Map<String, Object> extMap = new HashMap<>();
        extMap.put("type","ktp_thanks_push");
        extMap.put("myUserName",teamName);
        Long year = 0l;
        List<UserInfo> infos = userService.gitthankUser();
        for(UserInfo info:infos){
            //周年
            year = new Date().getTime() - info.getU_intime().getTime();
            year = ((year/1000 / 60 / 60 / 24)+1)/365;
            integers.add(info.getUser_id());
            extMap.put("ktp_thanks_year",year);
            extMap.put("ktp_thanks_username",info.getU_realname());
            //单个推送
            /*HuanXinRequestUtils.sendMessage(48864+"",integers,extMap,"亲爱的"+info.getU_realname()+"，截止到今天，" +
                    "您已经使用开太平信息管理平台"+year+"周年。非常感谢您的信任和支持！我们会一如既往地努力做好产品和运维服务。");*/
            HuanXinRequestUtils.sendMessage(teamId,integers,extMap,"感谢函");
            integers.clear();
        }
    }
}
