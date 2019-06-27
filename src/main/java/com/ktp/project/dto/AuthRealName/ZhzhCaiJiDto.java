package com.ktp.project.dto.AuthRealName;

import java.math.BigInteger;

public class ZhzhCaiJiDto {
    private String Name;
    private String CerfNum;
    private String Nation;
    private String Native;
    private BigInteger Sex;
    private String IdCardAddress;
    private String Birthday;
    private String CollectPhoto;
    private String IdCardPhoto;
    private String IssuingAuthority;
    private String ValidityPeriodBe;

    private String byear;
    private String bmonth;
    private String bday;
    private String stime;
    private String etime;

    public ZhzhCaiJiDto() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCerfNum() {
        return CerfNum;
    }

    public void setCerfNum(String cerfNum) {
        CerfNum = cerfNum;
    }

    public String getNation() {
        return Nation;
    }

    public void setNation(String nation) {
        Nation = nation;
    }

    public String getNative() {
        return Native;
    }

    public void setNative(String aNative) {
        Native = aNative;
    }

    public BigInteger getSex() {
        return Sex;
    }

    public void setSex(BigInteger sex) {
        Sex = sex;
    }

    public String getIdCardAddress() {
        return IdCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        IdCardAddress = idCardAddress;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getCollectPhoto() {
        return CollectPhoto;
    }

    public void setCollectPhoto(String collectPhoto) {
        CollectPhoto = collectPhoto;
    }

    public String getIdCardPhoto() {
        return IdCardPhoto;
    }

    public void setIdCardPhoto(String idCardPhoto) {
        IdCardPhoto = idCardPhoto;
    }

    public String getIssuingAuthority() {
        return IssuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        IssuingAuthority = issuingAuthority;
    }

    public String getValidityPeriodBe() {
        return ValidityPeriodBe;
    }

    public void setValidityPeriodBe(String validityPeriodBe) {
        ValidityPeriodBe = validityPeriodBe;
    }

    public String getByear() {
        return byear;
    }

    public void setByear(String byear) {
        this.byear = byear;
    }

    public String getBmonth() {
        return bmonth;
    }

    public void setBmonth(String bmonth) {
        this.bmonth = bmonth;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }
}
