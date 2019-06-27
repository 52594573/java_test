package com.zm.friendCircle.entity;

/**
 * ZmRefusedLook entity. @author MyEclipse Persistence Tools
 */

public class ZmRefusedLook implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer fromUserId;
	private Integer index;
	private Integer indexId;
	private Integer toUserId;
	private Integer operator;

	// Constructors

	/** default constructor */
	public ZmRefusedLook() {
		
	}

	/** full constructor */
	public ZmRefusedLook(Integer fromUserId, Integer index, Integer indexId,
			Integer toUserId,Integer operator) {
		this.fromUserId = fromUserId;
		this.index = index;
		this.indexId = indexId;
		this.toUserId = toUserId;
		this.operator = operator;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromUserId() {
		return this.fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Integer getIndex() {
		return this.index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getIndexId() {
		return this.indexId;
	}

	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}

	public Integer getToUserId() {
		return this.toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getOperator() {
		return operator;
	}

	public ZmRefusedLook setOperator(Integer operator) {
		this.operator = operator;
		return this;
	}
}