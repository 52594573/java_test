package com.ktp.project.dto.education;

import com.ktp.project.entity.EducationLabelEntity;

import java.math.BigInteger;
import java.util.List;

public class EducationH5Dto {
    private String vName;
    private String vUrl;
    private String pUrl;//视频封面
    private BigInteger vLearnNum;
    private String vSupplyName;
    private String vContent;
    private BigInteger vLikeNum;
    private BigInteger vCommentNum;

    List<EducationH5CommentDto> comments;
    List<EducationLabelEntity> label;

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvUrl() {
        return vUrl;
    }

    public void setvUrl(String vUrl) {
        this.vUrl = vUrl;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public String getvSupplyName() {
        return vSupplyName;
    }

    public void setvSupplyName(String vSupplyName) {
        this.vSupplyName = vSupplyName;
    }

    public String getvContent() {
        return vContent;
    }

    public void setvContent(String vContent) {
        this.vContent = vContent;
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

    public List<EducationH5CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<EducationH5CommentDto> comments) {
        this.comments = comments;
    }

    public List<EducationLabelEntity> getLabel() {
        return label;
    }

    public void setLabel(List<EducationLabelEntity> label) {
        this.label = label;
    }

    public EducationH5Dto() {
    }
}
