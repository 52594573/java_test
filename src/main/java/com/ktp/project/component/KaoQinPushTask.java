package com.ktp.project.component;

import com.ktp.project.dto.ProGZKaoQinLvDto;
import com.ktp.project.service.ProjectService;
import com.ktp.project.service.UserService;
import com.ktp.project.util.HuanXinRequestUtils;
import com.zm.entity.ProjectCollectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Author: tangbin
 * @Date: 2018-12-7 下午 13.10
 */

@Component
public class KaoQinPushTask {
    private static final Logger log = LoggerFactory.getLogger(PushTask.class);
    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    //每天下午三点推送  推送项目下的工种出勤率和工人出勤率
    @Scheduled(cron = "0 0 15 * * ?")
    /*@Scheduled(cron = "0 0/1 * * * ?")//每隔一分钟推送*/
    public void taskDo(){
        List<Integer> proIdList = projectService.getProjectId();
        ArrayList<Object> cmdList = new ArrayList<>();
        ArrayList<Object> imList = new ArrayList<>();
        for (Integer proId : proIdList) {
            List<Object> userList = userService.getUserList(proId);
            List<Map<String,Object>> mapList = userService.getAllUserList(proId);
            for (Map<String, Object> objectMap : mapList) {
                Object status = objectMap.get("m_status");
                if(status==null){
                    imList.add(objectMap.get("m_user_id"));
                }
                if(status!=null){
                    int type = (int) status;
                    if(type==0){
                        cmdList.add(objectMap.get("m_user_id"));
                    }
                    if(type!=0){
                       imList.add(objectMap.get("m_user_id"));
                    }
                }
            }
            //在表pro_gz_kaoqinlv中查询对应的数据
            List<ProGZKaoQinLvDto> proGZKaoQinLvDtoList = projectService.getProGZKaoQinLv(proId);
            String projectName = projectService.getProjectName(proId);
            Map<String,Object> extMap = new HashMap<>();
            extMap.put("list",proGZKaoQinLvDtoList);
            extMap.put("type","percentagegz");
            extMap.put("myUserName",projectName);
            extMap.put("projectId",proId);
            SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");//添加不同的参数，显示不同格式的日期
            extMap.put("date",f2.format(new Date()));
            if(cmdList.size()!=0){
                HuanXinRequestUtils.sendMessage("project-"+proId,cmdList,extMap,HuanXinRequestUtils.DEFAULT_MSG,true);
                cmdList.clear();
            }
            if(imList.size()!=0){
                HuanXinRequestUtils.sendMessage("project-"+proId,imList,extMap,HuanXinRequestUtils.DEFAULT_MSG);
                imList.clear();
            }
            if(mapList.size()==0){
                if(userList.size()!=0){
                    HuanXinRequestUtils.sendMessage("project-"+proId,userList,extMap,HuanXinRequestUtils.DEFAULT_MSG);
                }
            }
        }
        cmdList = new ArrayList<>();
        imList = new ArrayList<>();
        List<ProjectCollectId> projectCollectIdList = projectService.getProjectCollectId();
        for (ProjectCollectId projectCollectId : projectCollectIdList) {
            List<Object> userList = userService.getUserList(projectCollectId.getProjectId());
            List<Map<String,Object>> mapList = userService.getAllUserList(projectCollectId.getProjectId());
            for (Map<String, Object> objectMap : mapList) {
                Object status = objectMap.get("m_status");
                if(status!=null){
                    int type = (int) status;
                    if(type!=0){
                        imList.add(objectMap.get("m_user_id"));
                    }
                    if(type==0){
                        cmdList.add(objectMap.get("m_user_id"));
                }
                }
                if(status==null){
                    imList.add(objectMap.get("m_user_id"));
                }
            }
            String projectName = projectService.getProjectName(projectCollectId.getProjectId());
            Map<String ,Object> extMap = new HashMap<>();
            extMap.put("attendanceratio",projectCollectId.getKaoQinLv());
            extMap.put("total",projectCollectId.getUserCount());
            extMap.put("attendance",projectCollectId.getWorkCount());
            extMap.put("myUserName",projectName);
            extMap.put("projectId",projectCollectId.getId());
            extMap.put("type","attendancePercentLowWarning");
            SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");//添加不同的参数，显示不同格式的日期
            extMap.put("date",f2.format(new Date()));
            if(cmdList.size()!=0){
                /*cmdList.add(47732);
                cmdList.add(48864);
                cmdList.add(47714);
                cmdList.add(47327);*/
                HuanXinRequestUtils.sendMessage("project-"+projectCollectId.getProjectId(),cmdList,extMap,HuanXinRequestUtils.DEFAULT_MSG,true);
                cmdList.clear();
            }
            if(imList.size()!=0){
                /*imList.add(47732);
                imList.add(48864);
                imList.add(47714);
                imList.add(47327);*/
                HuanXinRequestUtils.sendMessage("project-"+projectCollectId.getProjectId(),imList,extMap,HuanXinRequestUtils.DEFAULT_MSG);
                imList.clear();
            }
            if(mapList.size()==0){
                if(userList.size()!=0){
                    HuanXinRequestUtils.sendMessage("project-"+projectCollectId.getProjectId(),userList,extMap,HuanXinRequestUtils.DEFAULT_MSG);
                }
            }
        }
    }

    @Scheduled( initialDelay = 1*1000L, fixedDelay = 60 * 60 *24 *30 * 1000L)
    public void toTask(){
        userService.updateByInitTime();
    }
}