package com.ktp.project.dto;


import com.ktp.project.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WorkLogQualityPushDto {

//        标题（质量评分偏低）、	quality_title
//
//    时间（年月日）、				work_log.in_time
//    质量评分、							work_log.wl_star
//    位置、									work_log.wl_lbs_name
//    工作内容、							work_log.wl_content
//    检查人、								user_info.u_realname
//    施工班组（班组长+工种+班组）、		user_info.u_realname + '组' + key_content.key_name + pro_organ.po_name
//    施工人员（工种+姓名）、						key_content.key_name +  user_info.u_realname
//    说明内容（质量评分偏低，请及时整改质量问题。）  work_log.pw_content
//
//            附加字段
//   （项目ID）   projectID
//   （检查人名） checkName
//   （检查人ID） checkNameID
//   （施工人名） workerName
//   （施工人ID） workerNameID
//   （组长名）	 teamerName
//   （组长ID）	 teamerNameID
//   （工种）		 key_name
//   （班组名字） teamName

    private Integer id;//质量评分偏低


    private String title;//质量评分偏低
    private String in_time;//时间（年月日）、
    private long wl_star;//质量 评星
    private String wl_lbs_name;//位置、
    private String wl_content;//工作内容、
    private String checkName;//检查人、
    private String constructionteam;//施工班组
    private String constructionperson;//施工人员
    private String explain;//说明内容

    private int projectID;     //   （项目ID）
    private String projectName;     //   （项目名称）
    private int checkNameID;    //   （检查人ID）
    private String workerName;   //   （施工人名）
    private String workerNameID;     //   （施工人ID）
    private String teamerName;     //   （组长名）
    private int teamerNameID;     //   （组长ID）
    private String key_name;     //   （工种）
    private String teamName;     //   （班组名字）

    private Integer wl_yzcd;//质量评分偏低

    public Integer getWl_yzcd() {
        return wl_yzcd;
    }

    public void setWl_yzcd(Integer wl_yzcd) {
        this.wl_yzcd = wl_yzcd;
    }

    private Integer wl_safe_type;     //   （不安全因素类别）

    public Integer getWl_safe_type() {
        return wl_safe_type;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setWl_safe_type(Integer wl_safe_type) {
        this.wl_safe_type = wl_safe_type;
    }

    //    public WorkLogDetail workLogDetail;
//
//    public WorkLogDetail getWorkLogDetail() {
//        return workLogDetail;
//    }
//
//    public void setWorkLogDetail(WorkLogDetail workLogDetail) {
//        this.workLogDetail = workLogDetail;
//    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public long getWl_star() {
        return wl_star;
    }

    public void setWl_star(long wl_star) {
        this.wl_star = wl_star;
    }

    public String getWl_lbs_name() {
        return wl_lbs_name;
    }

    public void setWl_lbs_name(String wl_lbs_name) {
        this.wl_lbs_name = wl_lbs_name;
    }

    public String getWl_content() {
        return wl_content;
    }

    public void setWl_content(String wl_content) {
        this.wl_content = wl_content;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getConstructionteam() {
        return constructionteam;
    }

    public void setConstructionteam(String constructionteam) {
        this.constructionteam = constructionteam;
    }

    public String getConstructionperson() {
        return constructionperson;
    }

    public void setConstructionperson(String constructionperson) {
        this.constructionperson = constructionperson;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getCheckNameID() {
        return checkNameID;
    }

    public void setCheckNameID(int checkNameID) {
        this.checkNameID = checkNameID;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerNameID() {
        return workerNameID;
    }

    public void setWorkerNameID(String workerNameID) {
        this.workerNameID = workerNameID;
    }

    public String getTeamerName() {
        return teamerName;
    }

    public void setTeamerName(String teamerName) {
        this.teamerName = teamerName;
    }

    public int getTeamerNameID() {
        return teamerNameID;
    }

    public void setTeamerNameID(int teamerNameID) {
        this.teamerNameID = teamerNameID;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
