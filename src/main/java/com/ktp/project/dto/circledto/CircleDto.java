package com.ktp.project.dto.circledto;

import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-20 下午 18:15
 */
public class CircleDto {
    private Integer id;
    private Integer userId;
    private String userName;
    private String uPic;
    private Integer uSex;
    private Integer jf;
    private Object jnf;
    private Object xyf;
    private String content;
    private String works;
    private Object amount;
    private String address;
    private String createDate;
    private String arriveTime;
    private Object tIndex;
    private String workType;
    private String url;
    private String summary;
    private String picUrl;
    private Object gzId;
    private String gzName;
    private Object workId;
    private Integer circleDel;
    private List picList;
    private List<LikeDto> likeList;
    private List<CommentDto> commentList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic;
    }

    public Integer getuSex() {
        return uSex;
    }

    public void setuSex(Integer uSex) {
        this.uSex = uSex;
    }

    public Integer getJf() {
        return jf;
    }

    public void setJf(Integer jf) {
        this.jf = jf;
    }

    public Object getJnf() {
        return jnf;
    }

    public void setJnf(Object jnf) {
        this.jnf = jnf;
    }

    public Object getXyf() {
        return xyf;
    }

    public void setXyf(Object xyf) {
        this.xyf = xyf;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWorks() {
        return works;
    }

    public void setWorks(String works) {
        this.works = works;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Object gettIndex() {
        return tIndex;
    }

    public void settIndex(Object tIndex) {
        this.tIndex = tIndex;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Object getGzId() {
        return gzId;
    }

    public void setGzId(Object gzId) {
        this.gzId = gzId;
    }

    public String getGzName() {
        return gzName;
    }

    public void setGzName(String gzName) {
        this.gzName = gzName;
    }

    public Object getWorkId() {
        return workId;
    }

    public void setWorkId(Object workId) {
        this.workId = workId;
    }

    public Integer getCircleDel() {
        return circleDel;
    }

    public void setCircleDel(Integer circleDel) {
        this.circleDel = circleDel;
    }

    public List getPicList() {
        return picList;
    }

    public void setPicList(List picList) {
        this.picList = picList;
    }

    public List<LikeDto> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeDto> likeList) {
        this.likeList = likeList;
    }

    public List<CommentDto> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDto> commentList) {
        this.commentList = commentList;
    }
}
