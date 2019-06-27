package com.ktp.project.dto.circledto;

import com.zm.entity.UserInfo;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: wuyeming
 * @Date: 2018-11-20 下午 17:53
 */
public class LikeDto {
    private Integer id;
    private Integer likeDel;
    private Timestamp createTime;
    private Integer userId;
    private String userName;
    private String uPic;
    private Integer uSex;
    private UserInfo userInfo;//下次发版需要去掉

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLikeDel() {
        return likeDel;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public LikeDto setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setLikeDel(Integer likeDel) {
        this.likeDel = likeDel;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
