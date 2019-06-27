package com.zm.entity;

/**
 * UserSfz entity. @author MyEclipse Persistence Tools
 */

public class UserSfz implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer UId;
	private String usName;
	private Integer usSex;
	private Integer usAge;
	private String usPic;
	private String usNation;
	private String usAddress;
	private String usProvince;
	private String usCity;
	private String usArea;
	private String usOrg;
	private String usStartTime;
	private String usExpireTime;
	private String usBirthYear;
	private String usBirthMonth;
	private String usBirthDay;

	// Constructors

	/** default constructor */
	public UserSfz() {
	}

	/** minimal constructor */
	public UserSfz(Integer UId) {
		this.UId = UId;
	}

	/** full constructor */
	public UserSfz(Integer UId, String usName, Integer usSex, Integer usAge,
			String usPic, String usNation, String usAddress, String usProvince,
			String usCity, String usArea, String usOrg, String usStartTime,
			String usExpireTime, String usBirthYear, String usBirthMonth,
			String usBirthDay) {
		this.UId = UId;
		this.usName = usName;
		this.usSex = usSex;
		this.usAge = usAge;
		this.usPic = usPic;
		this.usNation = usNation;
		this.usAddress = usAddress;
		this.usProvince = usProvince;
		this.usCity = usCity;
		this.usArea = usArea;
		this.usOrg = usOrg;
		this.usStartTime = usStartTime;
		this.usExpireTime = usExpireTime;
		this.usBirthYear = usBirthYear;
		this.usBirthMonth = usBirthMonth;
		this.usBirthDay = usBirthDay;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUId() {
		return this.UId;
	}

	public void setUId(Integer UId) {
		this.UId = UId;
	}

	public String getUsName() {
		return this.usName;
	}

	public void setUsName(String usName) {
		this.usName = usName;
	}

	public Integer getUsSex() {
		return this.usSex;
	}

	public void setUsSex(Integer usSex) {
		this.usSex = usSex;
	}

	public Integer getUsAge() {
		return this.usAge;
	}

	public void setUsAge(Integer usAge) {
		this.usAge = usAge;
	}

	public String getUsPic() {
		return this.usPic;
	}

	public void setUsPic(String usPic) {
		this.usPic = usPic;
	}

	public String getUsNation() {
		return this.usNation;
	}

	public void setUsNation(String usNation) {
		this.usNation = usNation;
	}

	public String getUsAddress() {
		return this.usAddress;
	}

	public void setUsAddress(String usAddress) {
		this.usAddress = usAddress;
	}

	public String getUsProvince() {
		return this.usProvince;
	}

	public void setUsProvince(String usProvince) {
		this.usProvince = usProvince;
	}

	public String getUsCity() {
		return this.usCity;
	}

	public void setUsCity(String usCity) {
		this.usCity = usCity;
	}

	public String getUsArea() {
		return this.usArea;
	}

	public void setUsArea(String usArea) {
		this.usArea = usArea;
	}

	public String getUsOrg() {
		return this.usOrg;
	}

	public void setUsOrg(String usOrg) {
		this.usOrg = usOrg;
	}

	public String getUsStartTime() {
		return this.usStartTime;
	}

	public void setUsStartTime(String usStartTime) {
		this.usStartTime = usStartTime;
	}

	public String getUsExpireTime() {
		return this.usExpireTime;
	}

	public void setUsExpireTime(String usExpireTime) {
		this.usExpireTime = usExpireTime;
	}

	public String getUsBirthYear() {
		return this.usBirthYear;
	}

	public void setUsBirthYear(String usBirthYear) {
		this.usBirthYear = usBirthYear;
	}

	public String getUsBirthMonth() {
		return this.usBirthMonth;
	}

	public void setUsBirthMonth(String usBirthMonth) {
		this.usBirthMonth = usBirthMonth;
	}

	public String getUsBirthDay() {
		return this.usBirthDay;
	}

	public void setUsBirthDay(String usBirthDay) {
		this.usBirthDay = usBirthDay;
	}

}