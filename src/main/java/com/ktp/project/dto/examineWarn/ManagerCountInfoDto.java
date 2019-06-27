package com.ktp.project.dto.examineWarn;

import java.util.ArrayList;
import java.util.List;

public class ManagerCountInfoDto {
    private String title = "管理人员考核预警";
    private String date;//日期
    private Integer projectId;//项目ID
    private String projectInfo;//项目名称+文字（项目考核统计结果：）
    private Integer failNum;//不合格人数
    private Integer failCale;//不合格人数占比
    private String remark = "该项目的项目管理人员考核不及格人数偏高，请您重视。";//说明
    private List<Object> pushIdList;//推送列表
    private List<CheckManagerInfoDto> managerInfos = new ArrayList<>(0);

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public List<Object> getPushIdList() {
        return pushIdList;
    }

    public void setPushIdList(List<Object> pushIdList) {
        this.pushIdList = pushIdList;
    }

    public List<CheckManagerInfoDto> getManagerInfos() {
        return managerInfos;
    }

    public void setManagerInfos(List<CheckManagerInfoDto> managerInfos) {
        this.managerInfos = managerInfos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(String projectInfo) {
        this.projectInfo = projectInfo;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public Integer getFailCale() {
        return failCale;
    }

    public void setFailCale(Integer failCale) {
        this.failCale = failCale;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
