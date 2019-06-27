package com.ktp.project.dto.AuthRealName;

import java.util.Date;

public class GzProOrganPerDto {

    private Integer projectId;//项目ID
    private String projectName;//项目名称
    private Integer proOrganId;//班组ID
    private String proOrganName;//班组名称
    private Integer userId;//用户ID
    private String name;//	M	VL30	工人姓名
    private Integer age;//年龄
    private String phone;//	M	VL11	手机号
    private String idCard;//	M	VL150	工人身份证号	检查工人是否存在的依据，如果工人已存在则修改，使用DES加密
    private Integer sex;//	M	FL1	性别 	M：男 F：女
    private String nation;//	M	VL40	民族
    private String birthday;//	M	FL8	生日	yyyyMMdd
    private String address;//	M	VL400	住址	身份证上的住址
    private String headImg;//	M	VL400	身份证头像URL	外网能访问的URL路径，“信息平台”会自动抓取
    private Date birthdayDate;

    public Date getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProOrganId() {
        return proOrganId;
    }

    public void setProOrganId(Integer proOrganId) {
        this.proOrganId = proOrganId;
    }

    public String getProOrganName() {
        return proOrganName;
    }

    public void setProOrganName(String proOrganName) {
        this.proOrganName = proOrganName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

}
