package com.ktp.project.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-10-26 下午 13:51
 */
public class LikeCommentDto {

    private Integer id;
    private Integer userId;
    private Integer tIndex;
    private String comment;
    private Integer fromUserId;
    private String uRealname;
    private String content;
    private Object type;
    private Timestamp createTime;
    private Integer indexId;
    private List picList;
    private String uPic;
    private Integer uSex;
    private Object commentDel;

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

    public Integer gettIndex() {
        return tIndex;
    }

    public void settIndex(Integer tIndex) {
        this.tIndex = tIndex;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getuRealname() {
        return uRealname;
    }

    public void setuRealname(String uRealname) {
        this.uRealname = uRealname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    public List getPicList() {
        return picList;
    }

    public void setPicList(List picList) {
        this.picList = picList;
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

    public Object getCommentDel() {
        return commentDel;
    }

    public void setCommentDel(Object commentDel) {
        this.commentDel = commentDel;
    }
}
