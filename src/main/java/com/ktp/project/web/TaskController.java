package com.ktp.project.web;


import com.ktp.project.component.PushTask;
import com.ktp.project.component.ThankUserPushTask;
import com.ktp.project.component.UserInfoTask;
import com.ktp.project.component.WorkLogWeekPushTask;
import com.ktp.project.logic.schedule.WorkLogGatherTask;
import com.ktp.project.service.OrganService;
import com.ktp.project.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/task", produces = "application/json;charset=UTF-8;")
public class TaskController {

    @Autowired
    private OrganService organService;

//    /**
//     * 周一请关注 ，考勤情况
//     */
//    @RequestMapping(value = "/kaoqinMonTip", method = RequestMethod.GET)
//    @ResponseBody
//    public String kaoqinMonTip() {
//        try {
//            PushTask pushTask = new PushTask();
//            pushTask.task();
//            return "周一请关注 ，考勤情况 推送成功";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
//        }
//    }

    /**
     * 周一   推送工人上一周的工作时长
     */
    @RequestMapping(value = "/WorkMondayWeekWorkTime", method = RequestMethod.GET)
    @ResponseBody
    public String WorkMondayWeekWorkTime() {
        try {
            PushTask pushTask = new PushTask();
            pushTask.worktask();
            return "周一 推送工人上一周的工作时长成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 感恩周年推送
     */
    @RequestMapping(value = "/thanksgivingAnniversary", method = RequestMethod.GET)
    @ResponseBody
    public String thanksgivingAnniversary() {
        try {
            ThankUserPushTask thankUserPushTask = new ThankUserPushTask();
            thankUserPushTask.task();
            return "感恩周年推送 成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 年龄预警  星期一上午9点推送
     */
    @RequestMapping(value = "/warningAge", method = RequestMethod.GET)
    @ResponseBody
    public String warningAge() {
        try {
            organService.pushByAge();
            return "年龄预警  星期一上午9点推送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 工作记录月统计
     */
    @RequestMapping(value = "/createWorkLogGatherTask", method = RequestMethod.GET)
    @ResponseBody
    public String createWorkLogGatherTask() {
        try {
            WorkLogGatherTask workLogGatherTask = new WorkLogGatherTask();
            workLogGatherTask.createWorkLogGatherTask();
            return "工作记录月统计 成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 工作记录周统计
     */
    @RequestMapping(value = "/worklogweek", method = RequestMethod.GET)
    @ResponseBody
    public String worklogweek() {
        try {
            WorkLogWeekPushTask workLogGatherTask = new WorkLogWeekPushTask();
            workLogGatherTask.task();
            return "工作记录周统计 成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 生日提醒
     */
    @RequestMapping(value = "/birthday", method = RequestMethod.GET)
    @ResponseBody
    public String birthday() {
        try {
            UserInfoTask userInfoTask = new UserInfoTask();
            userInfoTask.birthday();
            return "生日提醒 成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }

    /**
     * 身份证有效期提醒
     */
    @RequestMapping(value = "/validityOfIDCard", method = RequestMethod.GET)
    @ResponseBody
    public String validityOfIDCard() {
        try {
            UserInfoTask userInfoTask = new UserInfoTask();
            userInfoTask.validityOfIDCard();
            return "身份证有效期提醒 成功";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.createBussniessErrorJson(500, e.getMessage());
        }
    }


}



