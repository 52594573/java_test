package com.ktp.project.dto.im;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinHon 2018/8/3
 */
public class GroupDto {

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 群组描述
     */
    private String desc;

    /**
     * 是否是公开群
     */
    private boolean isPublic = true;

    /**
     * 群组成员最大数（包括群主），值为数值类型，默认值200，最大值2000
     */
    private String maxusers = "2000";

    /**
     * 加入群是否需要群主或者群管理员审批
     */
    private boolean membersOnly;

    /**
     * 是否允许群成员邀请别人加入此群
     */
    private boolean allowinvites;

    /**
     * 群主账号
     */
    private String owner;

    /**
     * 邀请加群，被邀请人是否需要确认
     */
    private boolean inviteNeedConfirm;

    /**
     * 群组成员，此属性为可选的，但是如果加了此项，数组元素至少一个（注：群主不需要写入到members里面）
     */
    private List members = new ArrayList();


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(String maxusers) {
        this.maxusers = maxusers;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public boolean isAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(boolean allowinvites) {
        this.allowinvites = allowinvites;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isInviteNeedConfirm() {
        return inviteNeedConfirm;
    }

    public void setInviteNeedConfirm(boolean inviteNeedConfirm) {
        this.inviteNeedConfirm = inviteNeedConfirm;
    }

    public List getMembers() {
        return members;
    }

    public void setMembers(List members) {
        this.members = members;
    }
}
