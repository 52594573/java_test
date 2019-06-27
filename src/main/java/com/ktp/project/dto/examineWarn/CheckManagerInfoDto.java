package com.ktp.project.dto.examineWarn;

public class CheckManagerInfoDto {
    private String title = "管理人员考核预警";
    private String date;//日期
    private String remark;//说明
    private Integer userId;///用户ID
    private String managerInfo;//管理人员信息
    private Double checkScore;//考核分数
    private String checkRate;//考核等级
    private Integer projectId;
    private String projectName;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getManagerInfo() {
        return managerInfo;
    }

    public void setManagerInfo(String managerInfo) {
        this.managerInfo = managerInfo;
    }

    public Double getCheckScore() {
        return checkScore;
    }

    public void setCheckScore(Double checkScore) {
        this.checkScore = checkScore;
    }

    public String getCheckRate() {
        return checkRate;
    }

    public void setCheckRate(String checkRate) {
        this.checkRate = checkRate;
    }

    public static void main(String[] args) {
        CheckManagerInfoDto infoDto = new CheckManagerInfoDto();
        infoDto.setProjectId(1);
        infoDto.setProjectName("111");
        infoDto.setCheckRate("11111");
        infoDto.setCheckScore(12D);
        infoDto.setDate("2018-12-10");
        infoDto.setManagerInfo("aa-11");
        infoDto.setRemark("哈哈哈");
        infoDto.setTitle("my demo");
    }
}
