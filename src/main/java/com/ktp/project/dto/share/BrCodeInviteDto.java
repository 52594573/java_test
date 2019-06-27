package com.ktp.project.dto.share;

import java.math.BigInteger;
import java.util.Date;

public class BrCodeInviteDto {

    private Integer userId;//用户ID
    private String nickName;//昵称
    private String headPortrait;//头像
    private BigInteger totalInviteNum;//邀请总数
    private BigInteger top;//当前排名
    private Date nearTime;

    public Date getNearTime() {
        return nearTime;
    }

    public void setNearTime(Date nearTime) {
        this.nearTime = nearTime;
    }

    public BigInteger getTop() {
        return top;
    }

    public void setTop(BigInteger top) {
        this.top = top;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public BigInteger getTotalInviteNum() {
        return totalInviteNum;
    }

    public void setTotalInviteNum(BigInteger totalInviteNum) {
        this.totalInviteNum = totalInviteNum;
    }
}
