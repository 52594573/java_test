package com.ktp.project.dto.GZProject;

public class GzAddOrUpdateWorkerDto {

    private String accessNo;//	M	FL32	接入编号	“信息平台”为施工企业分配的接入编号
    private String teamCode;//	M	VL320	班组编码	工程内部的班组唯一编码，如果班组编码已存在则修改班组，使用DES加密
    private String idcard;//	M	VL150	工人身份证号	检查工人是否存在的依据，如果工人已存在则修改，使用DES加密
    private String name;//	M	VL30	工人姓名
    private String sex;//	M	FL1	性别 	M：男 F：女
    private String nation;//	M	VL40	民族
    private String birthday;//	M	FL8	生日	yyyyMMdd
    private String address;//	M	VL400	住址	身份证上的住址
    private String signOrgan;//	M	VL30	签发机关	身份证的签发机关
    private String expiryStart;//	M	FL8	有效期开始日期	身份证有效期开始日期
    private String headImg;//	M	VL400	身份证头像URL	外网能访问的URL路径，“信息平台”会自动抓取
    private String currentAddress;//	M	VL400	常住地址	近期的常住地址
    private String political;//	M	FL1	政治面貌	0：党员 1：团员 2：群众
    private String education;//	M	VL20	文化程度	本科以上、本科、大专、中专、高中、初中及以下
    private String phone;//	M	VL11	手机号
    private String employType;//	M	FL1	用工类型	0：自有工人；1：外聘工人
    private String cardNum;//	M	VL150	工资卡账号	使用DES加密
    private String cardBank;//	M	VL10	工资卡开户行代码	参考附录4.4
    private String workType;//	M	VL32	工种代码	参考附录4.3
    private String techLevel;//	M	FL1	技能水平	0：无(普通)；1：初级工；2：中级工；3：高级工；4：技师；5：高级技师
    private String safetyEdu;//	M	FL1	是否参加安全教育	true: 是；false：否
    private String builderType;//	M	FL1	工人类型	0：建筑工匠；1：建筑产业技术工人；2：其他
    private String doDate;//	M	FL8	进场日期	yyyyMMdd
    private String nativePlace;//	M	VL255	籍贯
    private String  maritalStatus;//	M	FL1	婚姻状况	0：未婚；1：已婚；2：离异；3：丧偶
    private String timestamp;//	M	FL17	时间戳	北京时间精确到毫秒(yyyyMMddHHmmssSSS)
    private String sign;//	M	FL32	签名

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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSignOrgan() {
        return signOrgan;
    }

    public void setSignOrgan(String signOrgan) {
        this.signOrgan = signOrgan;
    }

    public String getExpiryStart() {
        return expiryStart;
    }

    public void setExpiryStart(String expiryStart) {
        this.expiryStart = expiryStart;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmployType() {
        return employType;
    }

    public void setEmployType(String employType) {
        this.employType = employType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getTechLevel() {
        return techLevel;
    }

    public void setTechLevel(String techLevel) {
        this.techLevel = techLevel;
    }

    public String getSafetyEdu() {
        return safetyEdu;
    }

    public void setSafetyEdu(String safetyEdu) {
        this.safetyEdu = safetyEdu;
    }

    public String getBuilderType() {
        return builderType;
    }

    public void setBuilderType(String builderType) {
        this.builderType = builderType;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
