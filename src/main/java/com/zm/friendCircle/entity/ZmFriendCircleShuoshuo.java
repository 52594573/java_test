package com.zm.friendCircle.entity;

import com.zm.entity.UserInfo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ZmFriendCircleShuoshuo entity. @author MyEclipse Persistence Tools
 */

public class ZmFriendCircleShuoshuo implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userId;
	private String content;
	private Timestamp initTime;
	private Integer vState;
	private String  createDate;
	private int index;
	private UserInfo userInfo;
	private List picList;
	private List likeList;
	private List commentList;

	//2018-11-1 wuyeming
	private Integer type;//说说类型



	public List getPicList() {
		return picList;
	}

	public void setPicList(List picList) {
		this.picList = picList;
	}

	public List getLikeList() {
		return likeList;
	}

	public void setLikeList(List likeList) {
		this.likeList = likeList;
	}

	public List getCommentList() {
		return commentList;
	}

	public void setCommentList(List commentList) {
		this.commentList = commentList;
	}

	/** default constructor */
	public ZmFriendCircleShuoshuo() {
	}

	/** full constructor */
	public ZmFriendCircleShuoshuo(Integer userId, String content,
			Timestamp initTime) {
		this.userId = userId;
		this.content = content;
		this.initTime = initTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getInitTime() {
		return this.initTime;
	}

	public void setInitTime(Timestamp initTime) {
		this.initTime = initTime;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createDate = format.format( new Date( initTime.getTime() ) );
	}

	public Integer getvState() {
		return vState;
	}

	public void setvState(Integer vState) {
		this.vState = vState;
	}

	public int getIndex() {
		return 2;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Integer getType() {
		return type;
	}

	public ZmFriendCircleShuoshuo setType(Integer type) {
		this.type = type;
		return this;
	}
}