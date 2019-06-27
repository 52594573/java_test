package com.zm.friendCircle.entity;

/**
 * ZmFriendCircleAlbum entity. @author MyEclipse Persistence Tools
 */

public class ZmFriendCircleAlbum implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer shuoshuoId;
	private String picUrl;
	private Integer userId;
	private Integer index;

	//2018-10-26 wuyeming
    private Integer type;

	// Constructors

	/** default constructor */
	public ZmFriendCircleAlbum() {
	}

	/** full constructor */
	public ZmFriendCircleAlbum(Integer shuoshuoId, String picUrl,
			Integer userId, Integer index) {
		this.shuoshuoId = shuoshuoId;
		this.picUrl = picUrl;
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

	public Integer getShuoshuoId() {
		return this.shuoshuoId;
	}

	public void setShuoshuoId(Integer shuoshuoId) {
		this.shuoshuoId = shuoshuoId;
	}

	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
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

	public Integer getType() {
		return type;
	}

	public ZmFriendCircleAlbum setType(Integer type) {
		this.type = type;
		return this;
	}
}