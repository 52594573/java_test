package com.ktp.project.dto.GZProject;

import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.Md5Util;
import com.ktp.project.util.NumberUtil;

import java.util.Date;

public class GZProjectRequestBody {

    private String Token = "";//todo
    private String Date = NumberUtil.formatDateTime(new Date());
    private String Sign = Md5Util.MD5Encode(this.Token + "" + this.Date);//todo
    private Object RequestJson;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSign() {
        return Sign;
    }


    public void setSign(String sign) {
        Sign = sign;
    }

    public Object getRequestJson() {
        return RequestJson;
    }

    public void setRequestJson(Object requestJson) {
        RequestJson = requestJson;
    }

    public static GZProjectRequestBody newInstance(Object RequestJson){
        GZProjectRequestBody body = new GZProjectRequestBody();
        body.setRequestJson(RequestJson);
        return body;
    }

    public static void main(String[] args) {

        GZProjectRequestBody aaaaa = newInstance("aaaaa");
        System.out.println(GsonUtil.toJson(aaaaa));
    }
}
