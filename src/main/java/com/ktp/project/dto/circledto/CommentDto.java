package com.ktp.project.dto.circledto;

import java.sql.Timestamp;

/**
 * @Author: wuyeming
 * @Date: 2018-11-20 下午 18:07
 */
public class CommentDto {
    private Integer id;
    private String comment;
    private Integer commentDel;
    private Timestamp createTime;
    private Integer fromUserId;
    private String fromUserName;
    private Integer fromUserSex;
    private String fromUserPic;
    private Integer toUserId;
    private String toUserName;
    private String toUserPic;
    private Integer toUserSex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCommentDel() {
        return commentDel;
    }

    public void setCommentDel(Integer commentDel) {
        this.commentDel = commentDel;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getFromUserSex() {
        return fromUserSex;
    }

    public void setFromUserSex(Integer fromUserSex) {
        this.fromUserSex = fromUserSex;
    }

    public String getFromUserPic() {
        return fromUserPic;
    }

    public void setFromUserPic(String fromUserPic) {
        this.fromUserPic = fromUserPic;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserPic() {
        return toUserPic;
    }

    public void setToUserPic(String toUserPic) {
        this.toUserPic = toUserPic;
    }

    public Integer getToUserSex() {
        return toUserSex;
    }

    public void setToUserSex(Integer toUserSex) {
        this.toUserSex = toUserSex;
    }
}
