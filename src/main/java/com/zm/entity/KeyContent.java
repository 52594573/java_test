package com.zm.entity;

import javax.persistence.Entity;

/**
 * KeyContent entity. @author MyEclipse Persistence Tools
 */

/**
 *  获取工种类型
 * 
 * */
@Entity
public class KeyContent implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer keyId;
	private String keyName;
	private String keyValue;

	// Constructors

	/** default constructor */
	public KeyContent() {
	}

	/** full constructor */
	public KeyContent(Integer keyId, String keyName, String keyValue) {
		this.keyId = keyId;
		this.keyName = keyName;
		this.keyValue = keyValue;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKeyId() {
		return this.keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return this.keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

}