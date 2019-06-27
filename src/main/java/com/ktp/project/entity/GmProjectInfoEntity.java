package com.ktp.project.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ktp_project_out")
public class GmProjectInfoEntity {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //企业code
    @Column(name = "corp_code")
    private String corp_code;
    //企业名称
    @Column(name = "corp_name")
    private String corp_name;
    //项目code
    @Column(name = "project_code")
    private String project_code;
    //项目id
    @Column(name = "project_id")
    private Integer project_id;
    //班组Code
    @Column(name = "team_code")
    private String team_code;
    //地区code
    @Column(name = "region_code")
    private String region_code;

    //项目进场时间
    @Column(name = "corp_intime")
    private Date corp_intime;

    public Date getCorp_intime() {
        return corp_intime;
    }

    public void setCorp_intime(Date corp_intime) {
        this.corp_intime = corp_intime;
    }

    public String getTeam_code() {
        return team_code;
    }

    public void setTeam_code(String team_code) {
        this.team_code = team_code;
    }

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorp_code() {
        return corp_code;
    }

    public void setCorp_code(String corp_code) {
        this.corp_code = corp_code;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        this.corp_name = corp_name;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }
}
