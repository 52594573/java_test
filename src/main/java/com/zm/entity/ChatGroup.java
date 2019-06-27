package com.zm.entity;

/**
 * ChatGroup entity. @author MyEclipse Persistence Tools
 */

public class ChatGroup implements java.io.Serializable {

	// Fields

	private Long id;
	private Integer proOrganId;
	private String name;
	private String notice;
	private Integer recvFlag;
	private Integer tribeType;
	private String masterId;

	// Constructors

	/** default constructor */
	public ChatGroup() {
	}

	/** minimal constructor */
	public ChatGroup(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ChatGroup(Long id, Integer proOrganId, String name, String notice,
			Integer recvFlag, Integer tribeType, String masterId) {
		this.id = id;
		this.proOrganId = proOrganId;
		this.name = name;
		this.notice = notice;
		this.recvFlag = recvFlag;
		this.tribeType = tribeType;
		this.masterId = masterId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getProOrganId() {
		return this.proOrganId;
	}

	public void setProOrganId(Integer proOrganId) {
		this.proOrganId = proOrganId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotice() {
		return this.notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Integer getRecvFlag() {
		return this.recvFlag;
	}

	public void setRecvFlag(Integer recvFlag) {
		this.recvFlag = recvFlag;
	}

	public Integer getTribeType() {
		return this.tribeType;
	}

	public void setTribeType(Integer tribeType) {
		this.tribeType = tribeType;
	}

	public String getMasterId() {
		return this.masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

}