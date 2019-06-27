package com.ktp.project.logic.schedule;

import com.ktp.project.entity.WorkLogGather;
import com.ktp.project.service.WorkLogGatherService;
import com.ktp.project.service.realName.GmAuthRealNameApi;
import com.ktp.project.service.realName.impl.GmAuthRealNameService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class TestGmTask {

    private static Logger log = LoggerFactory.getLogger(TestGmTask.class);

    @Autowired
    private GmAuthRealNameApi gmAuthRealNameApi;


    /**
     *
     * 同步高明数据
     */
   // @Scheduled(cron = "0 57 15 * * ?")
    public void createWorkLogGatherTask() {
        log.info("定时任务同步高明数据开始, 开始时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
                //gmAuthRealNameApi.synCompanyInfo( "9144078319428766XQ",  "开平住宅建筑工程集团有限公司",  "5894","2017-08-17");
        //项目
       // gmAuthRealNameApi.synWorkinfo(63);
        //班组
        //gmAuthRealNameApi.uploadTeamInfo(63, 987,  "照明",  "POSAVE");
        //修改班组
        //gmAuthRealNameApi.uploadTeamInfo(63, 830,  "市政",  "POUPDATE");
        //班组人员信息
        // gmAuthRealNameApi.uploadRosterInfo(63,830,3477);
        //进退场
        // gmAuthRealNameApi.synBuildPoUserJT( 63,  3477,  "",  830);
        //考勤
        //gmAuthRealNameApi.synWorkerAttendance( 63,  42727);
        log.info("定时任务同步高明数据结束, 结束时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));



    }

}
