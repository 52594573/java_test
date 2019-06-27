package com.ktp.project.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ktp.project.dao.MessageSwitchDao;
import com.ktp.project.dao.QueryChannelDao;
import com.ktp.project.dto.im.GroupUserDto;
import com.ktp.project.dto.im.MessageDto;
import com.ktp.project.service.OrganService;
import com.ktp.project.util.DateUtil;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 考勤预警推送
 * Created by LinHon 2018/12/7
 */
@Component
public class ClockInPushTask {

    private static final Logger log = LoggerFactory.getLogger(ClockInPushTask.class);

    private static final Integer DEFAULT_WORK_TYPE = 24;

    private static final Integer CLOCK_IN_TYPE = 1;

    private static final String CLOCK_IN_TITLE = "考勤预警";

    @Autowired
    private QueryChannelDao queryChannelDao;

    @Autowired
    private OrganService organService;

    @Autowired
    private MessageSwitchDao messageSwitchDao;


    /**
     * 当天考勤推送
     */
    //@Scheduled(cron = "0 0 19 * * ?")
    public void today() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        try {
            List<Project> projects = queryChannelDao.queryMany("from Project p where p.isDel = 0", Project.class);
            for (Project project : projects) {
                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        String sql = String.format("select user_id , k_state from kaoqin%d where to_days(k_time)  = to_days(now())", project.getId());
                        try {
                            List<Object[]> clockIn = queryChannelDao.selectMany(sql);
                            Map<Integer, Day.Detail> pushMap = getPushMapByTimes(project, clockIn);
                            pushToWorker(project, pushMap);  // 推送工人当天出入场次数不足的数据给工人
                            pushToLeader(project, pushMap);  // 推送工人当天出入场次数不足的数据给相关负责人
                        } catch (Exception e) {
                            log.error("推送当天考勤异常", e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            log.error("推送考勤失败", e);
        } finally {
            fixedThreadPool.shutdown();
        }
    }


    /**
     * 一周考勤预警推送
     */
    //@Scheduled(cron = "0 0 7 * * ?")
    public void week() {
        try {
            List<Project> projects = queryChannelDao.queryMany("from Project p where p.isDel = 0", Project.class);
            for (Project project : projects) {
                try {
                    pushToLeaderByWeek(project);  // 推送7天无考勤记录的数据给相关负责人
                } catch (Exception e) {
                    log.error("推送7天无考勤记录异常", e);
                }
            }
        } catch (Exception e) {
            log.error("推送考勤失败", e);
        }
    }

    // 取得考勤次数小于4的用户，Map<用户ID，DATA>
    private Map<Integer, Day.Detail> getPushMapByTimes(Project project, List<Object[]> clockIn) {
        Map<Integer, Day.Detail> today = Maps.newHashMap();
        for (Object[] array : clockIn) {
            Integer userId = Integer.parseInt(array[0].toString());
            Integer type = Integer.parseInt(array[1].toString());
            if (!today.containsKey(userId)) {
                today.put(userId, new Day.Detail());
            }
            Day.Detail detail = today.get(userId);
            if (type == 3) {
                detail.addEnter();
            }
            if (type == 4) {
                detail.addOut();
            }
        }

        // 初始化成员出入场次数
        Map<Integer, Day.Detail> initMap = Maps.newHashMap();
        List<Integer> userIds = organService.queryUserIds(project.getId(), 2);
        for (Integer userId : userIds) {
            initMap.put(userId, new Day.Detail());
        }

        // 将今天有考勤记录的数据put到已经初始化的Map
        for (Map.Entry<Integer, Day.Detail> entry : today.entrySet()) {
            if (initMap.containsKey(entry.getKey())) {
                initMap.put(entry.getKey(), entry.getValue());
            }
        }

        // 筛选出考勤次数低于4次的数据
        Map<Integer, Day.Detail> pushMap = Maps.newHashMap();
        for (Map.Entry<Integer, Day.Detail> entry : initMap.entrySet()) {
            int count = entry.getValue().count();
            if (count < 4) {
                pushMap.put(entry.getKey(), entry.getValue());
            }
        }
        return pushMap;
    }


    /**
     * 推送工人当天出入场次数不足的数据给工人
     *
     * @param project
     * @param pushMap
     */
    private void pushToWorker(Project project, Map<Integer, Day.Detail> pushMap) {
        List<Integer> ignoreUserIds = messageSwitchDao.selectUserIdsOfIgnore(project.getId(), CLOCK_IN_TYPE);
        for (Map.Entry<Integer, Day.Detail> entry : pushMap.entrySet()) {
            try {
                MessageDto messageDto = new MessageDto("project-" + project.getId(), Lists.newArrayList(entry.getKey()), "users", ignoreUserIds.contains(entry.getKey()));
                messageDto.setTitle(CLOCK_IN_TITLE);
                messageDto.putExt("myUserName", project.getPName());
                messageDto.putExt("projectId", project.getId());
                messageDto.putExt("type", "clockInTimesInTodayToWorker");
                messageDto.putExt("count", entry.getValue().count());
                messageDto.putExt("date", DateUtil.format(new Date(), DateUtil.FORMAT_DATE));
                HuanXinRequestUtils.sendExtendMessage(messageDto);
            } catch (Exception e) {
                log.error("推送每日考勤次数异常给工人失败", e);
            }
        }
    }


    /**
     * 推送工人当天出入场次数不足的数据给相关负责人
     *
     * @param project
     * @param pushMap
     */
    private void pushToLeader(Project project, Map<Integer, Day.Detail> pushMap) {
        // 初始化每个班组的数据
        Map<Integer, Day> initMap = getInitMap(project, pushMap);
        // 筛选出班组中没有异常考勤次数的数据
        Map<Integer, Day> newPushMap = getNewPushMapOfDay(initMap);
        // 推送消息
        doPushToLeader(project, newPushMap);
    }

    private Map<Integer, Day> getInitMap(Project project, Map<Integer, Day.Detail> pushMap) {
        Map<Integer, Day> initMap = Maps.newHashMap();
        List<ProOrgan> proOrgans = organService.queryManyByProjectId(project.getId(), 2);
        for (ProOrgan proOrgan : proOrgans) {
            try {
                List<GroupUserDto> users = organService.queryUsersOfOrgan(proOrgan.getId());
                if (users.size() == 0) {
                    continue;
                }
                Day day = getInitDay(proOrgan);
                for (GroupUserDto user : users) {
                    int organId = proOrgan.getId();
                    if (!initMap.containsKey(organId)) {
                        initMap.put(organId, day);
                    }

                    int userId = user.getUserId();
                    if (pushMap.containsKey(userId)) {
                        Day.Detail detail = pushMap.get(userId);
                        detail.setRealName(user.getuRealname());
                        day.addDetails(detail);
                    }
                }
            } catch (Exception e) {
            }
        }
        return initMap;
    }

    private Day getInitDay(ProOrgan proOrgan) {
        Day day = new Day();

        //处理脏数据
        Integer keyId = proOrgan.getPoGzid();
        if (keyId == null) {
            keyId = DEFAULT_WORK_TYPE;
        }
        if (!(proOrgan.getPoFzr() == null || proOrgan.getPoFzr() == 0)) {
            UserInfo leader = queryChannelDao.queryOne(UserInfo.class, proOrgan.getPoFzr());
            day.setLeaderRealName(leader.getU_realname());
        }

        KeyContent workType = queryChannelDao.queryOne(KeyContent.class, keyId);
        day.setOrganOwnerId(proOrgan.getPoFzr());
        day.setOrganName(proOrgan.getPoName());
        day.setWorkTypeName(workType.getKeyName());
        return day;
    }

    private Map<Integer, Day> getNewPushMapOfDay(Map<Integer, Day> initMap) {
        Map<Integer, Day> newPushMap = Maps.newHashMap(initMap);
        for (Map.Entry<Integer, Day> entry : initMap.entrySet()) {
            if (entry.getValue().getDetails().size() == 0) {
                newPushMap.remove(entry.getKey());
            }
        }
        return newPushMap;
    }

    private void doPushToLeader(Project project, Map<Integer, Day> newPushMap) {
        List<Integer> ignoreUserIds = messageSwitchDao.selectUserIdsOfIgnore(project.getId(), CLOCK_IN_TYPE);
        // 取得项目中所有生产经理用户ID
        List<Integer> leaderUserIds = organService.queryLeaderUserIds(project.getId(), 118);

        // 发送消息
        for (Map.Entry<Integer, Day> entry : newPushMap.entrySet()) {
            try {
                Day day = entry.getValue();
                List<Integer> newLeaderUserIds = Lists.newArrayList(leaderUserIds);
                newLeaderUserIds.add(day.getOrganOwnerId());

                Map<String, List<Integer>> filterMap = filter(ignoreUserIds, newLeaderUserIds);
                doSend(project, day, filterMap.get("ignore"), true);
                doSend(project, day, filterMap.get("notice"), false);
            } catch (Exception e) {
                log.error("推送每日考勤次数异常给工人失败", e);
            }
        }
    }

    /**
     * 筛选出忽略通知和不不略通知的用户
     *
     * @param ignoreUserIds
     * @param pushUserIds
     * @return
     */
    public Map<String, List<Integer>> filter(List<Integer> ignoreUserIds, List<Integer> pushUserIds) {
        Map result = Maps.newHashMap();
        result.put("ignore", pushUserIds.stream().filter(userId -> ignoreUserIds.contains(userId)).collect(Collectors.toList()));
        result.put("notice", pushUserIds.stream().filter(userId -> !ignoreUserIds.contains(userId)).collect(Collectors.toList()));
        return result;
    }

    private void doSend(Project project, Day day, List<Integer> toUserIds, boolean isIgnore) {
        MessageDto messageDto = new MessageDto("project-" + project.getId(), toUserIds, "users", isIgnore);
        messageDto.setTitle(CLOCK_IN_TITLE);
        messageDto.putExt("myUserName", project.getPName());
        messageDto.putExt("projectId", project.getId());
        messageDto.putExt("type", "clockInTimesInTodayToLeader");
        messageDto.putExt("data", day);
        HuanXinRequestUtils.sendExtendMessage(messageDto);
    }


    /**
     * 推送7天无考勤记录的数据给相关负责人
     *
     * @param project
     */
    private void pushToLeaderByWeek(Project project) {
        // 筛选出没有考勤记录的用户ID
        List<Integer> pushUserIds = getPushUserIds(project);
        // 初始化每个班组的数据
        Map<Integer, Week> initMap = getInitMap(project, pushUserIds);
        // 筛选掉班组中没有异常考勤次数的数据
        Map<Integer, Week> newPushMap = getNewPushMap(initMap);
        // 推送消息
        doPushToLeaderByWeek(project, newPushMap);
    }

    private List<Integer> getPushUserIds(Project project) {
        List<Integer> pushUserIds = Lists.newArrayList();
        String sql = String.format("select user_id from kaoqin%d where date_sub(curdate(), interval 7 day) <= date(k_time) group by user_id", project.getId());
        List<Integer> userIds = queryChannelDao.selectMany(sql);

        // 筛选出没有考勤记录的用户ID
        List<Integer> allUserIds = organService.queryUserIds(project.getId(), 2);
        for (Integer userId : allUserIds) {
            if (!userIds.contains(userId)) {
                pushUserIds.add(userId);
            }
        }
        return pushUserIds;
    }

    private Map<Integer, Week> getInitMap(Project project, List<Integer> pushUserIds) {
        Map<Integer, Week> initMap = Maps.newHashMap();
        String sql = String.format("select k_time from kaoqin%d where user_id = ? order by k_time desc limit 1", project.getId());
        List<ProOrgan> proOrgans = organService.queryManyByProjectId(project.getId(), 2);
        for (ProOrgan proOrgan : proOrgans) {
            try {
                List<Object[]> users = organService.selectUsersOfOrgan(proOrgan.getId());
                if (users.size() == 0) {
                    continue;
                }
                Week week = getInitWeek(proOrgan);
                for (Object[] user : users) {
                    int organId = proOrgan.getId();
                    if (!initMap.containsKey(organId)) {
                        initMap.put(organId, week);
                    }

                    int userId = Integer.parseInt(user[0].toString());
                    if (pushUserIds.contains(userId)) {
                        Week.Detail detail = new Week.Detail();
                        detail.setRealName(user[1].toString());

                        Date date = queryChannelDao.selectOne(sql, Lists.newArrayList(userId));
                        if (date != null) {
                            detail.setDays(subDays(date, new Date()));
                            week.addDetails(detail);
                        } else {
                            long days = subDays((Date) user[3], new Date());
                            if (days >= 7) {
                                detail.setDays(days);
                                week.addDetails(detail);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return initMap;
    }

    private Week getInitWeek(ProOrgan proOrgan) {
        Week week = new Week();

        //处理脏数据
        Integer keyId = proOrgan.getPoGzid();
        if (keyId == null) {
            keyId = DEFAULT_WORK_TYPE;
        }
        if (!(proOrgan.getPoFzr() == null || proOrgan.getPoFzr() == 0)) {
            UserInfo leader = queryChannelDao.queryOne(UserInfo.class, proOrgan.getPoFzr());
            week.setLeaderRealName(leader.getU_realname());
        }

        KeyContent workType = queryChannelDao.queryOne(KeyContent.class, keyId);
        week.setOrganOwnerId(proOrgan.getPoFzr());
        week.setOrganName(proOrgan.getPoName());
        week.setWorkTypeName(workType.getKeyName());
        return week;
    }

    private Map<Integer, Week> getNewPushMap(Map<Integer, Week> initMap) {
        Map<Integer, Week> newPushMap = Maps.newHashMap(initMap);
        for (Map.Entry<Integer, Week> entry : initMap.entrySet()) {
            if (entry.getValue().getDetails().size() == 0) {
                newPushMap.remove(entry.getKey());
            }
        }
        return newPushMap;
    }

    private void doPushToLeaderByWeek(Project project, Map<Integer, Week> newPushMap) {
        List<Integer> ignoreUserIds = messageSwitchDao.selectUserIdsOfIgnore(project.getId(), CLOCK_IN_TYPE);
        // 取得项目中所有生产经理用户ID
        List<Integer> leaderUserIds = organService.queryLeaderUserIds(project.getId(), 118);
        // 发送消息
        for (Map.Entry<Integer, Week> entry : newPushMap.entrySet()) {
            try {
                Week week = entry.getValue();
                List<Integer> newLeaderUserIds = Lists.newArrayList(leaderUserIds);
                newLeaderUserIds.add(week.getOrganOwnerId());

                Map<String, List<Integer>> filterMap = filter(ignoreUserIds, newLeaderUserIds);
                doSend(project, week, filterMap.get("ignore"), true);
                doSend(project, week, filterMap.get("notice"), false);
            } catch (Exception e) {
                log.error("推送7天无考勤记录失败", e);
            }
        }
    }

    private void doSend(Project project, Week week, List<Integer> toUserIds, boolean isIgnore) {
        MessageDto messageDto = new MessageDto("project-" + project.getId(), toUserIds, "users", isIgnore);
        messageDto.setTitle(CLOCK_IN_TITLE);
        messageDto.putExt("myUserName", project.getPName());
        messageDto.putExt("projectId", project.getId());
        messageDto.putExt("type", "clockInDaysToLeader");
        messageDto.putExt("data", week);
        HuanXinRequestUtils.sendExtendMessage(messageDto);
    }

    public static class Day {

        private List<Detail> details = Lists.newArrayList();

        private int organOwnerId;

        private String organName;

        private String leaderRealName;

        private String workTypeName;

        private String date = DateUtil.format(new Date(), DateUtil.FORMAT_DATE);


        public List<Detail> getDetails() {
            return details;
        }

        public void addDetails(Detail kqUser) {
            details.add(kqUser);
        }

        public int getOrganOwnerId() {
            return organOwnerId;
        }

        public void setOrganOwnerId(int organOwnerId) {
            this.organOwnerId = organOwnerId;
        }

        public String getOrganName() {
            return organName;
        }

        public void setOrganName(String organName) {
            this.organName = organName;
        }

        public void setLeaderRealName(String leaderRealName) {
            this.leaderRealName = leaderRealName;
        }

        public String getLeaderRealName() {
            return leaderRealName;
        }

        public String getWorkTypeName() {
            return workTypeName;
        }

        public void setWorkTypeName(String workTypeName) {
            this.workTypeName = workTypeName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public static class Detail {

            private String realName;

            private int enter;

            private int out;


            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public int getEnter() {
                return enter;
            }

            public void setEnter(int enter) {
                this.enter = enter;
            }

            public int getOut() {
                return out;
            }

            public void setOut(int out) {
                this.out = out;
            }

            public void addEnter() {
                ++enter;
            }

            public void addOut() {
                ++out;
            }

            public int count() {
                return enter + out;
            }
        }
    }


    public static class Week {

        private List<Detail> details = Lists.newArrayList();

        private int organOwnerId;

        private String organName;

        private String leaderRealName;

        private String workTypeName;

        private String date = DateUtil.format(new Date(), DateUtil.FORMAT_DATE);


        public List<Detail> getDetails() {
            return details;
        }

        public void addDetails(Detail detail) {
            details.add(detail);
        }

        public int getOrganOwnerId() {
            return organOwnerId;
        }

        public void setOrganOwnerId(int organOwnerId) {
            this.organOwnerId = organOwnerId;
        }

        public String getOrganName() {
            return organName;
        }

        public void setOrganName(String organName) {
            this.organName = organName;
        }

        public String getLeaderRealName() {
            return leaderRealName;
        }

        public void setLeaderRealName(String leaderRealName) {
            this.leaderRealName = leaderRealName;
        }

        public String getWorkTypeName() {
            return workTypeName;
        }

        public void setWorkTypeName(String workTypeName) {
            this.workTypeName = workTypeName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public static class Detail {

            private String realName;

            private long days;


            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public long getDays() {
                return days;
            }

            public void setDays(long days) {
                this.days = days;
            }
        }
    }


    private long subDays(Date begin, Date end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginFormat = format.parse(format.format(begin));
            Date endFormat = format.parse(format.format(end));
            return (endFormat.getTime() - beginFormat.getTime()) / (3600 * 1000 * 24);
        } catch (ParseException e) {
            throw new RuntimeException("格式化时间异常");
        }
    }

}
