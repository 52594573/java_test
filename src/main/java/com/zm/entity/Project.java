package com.zm.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Project entity. @author MyEclipse Persistence Tools
 */


/**
 *   获取项目信息-->创建群
 * */
public class Project implements java.io.Serializable {

	// Fields

	private Integer id;
	private String PName; //项目名字
  	private Integer PCity;
	private Integer PPrincipal; //负责人ID
	private String PContent; //项目内容->相当于群
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date PBegintime; //预计开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date PEndtime;  //预计竣工时间
	private Date PIntime;
	private String PLogo;  //Logo
	private Integer isDel;
	private Integer PState;//工程状态：1未开始2.进行中3.暂时停工4.已竣工
	private Integer PZb;   //总包id
	private Integer PKfs;  //开发商id
	private Integer zj;

	//2018-10-06 wuyeming
	private String PLbsX;
	private String PLbsY;
	private String PLbsFw;
	private Integer PCreater;
	private Integer PCreateType;
	private String groupId;

	//2018年12月18日14:20:46 lsh
    private String PSent;//判断发送
	private Integer hatchStatus;//否是孵化中心 0 否 1 是
	private String pArea;//省市区
	private String pAddress;//项目详细地址

	public String getpAddress() {
		return pAddress;
	}

	public void setpAddress(String pAddress) {
		this.pAddress = pAddress;
	}

	public String getpArea() {
		return pArea;
	}

	public void setpArea(String pArea) {
		this.pArea = pArea;
	}

	public Integer getHatchStatus() {
		return hatchStatus;
	}

	public void setHatchStatus(Integer hatchStatus) {
		this.hatchStatus = hatchStatus;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPName() {
		return this.PName;
	}

	public void setPName(String PName) {
		this.PName = PName;
	}

	public Integer getPCity() {
		return this.PCity;
	}

	public void setPCity(Integer PCity) {
		this.PCity = PCity;
	}

	public Integer getPPrincipal() {
		return this.PPrincipal;
	}

	public void setPPrincipal(Integer PPrincipal) {
		this.PPrincipal = PPrincipal;
	}

	public String getPContent() {
		return this.PContent;
	}

	public void setPContent(String PContent) {
		this.PContent = PContent;
	}

	public Date getPBegintime() {
		return this.PBegintime;
	}

	public void setPBegintime(Date PBegintime) {
		this.PBegintime = PBegintime;
	}

	public Date getPEndtime() {
		return this.PEndtime;
	}

	public void setPEndtime(Date PEndtime) {
		this.PEndtime = PEndtime;
	}

	public Date getPIntime() {
		return this.PIntime;
	}

	public void setPIntime(Date PIntime) {
		this.PIntime = PIntime;
	}

	public String getPLogo() {
		return this.PLogo;
	}

	public void setPLogo(String PLogo) {
		this.PLogo = PLogo;
	}

	public Integer getIsDel() {
		return this.isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getPState() {
		return this.PState;
	}

	public void setPState(Integer PState) {
		this.PState = PState;
	}

	public Integer getPZb() {
		return this.PZb;
	}

	public void setPZb(Integer PZb) {
		this.PZb = PZb;
	}

	public Integer getPKfs() {
		return this.PKfs;
	}

	public void setPKfs(Integer PKfs) {
		this.PKfs = PKfs;
	}

	public Integer getZj() {
		return this.zj;
	}

	public void setZj(Integer zj) {
		this.zj = zj;
	}

	public String getPLbsX() {
		return PLbsX;
	}

	public Project setPLbsX(String PLbsX) {
		this.PLbsX = PLbsX;
		return this;
	}

	public String getPLbsY() {
		return PLbsY;
	}

	public Project setPLbsY(String PLbsY) {
		this.PLbsY = PLbsY;
		return this;
	}

	public String getPLbsFw() {
		return PLbsFw;
	}

	public Project setPLbsFw(String PLbsFw) {
		this.PLbsFw = PLbsFw;
		return this;
	}

	public Integer getPCreater() {
		return PCreater;
	}

	public Project setPCreater(Integer PCreater) {
		this.PCreater = PCreater;
		return this;
	}

	public Integer getPCreateType() {
		return PCreateType;
	}

	public Project setPCreateType(Integer PCreateType) {
		this.PCreateType = PCreateType;
		return this;
	}

    public String getPSent() {
        return PSent;
    }

    public void setPSent(String PSent) {
        this.PSent = PSent;
    }

    public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}