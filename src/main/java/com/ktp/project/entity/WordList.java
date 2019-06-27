package com.ktp.project.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "word_list")
public class WordList {

    private Integer id;
    private String wlTitle;
    private String wlTitle2;
    private Integer wlSort;
    private String wlContent;
    private String wlPic;
    private String wlSource;
    private String wlAuthor;
    private Integer wlSysuid;
    private Date wlIntime;
    private Date wlLasttime;
    private Integer wlProid;
    private Integer wlUserid;
    private Date wlUserintime;
    private String wlKey;
    private Integer isDel;
    private Integer wlStatus;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "wl_title")
    public String getWlTitle() {
        return wlTitle;
    }

    public void setWlTitle(String wlTitle) {
        this.wlTitle = wlTitle;
    }
    @Column(name = "wl_title2")
    public String getWlTitle2() {
        return wlTitle2;
    }

    public void setWlTitle2(String wlTitle2) {
        this.wlTitle2 = wlTitle2;
    }
    @Column(name = "wl_sort")
    public Integer getWlSort() {
        return wlSort;
    }

    public void setWlSort(Integer wlSort) {
        this.wlSort = wlSort;
    }
    @Column(name = "wl_content")
    public String getWlContent() {
        return wlContent;
    }

    public void setWlContent(String wlContent) {
        this.wlContent = wlContent;
    }
    @Column(name = "wl_pic")
    public String getWlPic() {
        return wlPic;
    }

    public void setWlPic(String wlPic) {
        this.wlPic = wlPic;
    }
    @Column(name = "wl_source")
    public String getWlSource() {
        return wlSource;
    }

    public void setWlSource(String wlSource) {
        this.wlSource = wlSource;
    }
    @Column(name = "wl_author")
    public String getWlAuthor() {
        return wlAuthor;
    }

    public void setWlAuthor(String wlAuthor) {
        this.wlAuthor = wlAuthor;
    }
    @Column(name = "wl_sysuid")
    public Integer getWlSysuid() {
        return wlSysuid;
    }

    public void setWlSysuid(Integer wlSysuid) {
        this.wlSysuid = wlSysuid;
    }
    @Column(name = "wl_intime")
    public Date getWlIntime() {
        return wlIntime;
    }

    public void setWlIntime(Date wlIntime) {
        this.wlIntime = wlIntime;
    }
    @Column(name = "wl_lasttime")
    public Date getWlLasttime() {
        return wlLasttime;
    }

    public void setWlLasttime(Date wlLasttime) {
        this.wlLasttime = wlLasttime;
    }
    @Column(name = "wl_proid")
    public Integer getWlProid() {
        return wlProid;
    }

    public void setWlProid(Integer wlProid) {
        this.wlProid = wlProid;
    }
    @Column(name = "wl_userid")
    public Integer getWlUserid() {
        return wlUserid;
    }

    public void setWlUserid(Integer wlUserid) {
        this.wlUserid = wlUserid;
    }
    @Column(name = "wl_userintime")
    public Date getWlUserintime() {
        return wlUserintime;
    }

    public void setWlUserintime(Date wlUserintime) {
        this.wlUserintime = wlUserintime;
    }
    @Column(name = "wl_key")
    public String getWlKey() {
        return wlKey;
    }

    public void setWlKey(String wlKey) {
        this.wlKey = wlKey;
    }
    @Column(name = "wl_del")
    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    @Column(name = "wl_status")
    public Integer getWlStatus() {
        return wlStatus;
    }

    public void setWlStatus(Integer wlStatus) {
        this.wlStatus = wlStatus;
    }

    public WordList() {
    }
}
