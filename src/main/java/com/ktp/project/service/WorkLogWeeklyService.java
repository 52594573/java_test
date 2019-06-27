package com.ktp.project.service;

import com.ktp.project.dao.ARStatisticsDao;
import com.ktp.project.dao.WorkLogWeeklyDao;
import com.ktp.project.dto.*;
import com.ktp.project.entity.ARStatistics;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class WorkLogWeeklyService {

    private static final Logger log = LoggerFactory.getLogger(WorkLogWeeklyService.class);

    @Autowired
    private WorkLogWeeklyDao workLogWeeklyDao;

    @Autowired
    private ARStatisticsDao arStatisticsDao;

    @Autowired
    private WorkLogService workLogService;


    public void validateUserId(Integer userId) {
        List<Integer> userIds = workLogWeeklyDao.validateUserId();
        if (!userIds.contains(userId)) {
            throw new BusinessException("亲爱的用户,您没有该功能的查看权限!");
        }
    }

    /**
     * 周报 单个班组详情
     *
     * @param pro_id
     * @param wl_type
     * @param datetime
     * @return
     */
    public Map<String, Object> queryWorkLogListbyTeam(int pro_id, int teamid, int wl_type, String startDate, String datetime, int pageNo, int pageSize) {

        //查询出班组下面的全部信息
        List<WorkLogWeeklyDto> workLogWeeklyDtos = workLogWeeklyDao.queryteamList2(pro_id, teamid, wl_type, startDate, datetime, pageNo, pageSize);

        List<WorkLogDetail> workLogDetailList = new ArrayList<>();
        for (WorkLogWeeklyDto wdt : workLogWeeklyDtos) {
            WorkLogDetail workLogDetail = workLogService.worklogDetail(wdt.getId() + "");
            if (workLogDetail != null) {
                workLogDetailList.add(workLogDetail);
            }
        }

        for (WorkLogWeeklyDto workLog : workLogWeeklyDtos) {
            if (StringUtils.isNotBlank(workLog.getWorkLogPicUrl())) {
                String[] split = workLog.getWorkLogPicUrl().split(",");
                workLog.setWorkLogPics(Arrays.asList(split));
            }
        }


        Map<String, Object> result = new HashMap<>();
        int total = 0;

        if (workLogWeeklyDtos != null && workLogWeeklyDtos.size() > 0) {
            total = workLogWeeklyDtos.size();
        }

        double onestarp = 0;
        double twostarp = 0;
        double threestarp = 0;
        double fourstarp = 0;
        double fivestarp = 0;


        //获取到星星分布比例
        if (wl_type == 1 && total > 0) {
            for (WorkLogWeeklyDto wdt : workLogWeeklyDtos) {
                if (wdt.getWlStar() != null && wdt.getWlStar() > 0) {
                    switch (wdt.getWlStar()) {
                        case 1:
                            onestarp = onestarp + 1;
                            break;
                        case 2:
                            twostarp = twostarp + 1;
                            break;
                        case 3:
                            threestarp = threestarp + 1;
                            break;
                        case 4:
                            fourstarp = fourstarp + 1;
                            break;
                        case 5:
                            fivestarp = fivestarp + 1;
                            break;
                    }
                }


            }

            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            onestarp = (onestarp / total) * 100;
            String start1 = df.format(onestarp);
            twostarp = (twostarp / total) * 100;
            String start2 = df.format(twostarp);
            threestarp = (threestarp / total) * 100;
            String start3 = df.format(threestarp);
            fourstarp = (fourstarp / total) * 100;
            String start4 = df.format(fourstarp);
            fivestarp = (fivestarp / total) * 100;
            String start5 = df.format(fivestarp);


            Map<String, Object> start = new HashMap<>();
            start.put("onestarp", start1);
            start.put("twostarp", start2);
            start.put("threestarp", start3);
            start.put("fourstarp", start4);
            start.put("fivestarp", start5);

            result.put("start", start);
        } else {
            Map<String, Object> start = new HashMap<>();
            start.put("onestarp", 0);
            start.put("twostarp", 0);
            start.put("threestarp", 0);
            start.put("fourstarp", 0);
            start.put("fivestarp", 0);
            result.put("start", start);
        }

        if (wl_type == 2 && total > 0) {
            double serious = 0;//严重
            double caution = 0;//警示
            double ordinary = 0;//普通
            for (WorkLogWeeklyDto wdt : workLogWeeklyDtos) {
                if (wdt.getWlYzcd() != null && wdt.getWlYzcd() > 0) {
                    //1.严重2.普通3.警示
                    switch (wdt.getWlYzcd()) {
                        case 1:
                            serious = serious + 1;
                            break;
                        case 2:
                            caution = caution + 1;
                            break;
                        case 3:
                            ordinary = ordinary + 1;
                            break;
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            serious = (serious / total) * 100;
            String start1 = df.format(serious);
            caution = (caution / total) * 100;
            String start2 = df.format(caution);
            ordinary = (ordinary / total) * 100;
            String start3 = df.format(ordinary);

            WorkLogWeeklySeriousProportionDto querySeriousProportion = new WorkLogWeeklySeriousProportionDto();
            querySeriousProportion.setSerious(start1);
            querySeriousProportion.setCaution(start2);
            querySeriousProportion.setOrdinary(start3);
            result.put("seriousproportion", querySeriousProportion);
        } else {
            WorkLogWeeklySeriousProportionDto querySeriousProportion = new WorkLogWeeklySeriousProportionDto();
            querySeriousProportion.setCaution("0");
            querySeriousProportion.setSerious("0");
            querySeriousProportion.setOrdinary("0");
            result.put("seriousproportion", querySeriousProportion);
        }


        result.put("workLogList", workLogDetailList);

        return result;
    }


    /**
     * 周报全部班组情况
     *
     * @param pro_id
     * @param wl_type
     * @param datetime
     * @return
     */
    public Map<String, Object> querystatdetail(int pro_id, int wl_type, String startDate, String datetime) {
        //查询出这一周的全部工作记录
        WorkLogWeeklyDetailDto querystart = workLogWeeklyDao.querystart(pro_id, wl_type, startDate, datetime);
        WorkLogWeeklySeriousProportionDto querySeriousProportion = new WorkLogWeeklySeriousProportionDto();
        querySeriousProportion.setCaution("0");
        querySeriousProportion.setSerious("0");
        querySeriousProportion.setOrdinary("0");

        //查询该项目的全部班组工作情况
        List<WorkLogWeeklyDetailTeamListDto> teamLists = workLogWeeklyDao.queryteamListnew(pro_id, wl_type, startDate, datetime);
        if (teamLists != null && teamLists.size() > 0) {
            for (int i = teamLists.size() - 1; i >= 0; i--) {
                if (teamLists.get(i).getQualitynum().equals(new BigDecimal("0")) && teamLists.get(i).getSeriounum().equals(new BigDecimal("0"))) {
                    teamLists.remove(teamLists.get(i));
                }
            }
        }

        querystart.setTeamLists(teamLists);

        Map<String, Object> result = new HashMap<>();

        Map<String, Object> start = new HashMap<>();
        start.put("onestarp", 0);
        start.put("twostarp", 0);
        start.put("threestarp", 0);
        start.put("fourstarp", 0);
        start.put("fivestarp", 0);
        if (querystart != null) {
            start.put("onestarp", querystart.getOnestarp());
            start.put("twostarp", querystart.getTwostarp());
            start.put("threestarp", querystart.getThreestarp());
            start.put("fourstarp", querystart.getFourstarp());
            start.put("fivestarp", querystart.getFivestarp());
        }

        WorkLogWeeklySeriousProportionDto querySeriousProportion1 = workLogWeeklyDao.querySeriousProportion(pro_id, wl_type, startDate, datetime);
        if (querySeriousProportion1 != null) {
            if (querySeriousProportion.getCaution() != null) {
                querySeriousProportion.setCaution(querySeriousProportion1.getCaution() + "");
            }
            if (querySeriousProportion.getSerious() != null) {
                querySeriousProportion.setSerious(querySeriousProportion1.getSerious() + "");
            }
            if (querySeriousProportion.getOrdinary() != null) {
                querySeriousProportion.setOrdinary(querySeriousProportion1.getOrdinary() + "");
            }
        }

        result.put("start", start);
        result.put("seriousproportion", querySeriousProportion);
        result.put("teamList", teamLists);

        return result;
    }


    /**
     * 周报统计
     *
     * @param pro_id
     * @param datetime
     * @return
     */
    public Map<String, Object> querystat(int pro_id, String startDate, String datetime) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(datetime)) {
            datetime = DateUtil.getNowDate();
        }
        String wl_type = "1";
        //质量
        Long querystat = workLogWeeklyDao.querystat(pro_id, wl_type, startDate, datetime);
        Long querystatseri = workLogWeeklyDao.queryStartseri(pro_id, wl_type, startDate, datetime);

        //班组数
        int qualitystatteam = 0;
        int safestatteam = 0;

        //查询质量班组数
        qualitystatteam = workLogWeeklyDao.queryteamnumber(pro_id, Integer.parseInt(wl_type), startDate, datetime);

        //安全
        wl_type = "2";

        //查询安全班组数
        safestatteam = workLogWeeklyDao.queryteamnumber(pro_id, Integer.parseInt(wl_type), startDate, datetime);

        Long querystatsaft = workLogWeeklyDao.querystat(pro_id, wl_type, startDate, datetime);
        Long querystatsaftseri = workLogWeeklyDao.queryseri(pro_id, wl_type, "1", startDate, datetime);
        //出勤人数
        int userCount = arStatisticsDao.queryUserCount(pro_id, datetime);
        //总工时
        List<ARStatistics> list = arStatisticsDao.queryTineCount(pro_id, datetime);
        int sum = 0;
        for (ARStatistics arStatistics : list) {
            if(arStatistics.getHours()==null){
                sum +=0;
            }else {
                sum += arStatistics.getHours();
            }
        }
        Map<String, Object> quality = new HashMap<>();
        quality.put("total", querystat);
        quality.put("serious", querystatseri);
        quality.put("team", qualitystatteam);

        Map<String, Object> saft = new HashMap<>();
        saft.put("total", querystatsaft);
        saft.put("serious", querystatsaftseri);
        saft.put("team", safestatteam);

        Map<String, Object> checkwork = new HashMap<>();

        checkwork.put("attendance", userCount);
        checkwork.put("totalworkhours", sum / 480);

        result.put("quality", quality);
        result.put("saft", saft);
        result.put("checkwork", checkwork);

        return result;
    }


}
