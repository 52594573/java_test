package com.zm.friendCircle.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zm.entity.UserInfo;

/**
 * ZmNetworkArticle entity. @author MyEclipse Persistence Tools
 */

public class ZmNetworkArticle implements java.io.Serializable {

	// Fields

	private Integer id;
	private String content;
	private String url;
	private Timestamp initTime;
	private String  createDate;
	private Integer userId;
	

	private int index;
	private String summary;
	private String picUrl;
	
//	private Map<String,Object> userInfo;
	private UserInfo userInfo;
	private List likeList;
	private List commentList;
	
	
	
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	// Constructors

	/** default constructor */
	public ZmNetworkArticle() {
	}

	/** full constructor */
	public ZmNetworkArticle(String content, String url, Timestamp initTime,
			Integer userId) {
		this.content = content;
		this.url = url;
		this.initTime = initTime;
		this.userId = userId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Timestamp getInitTime() {
		return this.initTime;
	}

	public void setInitTime(Timestamp initTime) {
		this.initTime = initTime;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createDate = format.format( new Date( initTime.getTime() )); 
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getIndex() {
		return index;
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

}