package com.ktp.project.dto.education;

import java.math.BigInteger;

public class EducationListDto {
    private Integer id;
    private String vTypeName;
    private String vName;
    private Integer vLong;
    private BigInteger vLearnNum;
    private BigInteger vLikeNum;
    private BigInteger vCommentNum;
    private String vPictureUrl;
    private BigInteger vLike;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getvTypeName() {
        return vTypeName;
    }

    public void setvTypeName(String vTypeName) {
        this.vTypeName = vTypeName;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public Integer getvLong() {
        return vLong;
    }

    public void setvLong(Integer vLong) {
        this.vLong = vLong;
    }

    public BigInteger getvLearnNum() {
        return vLearnNum;
    }

    public void setvLearnNum(BigInteger vLearnNum) {
        this.vLearnNum = vLearnNum;
    }

    public BigInteger getvLikeNum() {
        return vLikeNum;
    }

    public void setvLikeNum(BigInteger vLikeNum) {
        this.vLikeNum = vLikeNum;
    }

    public BigInteger getvCommentNum() {
        return vCommentNum;
    }

    public void setvCommentNum(BigInteger vCommentNum) {
        this.vCommentNum = vCommentNum;
    }

    public String getvPictureUrl() {
        return vPictureUrl;
    }

    public void setvPictureUrl(String vPictureUrl) {
        this.vPictureUrl = vPictureUrl;
    }

    public BigInteger getvLike() {
        return vLike;
    }

    public void setvLike(BigInteger vLike) {
        this.vLike = vLike;
    }

    public EducationListDto() {
    }
}
