package com.ktp.project.dto;

import java.util.Date;

/**
 * @Author: wuyeming
 * @Date: 2018-12-26 上午 10:29
 */
public class AdvertiseDto {
	/**
	 * 广告id
	 */
	private Integer id;
	/**
	 * 活动名称
	 */
	private String title;
	/**
	 * 活动内容
	 */
	private String content;
	/**
	 * 图片url
	 */
	private String picUrl;
	/**
	 * html url
	 */
	private String htmlUrl;
	/**
	 * 1上线 2下线
	 */
	private Integer state;
	/**
	 * 创建时间
	 */
	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
