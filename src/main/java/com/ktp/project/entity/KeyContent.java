package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * KeyContent entity. @author MyEclipse Persistence Tools
 */

/**
 *  获取工种类型
 * 
 * */
@Entity
@Table(name = "key_content")
public class KeyContent {

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
    @Id
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    @Column(name = "key_id")
	public Integer getKeyId() {
		return this.keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}
    @Column(name = "key_name")
	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
    @Column(name = "key_value")
	public String getKeyValue() {
		return this.keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

}