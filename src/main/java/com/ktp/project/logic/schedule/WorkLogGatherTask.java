package com.ktp.project.logic.schedule;

import com.ktp.project.entity.WorkLogGather;
import com.ktp.project.service.WorkLogGatherService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.NumberUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class WorkLogGatherTask{

    private static Logger log = LoggerFactory.getLogger(WorkLogGatherTask.class);

    @Autowired
    private WorkLogGatherService workLogGatherService;


    /**
     * 每月1号凌晨自动生成工作记录统计
     * 如果失败,每隔1个小时重试一次,最多重试三次
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void createWorkLogGatherTask() {
        log.info("定时任务同步生成工作记录统计开始, 开始时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
        this.createWorkLogGather(4);
        log.info("定时任务同步生成工作记录统计结束, 结束时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
    }

    private void createWorkLogGather(int flag) {
        try {
            List<WorkLogGather> workLogGathers = workLogGatherService.listProjectIsAuth();
            Integer year = NumberUtil.getLastYearNumCurrentTime();
            Integer month = NumberUtil.getLastMonthNumCurrentTime();
            if (!CollectionUtils.isEmpty(workLogGathers)){
                for (WorkLogGather workLogGather : workLogGathers) {
                    workLogGather.setYear(year);workLogGather.setMonth(month+1);
                    WorkLogGather bo = workLogGatherService.SetParameters(workLogGather);
                    bo.setCreateTime(new Date());
                    workLogGatherService.save(bo);
                }
            }
            flag = 0;
        } catch (Exception e) {
            e.printStackTrace();
            flag--;
            log.info("同步生成工作记录统计失败, 还可以重试{}次!", flag);
        }
        if (flag > 0) {
            try {
                Thread.sleep(60 * 60 * 1000);//休眠一个小时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            createWorkLogGather(flag);
        }
    }

    public static void main(String[] args) {
        Integer lastMonthNumCurrentTime = NumberUtil.getLastMonthNumCurrentTime();
        System.out.println(lastMonthNumCurrentTime);
    }
}
