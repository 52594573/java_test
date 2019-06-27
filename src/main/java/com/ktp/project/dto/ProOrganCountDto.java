package com.ktp.project.dto;

import java.util.ArrayList;
import java.util.List;

public class ProOrganCountDto {
    private Integer poId;//班组ID
    private String poName;//班组名称
    private Integer totalNum;//总人数
    private List<WorkerInfo> content = new ArrayList<>(0);

    public Integer getPoId() {
        return poId;
    }

    public ProOrganCountDto setPoId(Integer poId) {
        this.poId = poId;
        return this;
    }

    public String getPoName() {
        return poName;
    }

    public ProOrganCountDto setPoName(String poName) {
        this.poName = poName;
        return this;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public ProOrganCountDto setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
        return this;
    }

    public List<WorkerInfo> getContent() {
        return content;
    }

    public ProOrganCountDto setContent(List<WorkerInfo> content) {
        this.content = content;
        return this;
    }

    public static class WorkerInfo{
        private Integer userId;//用户ID
        private String userName;//用户名
        private Integer userSex;
        private String phone;//用户手机号
        private String gzName;//工种名称
        private Double userStar;//用户星级
        private Integer isLead = 0;//工作类型 0 普通工人 1 班组长
        private String headPhotoUrl;//头像URL

        public Integer getUserSex() {
            return userSex;
        }

        public void setUserSex(Integer userSex) {
            this.userSex = userSex;
        }

        public String getHeadPhotoUrl() {
            return headPhotoUrl;
        }

        public void setHeadPhotoUrl(String headPhotoUrl) {
            this.headPhotoUrl = headPhotoUrl;
        }

        public Integer getIsLead() {
            return isLead;
        }

        public void setIsLead(Integer isLead) {
            this.isLead = isLead;
        }

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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGzName() {
            return gzName;
        }

        public void setGzName(String gzName) {
            this.gzName = gzName;
        }

        public Double getUserStar() {
            return userStar;
        }

        public void setUserStar(Double userStar) {
            this.userStar = userStar;
        }
    }
}
