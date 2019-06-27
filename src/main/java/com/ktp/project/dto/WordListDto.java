package com.ktp.project.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class WordListDto {

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
    private String wlUserName;
    private Integer isDel;
    private String uNiCheng;
    private String uPhone;
    private String uRealname;
    private String userNiCheng;
    private String userPhone;
    private String userRealname;
    private String province;//省
    private String city;//市
    private String area;//区
    private Integer sort;//排序
    private Integer upUserid;//修改人id
    private String url;//跳转链接地址
    private Integer isEffective;//链接地址是否有效
    private String wsName;//公告分类

    public String getWsName() {
        return wsName;
    }

    public void setWsName(String wsName) {
        this.wsName = wsName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getUpUserid() {
        return upUserid;
    }

    public void setUpUserid(Integer upUserid) {
        this.upUserid = upUserid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWlTitle() {
        return wlTitle;
    }

    public void setWlTitle(String wlTitle) {
        this.wlTitle = wlTitle;
    }

    public String getWlTitle2() {
        return wlTitle2;
    }

    public void setWlTitle2(String wlTitle2) {
        this.wlTitle2 = wlTitle2;
    }

    public Integer getWlSort() {
        return wlSort;
    }

    public void setWlSort(Integer wlSort) {
        this.wlSort = wlSort;
    }

    public String getWlContent() {
        return wlContent;
    }

    public void setWlContent(String wlContent) {
        this.wlContent = wlContent;
    }

    public String getWlPic() {
        return wlPic;
    }

    public void setWlPic(String wlPic) {
        this.wlPic = wlPic;
    }

    public String getWlSource() {
        return wlSource;
    }

    public void setWlSource(String wlSource) {
        this.wlSource = wlSource;
    }

    public String getWlAuthor() {
        return wlAuthor;
    }

    public void setWlAuthor(String wlAuthor) {
        this.wlAuthor = wlAuthor;
    }

    public Integer getWlSysuid() {
        return wlSysuid;
    }

    public void setWlSysuid(Integer wlSysuid) {
        this.wlSysuid = wlSysuid;
    }

    public Date getWlIntime() {
        return wlIntime;
    }

    public void setWlIntime(Date wlIntime) {
        this.wlIntime = wlIntime;
    }

    public Date getWlLasttime() {
        return wlLasttime;
    }

    public void setWlLasttime(Date wlLasttime) {
        this.wlLasttime = wlLasttime;
    }

    public Integer getWlProid() {
        return wlProid;
    }

    public void setWlProid(Integer wlProid) {
        this.wlProid = wlProid;
    }

    public Integer getWlUserid() {
        return wlUserid;
    }

    public void setWlUserid(Integer wlUserid) {
        this.wlUserid = wlUserid;
    }

    public Date getWlUserintime() {
        return wlUserintime;
    }

    public void setWlUserintime(Date wlUserintime) {
        this.wlUserintime = wlUserintime;
    }

    public String getWlKey() {
        return wlKey;
    }

    public void setWlKey(String wlKey) {
        this.wlKey = wlKey;
    }

    public String getWlUserName() {
        return wlUserName;
    }

    public void setWlUserName(String wlUserName) {
        this.wlUserName = wlUserName;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getuNiCheng() {
        return uNiCheng;
    }

    public void setuNiCheng(String uNiCheng) {
        this.uNiCheng = uNiCheng;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuRealname() {
        return uRealname;
    }

    public void setuRealname(String uRealname) {
        this.uRealname = uRealname;
    }

    public String getUserNiCheng() {
        return userNiCheng;
    }

    public void setUserNiCheng(String userNiCheng) {
        this.userNiCheng = userNiCheng;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public WordListDto() {
    }
}
