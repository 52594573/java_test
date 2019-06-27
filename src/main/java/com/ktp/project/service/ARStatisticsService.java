package com.ktp.project.service;

import com.ktp.project.dao.ARStatisticsDao;
import com.ktp.project.entity.ARStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: tangbin
 * @Date: 2018/12/17 19:46
 * @Version 1.0
 */
@Service
public class ARStatisticsService {

    @Autowired
    private ARStatisticsDao arStatisticsDao;

    //查询当前项目指定时间的考勤人数
    public Integer getDayCount(String sql,String endDate,Integer projectId) {
        return arStatisticsDao.getDayCount(sql,endDate,projectId);
    }

    //查询当前项目指定时间考勤人的工作时长
    public Integer getDayWorkCount(String sqlWork,String endDate,Integer projectId) {
        List<Integer> dayWorkCount = arStatisticsDao.getDayWorkCount(sqlWork,endDate,projectId);
        int sum=0;
        if(dayWorkCount.size()!=0 && !dayWorkCount.isEmpty()){
            for (Integer bigInteger : dayWorkCount) {
                sum+=bigInteger;
            }
        }
        return sum/480;
    }

    public List<String> getGZName(Integer proId) {
        return arStatisticsDao.getGZName(proId);
    }

    public Integer getBanZuCount(String banZuCountSql,Integer projectId,String gzName,String endDate) {
        return arStatisticsDao.getBanZuCount(banZuCountSql,projectId,gzName,endDate);
    }

    public Integer getBanZuCountWork(String banZuCountWorkSql,Integer projectId,String gzName,String endDate) {
        List<Integer> banZuCountWork = arStatisticsDao.getBanZuCountWork(banZuCountWorkSql,projectId,gzName,endDate);
        int sum=0;
        if(banZuCountWork.size()!=0 && !banZuCountWork.isEmpty()){
            for (Integer bigInteger : banZuCountWork) {
                sum+=bigInteger;
            }
        }
        return sum/480;
    }

    public List<ARStatistics> getAllARS(Integer projectId, String startDate) {
        return arStatisticsDao.getAllARS(projectId,startDate);
    }

    public List<Map<String, Object>> getAll(Integer projectId, String startDate, String endDate) {
        return arStatisticsDao.getAll(projectId,startDate,endDate);
    }

    public List<Map<String, Object>> getWeek(Integer projectId, String startDate, String endDate) {
        return arStatisticsDao.getWeek(projectId,startDate,endDate);
    }

