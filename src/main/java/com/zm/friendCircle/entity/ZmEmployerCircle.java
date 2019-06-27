package com.zm.friendCircle.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.zm.entity.UserInfo;

/**
 * ZmEmployerCircle entity. @author MyEclipse Persistence Tools
 */

public class ZmEmployerCircle implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userId;
	private String content;
	private String works;
	private String workType; //工种类型
	private Integer amount;
	private String address;
	private String   createDate;
	private Timestamp createTime;
	private Integer index;
	// 2018-10-31 wuyeming
	private Integer gzId;
	private Integer workId;

	private UserInfo userInfo;
	List    likeList;
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
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

	List    commentList;
	
	// Constructors

	/** default constructor */
	public ZmEmployerCircle() {
	}

	/** full constructor */
	public ZmEmployerCircle(Integer userId, String content, String workIds,
			Integer amount, String address, Timestamp createDate, Integer index) {
		this.userId = userId;
		this.content = content;
		this.amount = amount;
		this.address = address;
		this.index = index;
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


	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getIndex() {
		return this.index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getWorks() {
		return works;
	}

	public void setWorks(String works) {
		this.works = works;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.createDate = format.format( new Date( createTime.getTime() )); 
	}

	public Integer getGzId() {
		return gzId;
	}

	public ZmEmployerCircle setGzId(Integer gzId) {
		this.gzId = gzId;
		return this;
	}

	public Integer getWorkId() {
		return workId;
	}

	public ZmEmployerCircle setWorkId(Integer workId) {
		this.workId = workId;
		return this;
	}
}