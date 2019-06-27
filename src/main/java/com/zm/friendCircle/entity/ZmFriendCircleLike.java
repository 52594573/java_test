package com.zm.friendCircle.entity;

import com.zm.entity.UserInfo;

import java.sql.Timestamp;

/**
 * ZmFriendCircleLike entity. @author MyEclipse Persistence Tools
 */

public class ZmFriendCircleLike implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer indexId;
	private Integer userId;
	private Integer index;
	private UserInfo userInfo;

	//2018-10-26 wuyeming
	private Integer status;
	private Timestamp createTime;
	private Integer likeDel;
	
	// Constructors

	/** default constructor */
	public ZmFriendCircleLike() {
	}

	/** full constructor */
	public ZmFriendCircleLike(Integer indexId, Integer userId, Integer index,Integer status, Timestamp createTime) {
		this.indexId = indexId;
		this.userId = userId;
		this.index = index;
		this.status = status;
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

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIndex() {
		return this.index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Integer getStatus() {
		return status;
	}

	public ZmFriendCircleLike setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public ZmFriendCircleLike setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		return this;
	}

	public Integer getLikeDel() {
		return likeDel;
	}

	public ZmFriendCircleLike setLikeDel(Integer likeDel) {
		this.likeDel = likeDel;
		return this;
	}
}