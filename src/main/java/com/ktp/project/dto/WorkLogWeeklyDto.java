package com.ktp.project.dto;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkLogWeeklyDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// int(11) NOT NULL

    @Column(name = "pro_id")
    private Integer proId;// int(11) NULL

    @Column(name = "send_uid")
    private Integer sendUid;// int(11) NULL

    @Column(name = "wl_type")
    private Integer wlType;// int(11) NULL类型 ：1.质量检查2.安全事件3.不当行为。4。工人打卡 5.个人签到

    @Column(name = "wl_content")
    private String wlContent;// varchar(4000) NULL

    @Column(name = "wl_star")
    private Integer wlStar;// int(11) NULL评星：1-5星，仅质量检查时有评星

    @Column(name = "wl_yzcd")
    private Integer wlYzcd;// int(11) NULL问题严重程度：1.严重2.普通3.警示 ，仅安全和不当行为时有严重程度

    @Column(name = "in_time")
    private Date inTime;// datetime NULL

    @Column(name = "wl_state")
    private Integer wlState;// int(11) NULL1 正常显示 2.撤销 4 删除

    @Column(name = "wl_lbs_x")
    private BigDecimal wlLbsX;// decimal(17,7) NULL

    @Column(name = "wl_lbs_y")
    private BigDecimal wlLbsY;// decimal(17,7) NULL

    @Column(name = "wl_lbs_name")
    private String wlLbsName;// varchar(255) NULL

    @Column(name = "pw_id")
    private Integer pwId;// int(11) NULL

    @Column(name = "pw_content")
    private String pwContent;// varchar(255) NULL节点描述



    @Column(name = "po_id")
    private Integer po_id;//  班组id

    @Column(name = "po_name")
    private String po_name;// 班组名称




    @Transient
    private String oneLevelPoint="";//一级检查点

    @Transient
    private String twoLevelPoint="";//二级检查点

    @Transient
    private String projectName;//项目名称
    @Transient
    private String workLogPicUrl;//图片URL
    @Transient
    private List<String> workLogPics = new ArrayList<>(0);


    public Integer getPo_id() {
        return po_id;
    }

    public void setPo_id(Integer po_id) {
        this.po_id = po_id;
    }

    public String getPo_name() {
        return po_name;
    }

    public void setPo_name(String po_name) {
        this.po_name = po_name;
    }

    public String getWorkLogPicUrl() {
        return workLogPicUrl;
    }

    public void setWorkLogPicUrl(String workLogPicUrl) {
        this.workLogPicUrl = workLogPicUrl;
    }

    public String getOneLevelPoint() {
        return oneLevelPoint;
    }

    public void setOneLevelPoint(String oneLevelPoint) {
        this.oneLevelPoint = oneLevelPoint;
    }

    public String getTwoLevelPoint() {
        return twoLevelPoint;
    }

    public void setTwoLevelPoint(String twoLevelPoint) {
        this.twoLevelPoint = twoLevelPoint;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<String> getWorkLogPics() {
        return workLogPics;
    }

    public void setWorkLogPics(List<String> workLogPics) {
        this.workLogPics = workLogPics;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getSendUid() {
        return sendUid;
    }

    public void setSendUid(Integer sendUid) {
        this.sendUid = sendUid;
    }

    public Integer getWlType() {
        return wlType;
    }

    public void setWlType(Integer wlType) {
        this.wlType = wlType;
    }

    public String getWlContent() {
        return wlContent;
    }

    public void setWlContent(String wlContent) {
        this.wlContent = wlContent;
    }

    public Integer getWlStar() {
        return wlStar;
    }

    public void setWlStar(Integer wlStar) {
        this.wlStar = wlStar;
    }

    public Integer getWlYzcd() {
        return wlYzcd;
    }

    public void setWlYzcd(Integer wlYzcd) {
        this.wlYzcd = wlYzcd;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Integer getWlState() {
        return wlState;
    }

    public void setWlState(Integer wlState) {
        this.wlState = wlState;
    }

    public BigDecimal getWlLbsX() {
        return wlLbsX;
    }

    public void setWlLbsX(BigDecimal wlLbsX) {
        this.wlLbsX = wlLbsX;
    }

    public BigDecimal getWlLbsY() {
        return wlLbsY;
    }

    public void setWlLbsY(BigDecimal wlLbsY) {
        this.wlLbsY = wlLbsY;
    }

    public String getWlLbsName() {
        return wlLbsName;
    }

    public void setWlLbsName(String wlLbsName) {
        this.wlLbsName = wlLbsName;
    }

    public Integer getPwId() {
        return pwId;
    }

    public void setPwId(Integer pwId) {
        this.pwId = pwId;
    }

    public String getPwContent() {
        return pwContent;
    }

    public void setPwContent(String pwContent) {
        this.pwContent = pwContent;
    }


}
