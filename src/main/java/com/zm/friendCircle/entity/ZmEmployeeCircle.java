package com.zm.friendCircle.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zm.entity.UserInfo;

/**
 * ZmEmployeeCircle entity. @author MyEclipse Persistence Tools
 */

public class ZmEmployeeCircle implements java.io.Serializable {

	// Fields

	private Integer id;
	private String content;
	private Timestamp arriveTime;
	private Timestamp createTime;
	private String  arriveDate;
	private String  createDate;
	private String work;
	private Integer userId;
	private UserInfo userInfo;
	private Integer index;
	
	private List likeList;
	private List commentList;

	// 2018-10-31 wuyeming
	private Integer gzId;


	// Constructors

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
	public ZmEmployeeCircle() {
	}

	/** full constructor */
	public ZmEmployeeCircle(String content, Timestamp arriveTime,
			Timestamp createTime, Integer workId, Integer userId, Integer index) {
		this.content = content;
		this.arriveTime = arriveTime;
		this.createTime = createTime;
		this.userId = userId;
		this.index = index;
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

	public Timestamp getArriveTime() {
		return this.arriveTime;
	}

	public void setArriveTime(Timestamp arriveTime) {
		this.arriveTime = arriveTime;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.arriveDate = format.format( new Date( arriveTime.getTime() )); 
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createDate = format.format( new Date( createTime.getTime() )); 
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

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public Integer getGzId() {
		return gzId;
	}

	public ZmEmployeeCircle setGzId(Integer gzId) {
		this.gzId = gzId;
		return this;
	}
}