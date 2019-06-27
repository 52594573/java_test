package com.ktp.project.util;

import com.google.common.collect.Lists;
import com.ktp.project.constant.EnumMap;
import com.ktp.project.constant.RealNameConfig;
import com.ktp.project.dto.GZProject.GzAddOrUpdateWorkerDto;
import com.ktp.project.exception.BusinessException;
import com.ktp.project.service.realName.AuthRealNameApi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.springframework.util.CollectionUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

import static com.alibaba.druid.util.Utils.md5;

/**
 * 同步用户信息到不同项目工具类
 */
public class RealNameUtil {

    private static List<String> dateFiledList = Lists.newArrayList("doDate", "birthday", "expiryStart");

    private static List<String> imgFiledList = Lists.newArrayList("headImg" );

    /**
     * 判断对象中哪个参数为空
     * @param obj
     * @throws IllegalAccessException
     */
    public static void checkRequestParams(Object obj) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object o = field.get(obj);
            if (o == null) {
                throw new BusinessException(String.format("字段名:%s 值不能为空", fieldName));
            }
        }
    }

    /**
     * 给对象设置默认值(只针对广州项目)
     * @param obj
     * @throws IllegalAccessException
     */
    public static void setDefaultValueCheckNull(Object obj)  {
        try {
            Class<?> aClass = obj.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(obj);
                if (o == null || StringUtils.isBlank(String.valueOf(o))) {
                    String fieldName = field.getName();
                    Object null_field = EnumMap.getValueByKey("NULL_FIELD", fieldName, "NULL");
                    field.set(obj, String.valueOf(null_field));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BusinessException("给对象空字段设置默认值失败");
        }
    }

    /**
     * 获取AuthRealNameApi所有实现类的class对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Map<Integer, AuthRealNameApi> autoAuthRealNameApiSubclass() throws IllegalAccessException, InstantiationException {
        Map<Integer, AuthRealNameApi> map = new HashMap<>();
        Reflections reflections = new Reflections(RealNameConfig.API_PACKAGE);
        Set<Class<? extends AuthRealNameApi>> monitorClasses = reflections.getSubTypesOf(AuthRealNameApi.class);
        for (Class<? extends AuthRealNameApi> monitorClass : monitorClasses) {
            AuthRealNameApi authRealNameApi = monitorClass.newInstance();
//            Integer falg = authRealNameApi.getProjectId();
            Integer falg = 0;//TODO
            map.put(falg, authRealNameApi);
        }
        return map;
    }

    /**
     * 南宁南宁项目返回结果解析
     * @param json
     * @return
     */
    public static  String NNproResultSet(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }
        JSONObject object = JSONObject.fromObject(json);
        String code = String.valueOf(object.get("code"));
        String msg = String.valueOf(object.get("msg"));

        if (!"200".equals(code)) {
            throw new BusinessException(String.format("同步到实名系统失败: %s", msg));
        }
        return String.valueOf(object.get("d"));
    }

    /**
     * 南宁南宁项目返回结果解析
     * @param json
     * @return
     */
    public static  boolean NNproResIfSussess(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }
        JSONObject object = JSONObject.fromObject(json);
        String code = String.valueOf(object.get("code"));
        String msg = String.valueOf(object.get("msg"));

        if (!"200".equals(code)) {
            throw new BusinessException(String.format("同步到实名系统失败: %s", msg));
        }
        return true;
    }

    /**
     * 南宁南宁项目返回结果解析
     * @param json
     * @return
     */
    public static  boolean JMproResIfSussess(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }
        JSONObject object = JSONObject.fromObject(json);
        String code = String.valueOf(object.get("rstCode"));
        String msg = String.valueOf(object.get("rstMsg"));

        if (!"0".equals(code)) {
            throw new BusinessException(String.format("同步到实名系统失败: %s", msg));
        }
        return true;
    }

    /**
     * 广州项目返回结果解析
     * @param json
     * @return
     */
    public static  boolean GZproResIfSussess(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }
        JSONObject object = JSONObject.fromObject(json);
        String code = String.valueOf(object.get("resultCode"));
        String msg = String.valueOf(object.get("resultDesc"));

        if (!"0".equals(code)) {
            throw new BusinessException(String.format("同步到广州系统失败: %s", msg));
        }
        return true;
    }

    /**
     * 广州项目请求参数加密
     * @param dto
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String GZencodeRequestBody(Object dto, String privateKey){
        StringBuilder builder = new StringBuilder();
        TreeMap<String, String> treeMap = new TreeMap<>();
        JSONObject jsonObject = JSONObject.fromObject(dto);
        for (Object key : jsonObject.keySet()) {
            String keyVaule = String.valueOf(key);
            if (keyVaule.equals("sign")) {
                continue;
            }
            try {
                treeMap.put(keyVaule, URLEncoder.encode(String.valueOf(jsonObject.get(key)), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new BusinessException("广州项目请求参数加密失败");
            }
        }
        int flag = 0;
        for (String key : treeMap.keySet()) {
            if (flag != 0) {
                builder.append("&");
            }
            builder.append(key).append("=").append(treeMap.get(key));
            flag++;
        }
        String str = builder.toString();
//        builder.append("&sign=");
//        String sign = md5(builder.toString() + RealNameConfig.GZ_SECRETKEY);
//        treeMap.put("sign", sign);
        return builder.append("&sign=").append(md5(str + privateKey)).toString();
    }

    /**
    *
    * @Description: 高明项目返回结果解析
    * @Author: liaosh
    * @Date: 2018/12/19 0019
    */
    public static  boolean GMproResIfSussess(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }

        try {
            json= URLDecoder.decode(json,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject object = JSONObject.fromObject(json);
        String code = String.valueOf(object.get("result"));
        String msg = String.valueOf(object.get("message"));
        System.out.println(json);
        if (!"true".equals(code)) {
            throw new BusinessException(String.format("同步数据系统失败: %s", msg));
        }
        return true;
    }

    /**
     * 顺德项目结果解析
     * @param json
     * @return
     */
    public static  boolean SDproResIfSussess(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }
        JSONObject object = JSONObject.fromObject(json);
        boolean success = object.getBoolean("Success");
        String msg = String.valueOf(object.get("ErrMsg"));
        String strip = StringUtils.strip(msg, "[]");
//        JSONArray jsonArray = JSONArray.fromObject(msg);
//        if (CollectionUtils.isEmpty(jsonArray)){
//            System.out.println(true);
//        }
        if (!success || StringUtils.isNotBlank(strip)){
            throw new BusinessException(String.format("同步到顺德系统失败: %s", strip));
        }
        return true;
    }

    /**
     * 顺德项目结果解析
     * @param json
     * @return
     */
    public static  boolean SSproResIfSussess(String json) {
        if (StringUtils.isBlank(json)) {
            throw new BusinessException("参数不能为空");
        }
        JSONObject object = JSONObject.fromObject(json);
        boolean success = object.getBoolean("success");
        String msg = String.valueOf(object.get("msg"));

        if (!success || !msg.equals("0")){
            msg = EnumMap.getValueByKey("SS_RES", msg, msg).toString();
            throw new BusinessException(String.format("同步到顺德系统失败: %s", msg));
        }
        return true;
    }

    /**
     * AES加密
     * 实名认证中山项目
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data, String sign) throws Exception {
        byte[] raw = sign.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        String substring = sign.substring(0, 16);
        IvParameterSpec iv = new IvParameterSpec(substring.getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes("utf-8"));
        return Base64Util.encode(encrypted);
    }

    public static void main(String[] args) throws Exception {
        String data = encryptData("440684201812120306", "dccf770eec9e4fe6b4949def388dc68c");
        System.out.println(data);
    }

}
