package com.ktp.project.dto.education;

import java.util.Date;

public class EducationH5CommentDto {
    private String uUrl;
    private String uName;
    private String uComment;
    private String uTime;
    private Integer uSex;

    public Integer getuSex() {
        return uSex;
    }

    public void setuSex(Integer uSex) {
        this.uSex = uSex;
    }

    public String getuUrl() {
        return uUrl;
    }

    public void setuUrl(String uUrl) {
        this.uUrl = uUrl;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuComment() {
        return uComment;
    }

    public void setuComment(String uComment) {
        this.uComment = uComment;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }

    public EducationH5CommentDto() {
    }
}
