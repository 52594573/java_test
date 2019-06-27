package com.ktp.project.entity;


import javax.persistence.*;

@Entity
@Table(name = "ktp_po_out")
public class KtpPoOutEntity {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //返回班组code
    @Column(name = "team_sysNo")
    private String teamSysNo;
    //本项目班组id
    @Column(name = "po_id")
    private Integer poId;
    //标识
    @Column(name = "region_code")
    private String regionCode;

    public KtpPoOutEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamSysNo() {
        return teamSysNo;
    }

    public void setTeamSysNo(String teamSysNo) {
        this.teamSysNo = teamSysNo;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }
}
