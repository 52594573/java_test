package com.ktp.project.dto.share;

/**
 * 分享内容
 */
public class ShareContentDto {

    private Integer userId;//用户ID
    private String userName;//分享人姓名
    private String userHeadPic;//用户头像
    private String projectName;//项目名称

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadPic() {
        return userHeadPic;
    }

    public void setUserHeadPic(String userHeadPic) {
        this.userHeadPic = userHeadPic;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
