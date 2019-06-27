package com.ktp.project.dto.AuthRealName.ss;


public class SsRes {
    private String data;
    private boolean success = true;
    private String msg = "0";

    public SsRes(String data) {

        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
