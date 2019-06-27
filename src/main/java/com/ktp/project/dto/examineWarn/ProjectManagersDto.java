package com.ktp.project.dto.examineWarn;

public class ProjectManagersDto {
    private Integer projectId;
    private String pushUserIds;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getPushUserIds() {
        return pushUserIds;
    }

    public void setPushUserIds(String pushUserIds) {
        this.pushUserIds = pushUserIds;
    }
}
