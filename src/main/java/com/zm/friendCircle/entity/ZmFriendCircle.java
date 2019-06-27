package com.zm.friendCircle.entity;

import java.sql.Timestamp;

/**
 * ZmFriendCircle entity. @author MyEclipse Persistence Tools
 */

public class ZmFriendCircle implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer indexId;
	private Integer index;
	private Integer userId;
	// Constructors

	//2018-10-18 wuyeming
	private Integer status;
	//2018-10-26 wuyeming
	private Timestamp operatTime;
	//2018-11-12 wuyeming
	private Integer circleDel;

	/** default constructor */
	public ZmFriendCircle() {
	}

	/** full constructor */
	public ZmFriendCircle(Integer indexId, Integer index) {
		this.indexId = indexId;
		this.index = index;
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

	public Integer getIndex() {
		return this.index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getOperatTime() {
		return operatTime;
	}

	public void setOperatTime(Timestamp operatTime) {
		this.operatTime = operatTime;
	}

	public Integer getCircleDel() {
		return circleDel;
	}

	public void setCircleDel(Integer circleDel) {
		this.circleDel = circleDel;
	}
}