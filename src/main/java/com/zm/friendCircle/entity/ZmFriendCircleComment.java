package com.zm.friendCircle.entity;

import com.zm.entity.UserInfo;

import java.sql.Timestamp;

/**
 * ZmFriendCircleComment entity. @author MyEclipse Persistence Tools
 */

public class ZmFriendCircleComment implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer indexId;
	private Integer fromUserId;
	private Integer toUserId;
	private String comment;
	private Integer index;
	
	private UserInfo fromUserInfo;
	private UserInfo toUserInfo;

	//2018-10-26 wuyeming
	private Integer status;
	private Timestamp createTime;
	//2018-11-12 wuyeming
	private Integer auditsStatus;
	private Integer commentDel;

	// Constructors

	/** default constructor */
	public ZmFriendCircleComment() {
	}

	/** full constructor */
	public ZmFriendCircleComment(Integer indexId, Integer fromUserId,
			Integer toUserId, String comment, Integer index, Integer status, Timestamp createTime) {
		this.indexId = indexId;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.comment = comment;
		this.index = index;
		this.createTime = createTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndexId() {
		return this.indexId;
	}

	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}

	public Integer getFromUserId() {
		return this.fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Integer getToUserId() {
		return this.toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getIndex() {
		return this.index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public UserInfo getFromUserInfo() {
		return fromUserInfo;
	}

	public void setFromUserInfo(UserInfo fromUserInfo) {
		this.fromUserInfo = fromUserInfo;
	}

	public UserInfo getToUserInfo() {
		return toUserInfo;
	}

	public void setToUserInfo(UserInfo toUserInfo) {
		this.toUserInfo = toUserInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getAuditsStatus() {
		return auditsStatus;
	}

	public void setAuditsStatus(Integer auditsStatus) {
		this.auditsStatus = auditsStatus;
	}

	public Integer getCommentDel() {
		return commentDel;
	}

	public void setCommentDel(Integer commentDel) {
		this.commentDel = commentDel;
	}
}