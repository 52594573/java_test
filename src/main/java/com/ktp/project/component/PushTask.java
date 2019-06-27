package com.ktp.project.component;

import com.ktp.project.dto.WorkingHoursDto;
import com.ktp.project.service.ProjectService;
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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: wuyeming
 * @Date: 2018-11-7 下午 17:21
 */
@Component
public class PushTask {
    private static final Logger log = LoggerFactory.getLogger(PushTask.class);
    @Autowired
    private UserService userService;
    @Value("${jpush.env}")
    private String env;

    @Autowired
    private ProjectService projectService;

    @Scheduled(cron = "0 0 9 ? * MON")//星期一上午9点推送
    /*@Scheduled(cron = "0 1/1 * * * ?")//每隔一分钟推送*/
    //周一请关注 ，考勤情况
    public void task() {
        try {
            List<UserInfo> list = userService.getAllUser();
            Map<String, String> map = new HashMap<>();
            map.put("messageType", "attendance");//考勤详情页
            map.put("notify", "1");
            map.put("pushType", "attendance");
            List<String> androidList = new ArrayList<>();
            List<String> iosList = new ArrayList<>();
            String title = "考勤提醒";
            String content = "您好，新的一周开始了，请注意关注上周考勤情况！";
            for (UserInfo userInfo : list) {
                Integer userId = userInfo.getId();
                String OS = userInfo.getLast_device();
                if (OS != null) {
                    if (OS.equals("A")) {
                        androidList.add("KTP_" + env + "_" + OS + "_" + userId);
                        if (androidList.size() >= 20) {
                            JPushClientUtil.getInstance().pushDevice(androidList, OS, title, content, map, "1");
                            androidList.clear();
                        }
                    } else if (OS.equals("I")) {
                        iosList.add("KTP_" + env + "_" + OS + "_" + userId);
                        if (iosList.size() >= 20) {
                            JPushClientUtil.getInstance().pushDevice(iosList, OS, title, content, map, "1");
                            iosList.clear();
                        }
                    }
                }
            }
            if (androidList.size() > 0) {
                JPushClientUtil.getInstance().pushDevice(androidList, "A", title, content, map, "1");
                androidList.clear();
            }
            if (iosList.size() > 0) {
                JPushClientUtil.getInstance().pushDevice(iosList, "I", title, content, map, "1");
                iosList.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 9 ? * MON")//星期一上午9点推送
    /*@Scheduled(cron = "0 0/5 * * * ?")//每隔一分钟推送*/
    //推送工人上一周的工作时长
    public void worktask() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        Date lastMonday = calendar1.getTime();//上周一的时间
        Date lastSunday = calendar2.getTime();//上周日的时间
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");//添加不同的参数，显示不同格式的日期
        //从表working_hours中查询创建时间为当天的数据（即为工人的工作时间）
        List<WorkingHoursDto> list = projectService.getWorkingHours();
        for (WorkingHoursDto workingHoursDto : list) {
            Integer id = workingHoursDto.getUserId();
            int userId = userService.getUser(workingHoursDto.getProId(), workingHoursDto.getUserId());
            List<Object> userIds = new ArrayList<>();
            Map<String, Object> extMap = new HashMap<>();
            String projectName = projectService.getProjectName(workingHoursDto.getProId());
            userIds.add(workingHoursDto.getUserId());
            /*userIds.add(47732);
            userIds.add(48864);
            userIds.add(47714);
            userIds.add(47327);*/
            extMap.put("workingHoursDto", workingHoursDto);
            extMap.put("type", "weeklywork");
            extMap.put("lastMonday", f2.format(lastMonday));
            extMap.put("lastSunday", f2.format(lastSunday));
            extMap.put("myUserName", projectName);
            extMap.put("projectId", workingHoursDto.getProId());
            if (userId == 0) {
                HuanXinRequestUtils.sendMessage("project-" + workingHoursDto.getProId(), userIds, extMap, HuanXinRequestUtils.DEFAULT_MSG, true);
            }
            if (userId != 0) {
                HuanXinRequestUtils.sendMessage("project-" + workingHoursDto.getProId(), userIds, extMap, HuanXinRequestUtils.DEFAULT_MSG);
            }
        }
    }
}
