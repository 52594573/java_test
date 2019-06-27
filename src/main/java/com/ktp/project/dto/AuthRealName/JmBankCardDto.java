package com.ktp.project.dto.AuthRealName;

import java.util.ArrayList;
import java.util.List;

public class JmBankCardDto {

    private Integer createType=1;//	M	FL1	数字int类型	开户类型。1新开户；2已有账户
    private String payRollBankCardNumber;//	M	VL150	工资卡号	发放工资银行卡号使用DES加密。
    private String payRollBankName;//	M	VL100	工资卡开户行	发放工资开户行具体名称请看参考表的银行类型表
    private String payBankName="8888";//	M	VL100		开户行支行名称
    private String bankLinkNumber="8888";//	M	VL150	银行联号	工资卡银行联号
    private String userName;//	M	VL150	账户名	工资卡账户名
    private String phone;//	O	VL11	预留手机号	工资卡预留手机号
    private List<String> bankPic = new ArrayList<>(0);//	M	List	List<string>类型	开户凭证url。最多3张图片。外网能访问的URL路径，“监管系统”会自动抓取。使用DES加密
    private String personName;//	M	VL40	经手人姓名
    private String personIdentity;//	M	VL150	经手人身份证号	使用DES加密
    private String bankPics;
    private Integer projectId;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getBankPics() {
        return bankPics;
    }

    public void setBankPics(String bankPics) {
        this.bankPics = bankPics;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public String getPayRollBankCardNumber() {
        return payRollBankCardNumber;
    }

    public void setPayRollBankCardNumber(String payRollBankCardNumber) {
        this.payRollBankCardNumber = payRollBankCardNumber;
    }

    public String getPayRollBankName() {
        return payRollBankName;
    }

    public void setPayRollBankName(String payRollBankName) {
        this.payRollBankName = payRollBankName;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public String getBankLinkNumber() {
        return bankLinkNumber;
    }

    public void setBankLinkNumber(String bankLinkNumber) {
        this.bankLinkNumber = bankLinkNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getBankPic() {
        return bankPic;
    }

    public void setBankPic(List<String> bankPic) {
        this.bankPic = bankPic;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonIdentity() {
        return personIdentity;
    }

    public void setPersonIdentity(String personIdentity) {
        this.personIdentity = personIdentity;
    }
}