    public Map<String, Object> getXiangqin1(Integer projectId, String startDate, String endDate){
        List<Map<String,Object>> banzumin = this.getBanzu(projectId);
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> banzulist = new ArrayList<>();
        String str ="";
        for (Map<String, Object> stringObjectMap : banzumin) {
            Integer poId = (Integer) stringObjectMap.get("po_id");
            this.getGZName(poId);
            try{
                List<Map<String,Object>> mapList = this.getALLProId(projectId,poId,startDate);
                Map<String,Object> map = new HashMap<>();
                StringBuffer dayWork = new StringBuffer();
                StringBuffer dayTime = new StringBuffer();
                if(mapList.size()==0){
                    if(dayWork.length()==0){
                        dayWork.append("0,0,0,0,0,0,0,");
                        dayTime.append("0,0,0,0,0,0,0,");
                    }
                    dayWork=dayWork.deleteCharAt(dayWork.length()-1);
                    dayTime=dayTime.deleteCharAt(dayTime.length()-1);
                    map.put("week_work",dayWork);
                    map.put("week_time",dayTime);
                    map.put("gz_name",stringObjectMap.get("po_name"));
                    banzulist.add(map);
                    continue;
                }
                for (Map<String, Object> objectMap : mapList) {
                    String counts = (String) objectMap.get("counts");
                    String[] strings = counts.split(",");
                    int[] ints = new int[strings.length];
                    for(int i=0;i<strings.length;i++){
                        ints[i] = Integer.parseInt(strings[i]);
                    }
                    dayWork.append(ints.length).append(",");
                    String hours = (String) objectMap.get("hours");
                    String[] split = hours.split(",");
                    int[] ins = new int[split.length];
                    for (int i = 0; i <split.length ; i++) {
                        ins[i]=Integer.parseInt(split[i]);
                    }
                    int sum = 0;
                    for (int i : ins) {
                        sum+=i;
                    }
                    dayTime.append(sum/480).append(",");
                }
                if(dayWork.length()==0){
                    dayWork.append("0,0,0,0,0,0,0,");
                    dayTime.append("0,0,0,0,0,0,0,");
                }
                dayWork=dayWork.deleteCharAt(dayWork.length()-1);
                dayTime=dayTime.deleteCharAt(dayTime.length()-1);
                map.put("week_work",dayWork);
                map.put("week_time",dayTime);
                map.put("gz_name",stringObjectMap.get("po_name"));
                banzulist.add(map);
            }catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        //查询班组下的总人数
        List<Map<String,Object>> ban = this.getBanZu(projectId);
        int allCount = 0;
        for (Map<String, Object> banzumap : banzulist) {
            String po_name = (String) banzumap.get("gz_name");
            if(po_name==null){
                continue;
            }
            for (Map<String, Object> map : ban) {
                String po_name2 = (String) map.get("po_name");
                if(po_name.equals(po_name2)){
                    String counts = (String) map.get("counts");

                    String[] strings = counts.split(",");
                    int[] ints = new int[strings.length];
                    for(int j=0;j<strings.length;j++){
                        ints[j] = Integer.parseInt(strings[j]);
                    }
                    banzumap.put("count",ints.length);
                    allCount+=ints.length;
                }
            }
        }
        for (Map<String, Object> map : banzulist) {
            Object count = map.get("count");
            if(count==null){
                map.put("count",0);
            }
        }
        StringBuffer stringBufferWork = new StringBuffer();
        StringBuffer stringBufferTime = new StringBuffer();
        List<Map<String, Object>> listmap = arStatisticsDao.getAllCount(projectId,startDate,endDate);
        for (Map<String, Object> map : listmap) {
            String hours = (String) map.get("timecount");
            if(hours==null){
                hours="0";
            }
            String[] strings = hours.split(",");
            int[] ints = new int[strings.length];
            for(int j=0;j<strings.length;j++){
                ints[j] = Integer.parseInt(strings[j]);
            }
            int timeCount = 0;
            for (int i : ints) {
                timeCount+=i;
            }
            stringBufferTime.append(timeCount/480).append(",");
            BigInteger bigInteger = (BigInteger) map.get("workcount");
            Integer counts = bigInteger.intValue();
            stringBufferWork.append(counts).append(",");
        }
        if(stringBufferWork.length()==0){
            stringBufferWork.append("0,0,0,0,0,0,0,");
            stringBufferTime.append("0,0,0,0,0,0,0,");
        }
        stringBufferWork.deleteCharAt(stringBufferWork.length()-1);
        stringBufferTime.deleteCharAt(stringBufferTime.length()-1);
        result.put("week_allwork",stringBufferWork.toString());
        result.put("week_alltime",stringBufferTime.toString());
        result.put("count",allCount);
        result.put("banzu_list",banzulist);
        return result;
    }

    private List<Map<String, Object>> getALLProId(Integer projectid,Integer poId, String startDate) {
        return arStatisticsDao.getALLProId(projectid,poId,startDate);
    }

    private List<Map<String, Object>> getBanzu(Integer projectId) {
        return arStatisticsDao.getBanzuMin(projectId);
    }

    public Map<String, Object> getXiangqin(Integer projectId, String startDate, String endDate){
        List<Map<String,Object>> list = this.getAll(projectId,startDate,endDate);
        String gzName = "";
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> banzulist = new ArrayList<>();
        StringBuffer dayWork = new StringBuffer();
        StringBuffer dayTime = new StringBuffer();
        int time = 0;
        if(list.size()==0){
            result.put("week_allwork","0,0,0,0,0,0,0");
            result.put("week_alltime","0,0,0,0,0,0,0");
            result.put("count",0);
            return result;
        }
        Map<String, Object> objectMap = list.get(list.size()-1);
        String lastklTime = (String) objectMap.get("klTime");
        int ndate = 0;
        int poId1 = (int) objectMap.get("poId");
        for (Map<String, Object> stringObjectMap : list) {
            Map<String,Object> banzu = new HashMap<>();
            int poId2 = (int)stringObjectMap.get("poId");
            if(time==7){
                dayWork = new StringBuffer();
                dayTime = new StringBuffer();
                time=0;
            }
            Object object = stringObjectMap.get("count");
            if(object==null){
                if(time>0&&time<7){
                    for (int i = time; i <7; i++) {
                        dayWork.append("0,");
                        dayTime.append("0,");
                    }
                    dayWork=dayWork.deleteCharAt(dayWork.length()-1);
                    dayTime=dayTime.deleteCharAt(dayTime.length()-1);
                    banzu.put("gz_id",stringObjectMap.get("proId"));
                    banzu.put("week_work",dayWork.toString());
                    banzu.put("week_time",dayTime.toString());
                    banzu.put("gz_name",gzName);
                    banzulist.add(banzu);
                    time=0;
                    dayWork=new StringBuffer();
                    dayTime=new StringBuffer();
                    banzu = new HashMap<>();
                }
                banzu.put("gz_id",stringObjectMap.get("proId"));
                banzu.put("week_work","0,0,0,0,0,0,0");
                banzu.put("week_time","0,0,0,0,0,0,0");
                banzu.put("gz_name",stringObjectMap.get("po_name"));
                banzulist.add(banzu);
            }
            String klTime = (String) stringObjectMap.get("klTime");
            if(object!=null){
                String[] split = klTime.split("-");
                int nowdate = Integer.parseInt(split[split.length-1]);
                String name = (String) stringObjectMap.get("po_name");
                if(name.equals(gzName)){
                    time++;
                    if(klTime.equals(startDate)){
                        String hoursStr = (String) stringObjectMap.get("hoursStr");
                        if(hoursStr==null){
                            hoursStr="0";
                        }
                        String[] strings = hoursStr.split(",");
                        int[] ints = new int[strings.length];
                        for(int i=0;i<strings.length;i++){
                            ints[i] = Integer.parseInt(strings[i]);
                        }
                        int sum = 0;
                        for (int i : ints) {
                            sum+=i;
                        }
                        sum=sum/480;
                        Object count = stringObjectMap.get("count");
                        if(count==null){
                            count=0;
                        }
                        if(klTime.equals(startDate)){
                            dayWork.append(count).append(",");
                            dayTime.append(sum).append(",");
                        }
                    }
                    if(!klTime.equals(startDate)){
                        int lengthwork = dayWork.length()-1;
                        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
                        Date date=null;
                        Calendar c = Calendar.getInstance();
                        try {
                            date = new SimpleDateFormat("yy-MM-dd").parse(startDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.setTime(date);
                        int day=c.get(Calendar.DATE);
                        for (int i = 1; i <7 ; i++) {
                            c.set(Calendar.DATE, day + i);
                            Date date1 = null;
                            String dayBefore = sdf.format(c.getTime());
                            try {
                                date1 = sdf.parse(dayBefore);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String dateq = sdf.format(date1);
                            if(klTime.equals(dateq)){
                                if(nowdate-ndate>1){
                                    int number = nowdate-ndate;
                                    for (int j = 0; j <number-1 ; j++) {
                                        dayWork.append("0,");
                                        dayTime.append("0,");
                                    }
                                    time+=number-1;
                                    ndate=0;
                                }
                                String hoursStr = (String) stringObjectMap.get("hoursStr");
                                if(hoursStr==null){
                                    hoursStr="0";
                                }
                                String[] strings = hoursStr.split(",");
                                int[] ints = new int[strings.length];
                                for(int j=0;j<strings.length;j++){
                                    ints[j] = Integer.parseInt(strings[j]);
                                }
                                int sum = 0;
                                for (int num : ints) {
                                    sum+=num;
                                }
                                sum=sum/480;
                                Object count = stringObjectMap.get("count");
                                if(count==null){
                                    count=0;
                                }
                                dayWork.append(count).append(",");
                                dayTime.append(sum).append(",");
                            }
                        }
                        if(lengthwork==dayWork.length()-1){
                            Object count = stringObjectMap.get("count");
                            if(count==null){
                                count=0;
                            }
                            dayWork.append(count).append(",");
                            dayTime.append(0).append(",");
                        }
                    }
                }
                if(!name.equals(gzName)){
                    String[] split1 = startDate.split("-");
                    ndate=Integer.parseInt(split1[split1.length-1]);
                    if(time>0&&time<7){
                        for (int i = time; i < 7; i++) {
                            dayWork.append("0,");
                            dayTime.append("0,");
                        }
                        dayWork=dayWork.deleteCharAt(dayWork.length()-1);
                        dayTime=dayTime.deleteCharAt(dayTime.length()-1);
                        banzu.put("gz_id",stringObjectMap.get("proId"));
                        banzu.put("week_work",dayWork.toString());
                        banzu.put("week_time",dayTime.toString());
                        banzu.put("gz_name",gzName);
                        banzulist.add(banzu);
                        time=0;
                        dayWork=new StringBuffer();
                        dayTime=new StringBuffer();
                        banzu = new HashMap<>();
                    }
                    time++;
                    if(klTime.equals(startDate)){
                        String hoursStr = (String) stringObjectMap.get("hoursStr");
                        if(hoursStr==null){
                            hoursStr="0";
                        }
                        String[] strings = hoursStr.split(",");
                        int[] ints = new int[strings.length];
                        for(int i=0;i<strings.length;i++){
                            ints[i] = Integer.parseInt(strings[i]);
                        }
                        int sum = 0;
                        for (int i : ints) {
                            sum+=i;
                        }
                        sum=sum/480;
                        Object count = stringObjectMap.get("count");
                        if(count==null){
                            count=0;
                        }
                        if(klTime.equals(startDate)){
                            dayWork.append(count).append(",");
                            dayTime.append(sum).append(",");
                        }
                    }
                    if(!klTime.equals(startDate)){
                        if(nowdate-ndate>0){
                            int number = nowdate-ndate;
                            for (int j = 0; j <number ; j++) {
                                dayWork.append("0,");
                                dayTime.append("0,");
                            }
                            time+=number-1;
                        }
                        int lengthwork = dayWork.length()-1;
                        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
                        Date date=null;
                        Calendar c = Calendar.getInstance();
                        try {
                            date = new SimpleDateFormat("yy-MM-dd").parse(startDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.setTime(date);
                        int day=c.get(Calendar.DATE);
                        for (int i = 1; i <7 ; i++) {
                            c.set(Calendar.DATE, day + i);
                            Date date1 = null;
                            String dayBefore = sdf.format(c.getTime());
                            try {
                                date1 = sdf.parse(dayBefore);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String dateq = sdf.format(date1);
                            if(klTime.equals(dateq)){
                                String hoursStr = (String) stringObjectMap.get("hoursStr");
                                if(hoursStr==null){
                                    hoursStr="0";
                                }
                                String[] strings = hoursStr.split(",");
                                int[] ints = new int[strings.length];
                                for(int j=0;j<strings.length;j++){
                                    ints[j] = Integer.parseInt(strings[j]);
                                }
                                int sum = 0;
                                for (int num : ints) {
                                    sum+=num;
                                }
                                sum=sum/480;
                                Object count = stringObjectMap.get("count");
                                if(count==null){
                                    count=0;
                                }
                                dayTime.append(sum).append(",");
                                dayWork.append(count).append(",");
                            }
                        }
                        if(lengthwork==dayWork.length()-1){
                            Object count = stringObjectMap.get("count");
                            if(count==null){
                                count=0;
                            }
                            dayTime.append(0).append(",");
                            dayWork.append(count).append(",");
                        }
                    }
                }
                ndate=nowdate;
            }
            gzName= (String) stringObjectMap.get("po_name");
            if(time==7){
                dayWork=dayWork.deleteCharAt(dayWork.length()-1);
                dayTime=dayTime.deleteCharAt(dayTime.length()-1);
                banzu.put("gz_id",stringObjectMap.get("proId"));
                banzu.put("week_work",dayWork.toString());
                banzu.put("week_time",dayTime.toString());
                banzu.put("gz_name",stringObjectMap.get("po_name"));
                banzulist.add(banzu);
            }
            if(lastklTime!=null){
                if(lastklTime.equals(klTime)&&poId1==poId2){
                    if(time>0&&time<7) {
                        for (int i = time; i < 7; i++) {
                            dayWork.append("0,");
                            dayTime.append("0,");
                        }
                        dayWork = dayWork.deleteCharAt(dayWork.length() - 1);
                        dayTime = dayTime.deleteCharAt(dayTime.length() - 1);
                        banzu.put("gz_id", stringObjectMap.get("proId"));
                        banzu.put("week_work", dayWork.toString());
                        banzu.put("week_time", dayTime.toString());
                        banzu.put("gz_name", gzName);
                        banzulist.add(banzu);
                    }
                }
            }
        }
        //查询班组下的总人数
        List<Map<String,Object>> ban = this.getBanZu(projectId);
        int allCount = this.getProjectCount(projectId);
        for (Map<String, Object> banzumap : banzulist) {
            String po_name = (String) banzumap.get("gz_name");
            for (Map<String, Object> map : ban) {
                String po_name2 = (String) map.get("po_name");
                if(po_name.equals(po_name2)){
                    String counts = (String) map.get("counts");
                    if(counts==null){
                        counts="0";
                    }
                    String[] strings = counts.split(",");
                    int[] ints = new int[strings.length];
                    for(int j=0;j<strings.length;j++){
                        ints[j] = Integer.parseInt(strings[j]);
                    }
                    banzumap.put("count",ints.length);
                }
            }
        }
        for (Map<String, Object> map : banzulist) {
            Object count = map.get("count");
            if(count==null){
                map.put("count",0);
            }
        }
        StringBuffer stringBufferWork = new StringBuffer();
        StringBuffer stringBufferTime = new StringBuffer();
        List<Map<String, Object>> listmap = arStatisticsDao.getAllCount(projectId,startDate,endDate);
        String[] split1 = startDate.split("-");
        int ndate1 = Integer.parseInt(split1[split1.length-1]);
        int type =0;
        for (Map<String, Object> map : listmap) {
            String klTime = (String)map.get("times");
            String[] split = klTime.split("-");
            int nowdate = Integer.parseInt(split[split.length-1]);
            if(ndate1!=0){
                if(nowdate-ndate1>=1){
                    int number = nowdate-ndate1;
                    int num=0;
                    if(type==0){
                        num=number;
                    }
                    if(type!=0){
                        num=number-1;
                    }
                    for (int j = 0; j <num ; j++) {
                        stringBufferTime.append("0,");
                        stringBufferWork.append("0,");
                    }
                    String hours = (String) map.get("hours");
                    if(hours==null){
                        hours="0";
                    }
                    String[] strings = hours.split(",");
                    int[] ints = new int[strings.length];
                    for(int j=0;j<strings.length;j++){
                        ints[j] = Integer.parseInt(strings[j]);
                    }
                    int timeCount = 0;
                    for (int i : ints) {
                        timeCount+=i;
                    }
                    stringBufferTime.append(timeCount/480).append(",");
                    String counts = (String) map.get("counts");
                    if(counts==null){
                        counts="0";
                    }
                    String[] strs = counts.split(",");
                    int[] ins = new int[strs.length];
                    for(int j=0;j<strs.length;j++){
                        ins[j] = Integer.parseInt(strs[j]);
                    }
                    stringBufferWork.append(ins.length).append(",");
                }else {
                    String hours = (String) map.get("hours");
                    if(hours==null){
                        hours="0";
                    }
                    String[] strings = hours.split(",");
                    int[] ints = new int[strings.length];
                    for(int j=0;j<strings.length;j++){
                        ints[j] = Integer.parseInt(strings[j]);
                    }
                    int timeCount = 0;
                    for (int i : ints) {
                        timeCount+=i;
                    }
                    stringBufferTime.append(timeCount/480).append(",");
                    String counts = (String) map.get("counts");
                    if(counts==null){
                        counts="0";
                    }
                    String[] strs = counts.split(",");
                    int[] ins = new int[strs.length];
                    for(int j=0;j<strs.length;j++){
                        ins[j] = Integer.parseInt(strs[j]);
                    }
                    stringBufferWork.append(ins.length).append(",");
                }
            }
            if(ndate1==0){
                String hours = (String) map.get("hours");
                if(hours==null){
                    hours="0";
                }
                String[] strings = hours.split(",");
                int[] ints = new int[strings.length];
                for(int j=0;j<strings.length;j++){
                    ints[j] = Integer.parseInt(strings[j]);
                }
                int timeCount = 0;
                for (int i : ints) {
                    timeCount+=i;
                }
                stringBufferTime.append(timeCount/480).append(",");
                String counts = (String) map.get("counts");
                if(counts==null){
                    counts="0";
                }
                String[] strs = counts.split(",");
                int[] ins = new int[strs.length];
                for(int j=0;j<strs.length;j++){
                    ins[j] = Integer.parseInt(strs[j]);
                }
                stringBufferWork.append(ins.length).append(",");
            }
            ndate1=nowdate;
            type=1;
        }
        if(stringBufferWork.length()==0){
            stringBufferWork.append("0,0,0,0,0,0,0,");
            stringBufferTime.append("0,0,0,0,0,0,0,");
        }
        int length = stringBufferWork.length();
        if(length<14){
            for (int i = 0; i < (14-length)/2; i++) {
                stringBufferWork.append("0,");
                stringBufferTime.append("0,");
            }
        }
        stringBufferWork.deleteCharAt(stringBufferWork.length()-1);
        stringBufferTime.deleteCharAt(stringBufferTime.length()-1);
        result.put("week_allwork",stringBufferWork.toString());
        result.put("week_alltime",stringBufferTime.toString());
        result.put("count",allCount);
        result.put("banzu_list",banzulist);
        return result;
    }

    private int getProjectCount(Integer projectId) {
        return arStatisticsDao.getProjectCount(projectId);
    }

    private List<Map<String, Object>> getBanZu(Integer projectId) {
        return arStatisticsDao.getBanZu(projectId);
    }
}
