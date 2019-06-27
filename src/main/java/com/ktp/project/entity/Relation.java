package com.ktp.project.entity;

/**
 * 关系
 *
 * @author djcken
 * @date 2018/7/28
 */
public class Relation {

    private boolean isFriend;
    private boolean isWorkerFriend;
    private boolean isIgnoreMsg;
    private boolean isIgnoreFriendMsg;
    private String u_pic;
    private String u_realname;
    private String u_nicheng;
    private int u_sex;

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isWorkerFriend() {
        return isWorkerFriend;
    }

    public void setWorkerFriend(boolean workerFriend) {
        isWorkerFriend = workerFriend;
    }

    public String getU_pic() {
        return u_pic;
    }

    public void setU_pic(String u_pic) {
        this.u_pic = u_pic;
    }

    public String getU_realname() {
        return u_realname;
    }

    public void setU_realname(String u_realname) {
        this.u_realname = u_realname;
    }

    public String getU_nicheng() {
        return u_nicheng;
    }

    public void setU_nicheng(String u_nicheng) {
        this.u_nicheng = u_nicheng;
    }

    public int getU_sex() {
        return u_sex;
    }

    public void setU_sex(int u_sex) {
        this.u_sex = u_sex;
    }

    public boolean isIgnoreMsg() {
        return isIgnoreMsg;
    }

    public void setIgnoreMsg(boolean ignoreMsg) {
        isIgnoreMsg = ignoreMsg;
    }

    public boolean isIgnoreFriendMsg() {
        return isIgnoreFriendMsg;
    }

    public void setIgnoreFriendMsg(boolean ignoreFriendMsg) {
        isIgnoreFriendMsg = ignoreFriendMsg;
    }
}
