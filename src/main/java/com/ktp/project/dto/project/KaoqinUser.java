package com.ktp.project.dto.project;

import java.math.BigDecimal;

public class KaoqinUser {
    private BigDecimal kLongitude;//经度
    private BigDecimal kLatitude;//纬度
    private String kTime;//打卡时间
    private Integer kType;//3上班 4下班
    private String kCard ;//卡号
    private String kJihao;//机号
    private Integer kXsd;//相识度
    private String kPic;//图像

    public BigDecimal getkLongitude() {
        return kLongitude;
    }

    public void setkLongitude(BigDecimal kLongitude) {
        this.kLongitude = kLongitude;
    }

    public BigDecimal getkLatitude() {
        return kLatitude;
    }

    public void setkLatitude(BigDecimal kLatitude) {
        this.kLatitude = kLatitude;
    }

    public String getkTime() {
        return kTime;
    }

    public void setkTime(String kTime) {
        this.kTime = kTime;
    }


    public Integer getkType() {
        return kType;
    }

    public void setkType(Integer kType) {
        this.kType = kType;
    }

    public String getkCard() {
        return kCard;
    }

    public void setkCard(String kCard) {
        this.kCard = kCard;
    }

    public String getkJihao() {
        return kJihao;
    }

    public void setkJihao(String kJihao) {
        this.kJihao = kJihao;
    }

    public Integer getkXsd() {
        return kXsd;
    }

    public void setkXsd(Integer kXsd) {
        this.kXsd = kXsd;
    }

    public String getkPic() {
        return kPic;
    }

    public void setkPic(String kPic) {
        this.kPic = kPic;
    }

    public KaoqinUser() {
    }
}
