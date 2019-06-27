package com.ktp.project.dto.AuthRealName;

import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.ProjectEnum;
import com.ktp.project.service.realName.AuthRealNameApi;
import com.ktp.project.util.GsonUtil;
import com.ktp.project.util.Md5Util;
import com.ktp.project.util.NumberUtil;
import net.sf.json.JSONObject;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

public class JmAndGsxProjectRequestBody {

    private String Token;
    private String Date;
    private String Sign;
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

    public static JmAndGsxProjectRequestBody newInstance(Object RequestJson){
        JmAndGsxProjectRequestBody body = new JmAndGsxProjectRequestBody();
        JSONObject object = JSONObject.fromObject(RequestJson);
        String projectId = object.get("projectId").toString();
        object.remove("projectId");object.remove("userId");
        int pId = Integer.parseInt(projectId);
        ProjectEnum projectEnum = EnumMap.projectEnumMap.get(pId);
        body.setRequestJson(object);
        body.setDate(NumberUtil.formatDateTime(new Date()));
        body.setToken(projectEnum.getToken());
        body.setSign(Md5Util.MD5Encode(body.getToken() + projectEnum.getKey() + body.getDate()));
        return body;
    }

    public static void main(String[] args) {

        JmAndGsxProAttendanceDto dto = new JmAndGsxProAttendanceDto();
        dto.setType(new BigInteger("1"));
        dto.setOther("1111");
        dto.setCheckDate("2018-12-24 10:00:00");
        dto.setUserId(967273);
        dto.setProjectId(3447);
        dto.setIdentityCode("0000000000");
        dto.setFaceUrl("11111111111");
        JmAndGsxProjectRequestBody body = JmAndGsxProjectRequestBody.newInstance(dto);
        System.out.println(GsonUtil.toJson(body));
        System.out.println(GsonUtil.toJson(body));
        Map<Integer, AuthRealNameApi> subclassMap = EnumMap.subclassMap;
        System.out.println(subclassMap.size());
        System.out.println(EnumMap.projectEnumMap.size());
    }
}
