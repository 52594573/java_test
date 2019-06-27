package com.ktp.project.entity;

import com.google.gson.annotations.SerializedName;
import com.ktp.project.util.GsonUtil;

import java.io.Serializable;

/**
 * 基础实体
 *
 * @author djcken
 * @date 2018/5/23
 */
public class BaseEntity implements Serializable {

    private Status status;//状态信息
    @SerializedName("busstatus")
    private BusinessStatus businessStatus;
    private Object content;

    public boolean isOk() {
        return status != null && status.getCode() == 10;
    }

    public boolean isBusniessOk() {
        return businessStatus != null && businessStatus.getCode() == 100;
    }

    public String getMessage() {
        return status != null ? status.getMsg() : "";
    }

    public String getBusniessMessage() {
        return businessStatus != null ? businessStatus.getMsg() : "";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BusinessStatus getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(BusinessStatus businessStatus) {
        this.businessStatus = businessStatus;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * 接口状态
     */
    public static class Status implements Serializable {
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    /**
     * 业务状态
     */
    public static class BusinessStatus implements Serializable {

        private int code;
        private String msg;
        private String integralmsg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getIntegralmsg() {
            return integralmsg;
        }

        public void setIntegralmsg(String integralmsg) {
            this.integralmsg = integralmsg;
        }
    }

    public String toJson() {
        return GsonUtil.toJson(this);
    }
}
