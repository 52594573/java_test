package com.ktp.project.dto.GZProject;

public class GZProjectBuildDto {
	public GZProjectBuildDto() {
	}

	private String accessNo;//“信息平台”为施工企业分配的接入编号
	private String leaderAddress;//住址	身份证上的住址
	private String leaderBirthday;//生日	yyyyMMdd
	private String leaderBuilderType;//工人类型	0：建筑工匠；1：建筑产业技术工人；2：其他
	private String leaderCardBank;//银行代码
	private String leaderCardNum;//银行卡号码
	private String leaderCurrentAddress;//常住地址	近期的常住地址
	private String leaderDoDate;//进场日期	yyyyMMdd
	private String leaderEducation;//文化程度	本科以上、本科、大专、中专、高中、初中及以下
	private String leaderEmployType;//用工形式	0：固定工人 1：外聘
	private String leaderExpiryStart;//有效期开始日期	身份证有效期开始日期
	private String leaderHeadImg;//身份证头像URL
	private String leaderIdcard;//工人身份证号
	private String leaderMaritalStatus;//婚姻状况	0：未婚；1：已婚；2：离异；3：丧偶
	private String leaderName;//工人姓名
	private String leaderNation;//民族
	private String leaderNativePlace;//籍贯
	private String leaderPhone;//手机号
	private String leaderPolitical;//政治面貌	0：党员1：团员 2：群众
	private String leaderSafetyEdu;//是否参加安全教育	true: 是；false：否
	private String leaderSex;//性别 M：男 F：女
	private String leaderSignOrgan;//签发机关	身份证的签发机关
	private String leaderTechLevel;//技能水平	0：无(普通)；1：初级工；2：中级工；3：高级工；4：技师；5：高级技师
	private String leaderWorkType;//工种代码
	private String name;//班组命名格式：班组长姓名 + 班组类型 + “班组”
	private String teamCode;//工程内部的班组唯一编码，如果班组编码已存在则修改班组。格式：班组长身份证号 + 班组类型代码，使用DES加密
	private String timestamp;//时间戳	北京时间精确到毫秒(yyyyMMddHHmmssSSS)
	private String workType;//班组类型

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getLeaderIdcard() {
		return leaderIdcard;
	}

	public void setLeaderIdcard(String leaderIdcard) {
		this.leaderIdcard = leaderIdcard;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getLeaderSex() {
		return leaderSex;
	}

	public void setLeaderSex(String leaderSex) {
		this.leaderSex = leaderSex;
	}

	public String getLeaderNation() {
		return leaderNation;
	}

	public void setLeaderNation(String leaderNation) {
		this.leaderNation = leaderNation;
	}

	public String getLeaderBirthday() {
		return leaderBirthday;
	}

	public void setLeaderBirthday(String leaderBirthday) {
		this.leaderBirthday = leaderBirthday;
	}

	public String getLeaderAddress() {
		return leaderAddress;
	}

	public void setLeaderAddress(String leaderAddress) {
		this.leaderAddress = leaderAddress;
	}

	public String getLeaderSignOrgan() {
		return leaderSignOrgan;
	}

	public void setLeaderSignOrgan(String leaderSignOrgan) {
		this.leaderSignOrgan = leaderSignOrgan;
	}

	public String getLeaderExpiryStart() {
		return leaderExpiryStart;
	}

	public void setLeaderExpiryStart(String leaderExpiryStart) {
		this.leaderExpiryStart = leaderExpiryStart;
	}

	public String getLeaderHeadImg() {
		return leaderHeadImg;
	}

	public void setLeaderHeadImg(String leaderHeadImg) {
		this.leaderHeadImg = leaderHeadImg;
	}

	public String getLeaderCurrentAddress() {
		return leaderCurrentAddress;
	}

	public void setLeaderCurrentAddress(String leaderCurrentAddress) {
		this.leaderCurrentAddress = leaderCurrentAddress;
	}

	public String getLeaderPolitical() {
		return leaderPolitical;
	}

	public void setLeaderPolitical(String leaderPolitical) {
		this.leaderPolitical = leaderPolitical;
	}

	public String getLeaderEducation() {
		return leaderEducation;
	}

	public void setLeaderEducation(String leaderEducation) {
		this.leaderEducation = leaderEducation;
	}

	public String getLeaderPhone() {
		return leaderPhone;
	}

	public void setLeaderPhone(String leaderPhone) {
		this.leaderPhone = leaderPhone;
	}

	public String getLeaderEmployType() {
		return leaderEmployType;
	}

	public void setLeaderEmployType(String leaderEmployType) {
		this.leaderEmployType = leaderEmployType;
	}

	public String getLeaderTechLevel() {
		return leaderTechLevel;
	}

	public void setLeaderTechLevel(String leaderTechLevel) {
		this.leaderTechLevel = leaderTechLevel;
	}

	public String getLeaderWorkType() {
		return leaderWorkType;
	}

	public void setLeaderWorkType(String leaderWorkType) {
		this.leaderWorkType = leaderWorkType;
	}

	public String getLeaderSafetyEdu() {
		return leaderSafetyEdu;
	}

	public void setLeaderSafetyEdu(String leaderSafetyEdu) {
		this.leaderSafetyEdu = leaderSafetyEdu;
	}

	public String getLeaderBuilderType() {
		return leaderBuilderType;
	}

	public void setLeaderBuilderType(String leaderBuilderType) {
		this.leaderBuilderType = leaderBuilderType;
	}

	public String getLeaderDoDate() {
		return leaderDoDate;
	}

	public void setLeaderDoDate(String leaderDoDate) {
		this.leaderDoDate = leaderDoDate;
	}

	public String getLeaderNativePlace() {
		return leaderNativePlace;
	}

	public void setLeaderNativePlace(String leaderNativePlace) {
		this.leaderNativePlace = leaderNativePlace;
	}

	public String getLeaderMaritalStatus() {
		return leaderMaritalStatus;
	}

	public void setLeaderMaritalStatus(String leaderMaritalStatus) {
		this.leaderMaritalStatus = leaderMaritalStatus;
	}

	public String getLeaderCardNum() {
		return leaderCardNum;
	}

	public void setLeaderCardNum(String leaderCardNum) {
		this.leaderCardNum = leaderCardNum;
	}

	public String getLeaderCardBank() {
		return leaderCardBank;
	}

	public void setLeaderCardBank(String leaderCardBank) {
		this.leaderCardBank = leaderCardBank;
	}

	public String getAccessNo() {
		return accessNo;
	}

	public void setAccessNo(String accessNo) {
		this.accessNo = accessNo;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
