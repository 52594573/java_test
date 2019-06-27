package com.ktp.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * six_zj表实体
 */
@Entity
@Table(name = "six_zj")
public class SixZj {

	private int id;
	private int proId;
	private String cardId;
	private String pic1;
	private String inImgQiniu;

	public SixZj() {
	}

	public SixZj(int id, int proId, String pic1) {
		this.id = id;
		this.proId = proId;
		this.pic1 = pic1;
	}

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "pro_id")
	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	@Column(name = "card_id")
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Column(name = "pic1")
	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	@Column(name = "in_img_qiniu")
	public String getInImgQiniu() {
		return inImgQiniu;
	}

	public void setInImgQiniu(String inImgQiniu) {
		this.inImgQiniu = inImgQiniu;
	}

	@Override
	public String toString() {
		return "SixZj [id=" + id + ", proId=" + proId + ", pic1=" + pic1 + "]";
	}


}
