package com.ktp.project.dto.education;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class EducationDetailDto {
    private Integer id;
    private String vTypeName;
    private String vName;
    private BigInteger vLikeNum;
    private BigInteger vLearnNum;
    private String vSupply;
    private String vPictureUrl;
    private String vUrl;
    private BigInteger vLike;
    private String vContent;
    private List<String> vLabel;
    private Integer vLong;

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

    public BigInteger getvLikeNum() {
        return vLikeNum;
    }

    public void setvLikeNum(BigInteger vLikeNum) {
        this.vLikeNum = vLikeNum;
    }

    public BigInteger getvLearnNum() {
        return vLearnNum;
    }

    public void setvLearnNum(BigInteger vLearnNum) {
        this.vLearnNum = vLearnNum;
    }

    public String getvSupply() {
        return vSupply;
    }

    public void setvSupply(String vSupply) {
        this.vSupply = vSupply;
    }

    public String getvPictureUrl() {
        return vPictureUrl;
    }

    public void setvPictureUrl(String vPictureUrl) {
        this.vPictureUrl = vPictureUrl;
    }

    public String getvUrl() {
        return vUrl;
    }

    public void setvUrl(String vUrl) {
        this.vUrl = vUrl;
    }

    public BigInteger getvLike() {
        return vLike;
    }

    public void setvLike(BigInteger vLike) {
        this.vLike = vLike;
    }

    public String getvContent() {
        return vContent;
    }

    public void setvContent(String vContent) {
        this.vContent = vContent;
    }

    public List<String> getvLabel() {
        return vLabel;
    }

    public void setvLabel(List<String> vLabel) {
        this.vLabel = vLabel;
    }

    public Integer getvLong() {
        return vLong;
    }

    public void setvLong(Integer vLong) {
        this.vLong = vLong;
    }

    public EducationDetailDto() {
    }
}
