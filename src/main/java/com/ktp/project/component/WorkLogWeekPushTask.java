package com.ktp.project.component;

import com.ktp.project.service.WorkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author: tangbin
 * @Date: 2018-12-7 下午 13.10
 */

@Component
public class WorkLogWeekPushTask {

    @Autowired
    private WorkLogService workLogService;


    //凌晨遍历更新工作日志
    @Scheduled(cron = "0 0 0 * * ?")
    public void task() {
        workLogService.updateYesterdayAllworkLog();

    }

}
