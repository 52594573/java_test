package com.ktp.project.dto.education;

import java.math.BigInteger;
import java.util.Date;

public class EducationCommentDto {
    private Integer userId;
    private BigInteger cNum;//数量
    private String cName;//名字
    private String cUrl;//评论在头像
    private String cContent;//评论内容
    private Date cTime;//评论时间
    private Integer cSex;//性别

    public BigInteger getcNum() {
        return cNum;
    }

    public void setcNum(BigInteger cNum) {
        this.cNum = cNum;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcUrl() {
        return cUrl;
    }

    public void setcUrl(String cUrl) {
        this.cUrl = cUrl;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Integer getcSex() {
        return cSex;
    }

    public void setcSex(Integer cSex) {
        this.cSex = cSex;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public EducationCommentDto() {
    }
}
