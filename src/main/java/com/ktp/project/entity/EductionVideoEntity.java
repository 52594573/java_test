package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class EductionVideoEntity {
    private Integer id;
    private Integer vTypeId;
    private String vName;
    private Integer vLong;
    private String vPictureUrl;
    private String vUrl;
    private String vSupply;
    private Integer vBuildId;
    private Date vChangeTime;
    private Date vBuildTiem;
    private String vContent;
    private Integer vStatus;
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "v_type_id")
    public Integer getvTypeId() {
        return vTypeId;
    }

    public void setvTypeId(Integer vTypeId) {
        this.vTypeId = vTypeId;
    }
    @Column(name = "v_name")
    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }
    @Column(name = "v_long")
    public Integer getvLong() {
        return vLong;
    }

    public void setvLong(Integer vLong) {
        this.vLong = vLong;
    }
    @Column(name = "v_picture_url")
    public String getvPictureUrl() {
        return vPictureUrl;
    }

    public void setvPictureUrl(String vPictureUrl) {
        this.vPictureUrl = vPictureUrl;
    }
    @Column(name = "v_url")
    public String getvUrl() {
        return vUrl;
    }

    public void setvUrl(String vUrl) {
        this.vUrl = vUrl;
    }
    @Column(name = "v_supply")
    public String getvSupply() {
        return vSupply;
    }

    public void setvSupply(String vSupply) {
        this.vSupply = vSupply;
    }
    @Column(name = "v_build_id")
    public Integer getvBuildId() {
        return vBuildId;
    }

    public void setvBuildId(Integer vBuildId) {
        this.vBuildId = vBuildId;
    }
    @Column(name = "v_change_time")
    public Date getvChangeTime() {
        return vChangeTime;
    }

    public void setvChangeTime(Date vChangeTime) {
        this.vChangeTime = vChangeTime;
    }
    @Column(name = "v_build_time")
    public Date getvBuildTiem() {
        return vBuildTiem;
    }

    public void setvBuildTiem(Date vBuildTiem) {
        this.vBuildTiem = vBuildTiem;
    }
    @Column(name = "v_status")
    public Integer getvStatus() {
        return vStatus;
    }

    public void setvStatus(Integer vStatus) {
        this.vStatus = vStatus;
    }
    @Column(name = "v_content")
    public String getvContent() {
        return vContent;
    }

    public void setvContent(String vContent) {
        this.vContent = vContent;
    }

    public EductionVideoEntity() {
    }
}
