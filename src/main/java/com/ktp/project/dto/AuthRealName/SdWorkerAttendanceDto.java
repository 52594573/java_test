package com.ktp.project.dto.AuthRealName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

/**
 * 顺德考勤dto
 */
public class SdWorkerAttendanceDto {

    @JsonProperty(value = "Token")
    private String Token;

    @JsonProperty(value = "deviceKey")
    private String deviceKey;

    @JsonProperty(value = "Data")
    private List <KaoQinData> Data;

    @JsonProperty(value = "ProjectNum")
    private String ProjectNum;

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    @JsonIgnore
    public void setToken(String token) {
        Token = token;
    }

    @JsonIgnore
    public String getDeviceKey() {
        return deviceKey;
    }

    @JsonIgnore
    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    @JsonIgnore
    public List<KaoQinData> getData() {
        return Data;
    }

    @JsonIgnore
    public void setData(List<KaoQinData> data) {
        Data = data;
    }

    @JsonIgnore
    public String getProjectNum() {
        return ProjectNum;
    }

    @JsonIgnore
    public void setProjectNum(String projectNum) {
        ProjectNum = projectNum;
    }


    public static class KaoQinData{

        @JsonProperty(value = "IDcardNum")
        private String IDcardNum;

        @JsonProperty(value = "AttendanceTime")
        private String AttendanceTime;

        @JsonProperty(value = "AttendanceType")
        private Integer AttendanceType;

        @JsonProperty(value = "IsInAttendance")
        private BigInteger IsInAttendance;

        @JsonProperty(value = "imgBase64")
        private String imgBase64;

        @JsonIgnore
        public String getIDcardNum() {
            return IDcardNum;
        }

        @JsonIgnore
        public void setIDcardNum(String IDcardNum) {
            this.IDcardNum = IDcardNum;
        }

        @JsonIgnore
        public String getAttendanceTime() {
            return AttendanceTime;
        }

        @JsonIgnore
        public void setAttendanceTime(String attendanceTime) {
            AttendanceTime = attendanceTime;
        }

        @JsonIgnore
        public Integer getAttendanceType() {
            return AttendanceType;
        }

        @JsonIgnore
        public void setAttendanceType(Integer attendanceType) {
            AttendanceType = attendanceType;
        }

        @JsonIgnore
        public BigInteger isInAttendance() {
            return IsInAttendance;
        }

        @JsonIgnore
        public void setInAttendance(BigInteger inAttendance) {
            IsInAttendance = inAttendance;
        }

        @JsonIgnore
        public String getImgBase64() {
            return imgBase64;
        }

        @JsonIgnore
        public void setImgBase64(String imgBase64) {
            this.imgBase64 = imgBase64;
        }
    }

}
