package com.zm.entity;

import java.sql.Timestamp;

/**
 * ProOrganPer entity. @author MyEclipse Persistence Tools
 */

public class ProOrganPer implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer pId;
	private Integer poId;
	private Integer userId;
	private Integer PType;
	private Integer popState;
	private Integer popType;
	private Double popJn;
	private Double popXy;
	private Timestamp popEndtime;
	private Timestamp popIntime;
	private Long popCard;
	private String popPic1;
	private String popPic2;
	private String popPic3;
	private String popPic4;
	private Integer popSgzt;
	private String popPic5;
	private Double faceSim;
	private String zfUid;
	private String popPic6;
	private String popBankid;


	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPoId() {
		return this.poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPType() {
		return this.PType;
	}

	public void setPType(Integer PType) {
		this.PType = PType;
	}

	public Integer getPopState() {
		return this.popState;
	}

	public void setPopState(Integer popState) {
		this.popState = popState;
	}

	public Integer getPopType() {
		return this.popType;
	}

	public void setPopType(Integer popType) {
		this.popType = popType;
	}

	public Double getPopJn() {
		return this.popJn;
	}

	public void setPopJn(Double popJn) {
		this.popJn = popJn;
	}

	public Double getPopXy() {
		return this.popXy;
	}

	public void setPopXy(Double popXy) {
		this.popXy = popXy;
	}

	public Timestamp getPopEndtime() {
		return this.popEndtime;
	}

	public void setPopEndtime(Timestamp popEndtime) {
		this.popEndtime = popEndtime;
	}

	public Timestamp getPopIntime() {
		return this.popIntime;
	}

	public void setPopIntime(Timestamp popIntime) {
		this.popIntime = popIntime;
	}

	public Long getPopCard() {
		return this.popCard;
	}

	public void setPopCard(Long popCard) {
		this.popCard = popCard;
	}

	public String getPopPic1() {
		return this.popPic1;
	}

	public void setPopPic1(String popPic1) {
		this.popPic1 = popPic1;
	}

	public String getPopPic2() {
		return this.popPic2;
	}

	public void setPopPic2(String popPic2) {
		this.popPic2 = popPic2;
	}

	public String getPopPic3() {
		return this.popPic3;
	}

	public void setPopPic3(String popPic3) {
		this.popPic3 = popPic3;
	}

	public String getPopPic4() {
		return this.popPic4;
	}

	public void setPopPic4(String popPic4) {
		this.popPic4 = popPic4;
	}

	public Integer getPopSgzt() {
		return this.popSgzt;
	}

	public void setPopSgzt(Integer popSgzt) {
		this.popSgzt = popSgzt;
	}

	public String getPopPic5() {
		return this.popPic5;
	}

	public void setPopPic5(String popPic5) {
		this.popPic5 = popPic5;
	}

	public Double getFaceSim() {
		return this.faceSim;
	}

	public void setFaceSim(Double faceSim) {
		this.faceSim = faceSim;
	}

	public String getZfUid() {
		return this.zfUid;
	}

	public void setZfUid(String zfUid) {
		this.zfUid = zfUid;
	}

	public String getPopPic6() {
		return this.popPic6;
	}

	public void setPopPic6(String popPic6) {
		this.popPic6 = popPic6;
	}

	public String getPopBankid() {
		return popBankid;
	}

	public void setPopBankid(String popBankid) {
		this.popBankid = popBankid;
	}
}