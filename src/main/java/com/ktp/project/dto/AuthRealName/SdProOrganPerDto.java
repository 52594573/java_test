package com.ktp.project.dto.AuthRealName;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

/**
 * 顺德项目工人信息dto
 */
public class SdProOrganPerDto {

    @JsonProperty(value = "Token")
    private String Token;

    @JsonProperty(value = "PostJson")
    private WorkerInfo PostJson;

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    @JsonIgnore
    public void setToken(String token) {
        Token = token;
    }
    @JsonIgnore
    public WorkerInfo getPostJson() {
        return PostJson;
    }
    @JsonIgnore
    public void setPostJson(WorkerInfo postJson) {
        PostJson = postJson;
    }

    public static class WorkerInfo{

        @JsonProperty(value = "IdNum")
        private String IdNum;

        @JsonProperty(value = "Name")
        private String Name;

        @JsonProperty(value = "Sex")
        private Integer Sex;

        @JsonProperty(value = "Nation")
        private String Nation;

        @JsonProperty(value = "Birthday")
        private String Birthday;

        @JsonProperty(value = "Address")
        private String Address;

        @JsonProperty(value = "Native")
        private String Native;

        @JsonProperty(value = "Phone")
        private String Phone;

        @JsonProperty(value = "EmerPeople")
        private String EmerPeople;

        @JsonProperty(value = "EmerPhone")
        private String EmerPhone;

        @JsonProperty(value = "Culture")
        private BigInteger Culture;

        @JsonProperty(value = "Health")
        private BigInteger Health;

        @JsonProperty(value = "WorkerType")
        private BigInteger WorkerType;

        @JsonProperty(value = "CompanyName")
        private String CompanyName;

        @JsonProperty(value = "CompanyNumber")
        private String CompanyNumber;

        @JsonIgnore
        public String getIdNum() {
            return IdNum;
        }

        @JsonIgnore
        public void setIdNum(String idNum) {
            IdNum = idNum;
        }

        @JsonIgnore
        public String getName() {
            return Name;
        }

        @JsonIgnore
        public void setName(String name) {
            Name = name;
        }

        @JsonIgnore
        public Integer getSex() {
            return Sex;
        }

        @JsonIgnore
        public void setSex(Integer sex) {
            Sex = sex;
        }

        @JsonIgnore
        public String getNation() {
            return Nation;
        }

        @JsonIgnore
        public void setNation(String nation) {
            Nation = nation;
        }

        @JsonIgnore
        public String getBirthday() {
            return Birthday;
        }

        @JsonIgnore
        public void setBirthday(String birthday) {
            Birthday = birthday;
        }

        @JsonIgnore
        public String getAddress() {
            return Address;
        }


        @JsonIgnore
        public void setAddress(String address) {
            Address = address;
        }

        @JsonIgnore
        public String getNative() {
            return Native;
        }

        @JsonIgnore
        public void setNative(String aNative) {
            Native = aNative;
        }

        @JsonIgnore
        public String getPhone() {
            return Phone;
        }

        @JsonIgnore
        public void setPhone(String phone) {
            Phone = phone;
        }

        @JsonIgnore
        public String getEmerPeople() {
            return EmerPeople;
        }

        @JsonIgnore
        public void setEmerPeople(String emerPeople) {
            EmerPeople = emerPeople;
        }

        @JsonIgnore
        public String getEmerPhone() {
            return EmerPhone;
        }

        @JsonIgnore
        public void setEmerPhone(String emerPhone) {
            EmerPhone = emerPhone;
        }

        @JsonIgnore
        public BigInteger getCulture() {
            return Culture;
        }

        @JsonIgnore
        public void setCulture(BigInteger culture) {
            Culture = culture;
        }

        @JsonIgnore
        public BigInteger getHealth() {
            return Health;
        }

        @JsonIgnore
        public void setHealth(BigInteger health) {
            Health = health;
        }

        @JsonIgnore
        public BigInteger getWorkerType() {
            return WorkerType;
        }

        @JsonIgnore
        public void setWorkerType(BigInteger workerType) {
            WorkerType = workerType;
        }

        @JsonIgnore
        public String getCompanyName() {
            return CompanyName;
        }

        @JsonIgnore
        public void setCompanyName(String companyName) {
            CompanyName = companyName;
        }

        @JsonIgnore
        public String getCompanyNumber() {
            return CompanyNumber;
        }

        @JsonIgnore
        public void setCompanyNumber(String companyNumber) {
            CompanyNumber = companyNumber;
        }
    }
}
