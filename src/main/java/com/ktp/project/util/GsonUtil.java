package com.ktp.project.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Gson工具类
 *
 * @author djcken
 * @date 2017/8/7
 */
public class GsonUtil {
    /**
     * Description: 初始化GOSN参数
     */
    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()                                    //支持Map的key为复杂对象的形式
//				.serializeNulls()													//序列化null字段,进行显示设置
                .setDateFormat("yyyy-MM-dd HH:mm:ss")            //时间转化为特定格式
                .setPrettyPrinting()                                                //对json结果格式化.
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)            //long数值不自动转换科学计数法
                .create();

        return gson;
    }


    /**
     * 将json字符串转化成实体对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        T t = null;
        try {
            Gson gson = getGson();
            ;
            t = gson.fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     */
    public static <T> String toJson(T object) {
        String json = "";
        try {
            Gson gson = getGson();
            json = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 把json 字符串转化成list
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            Gson gson = getGson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据json字符串返回Map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        Gson gson = getGson();

        Map<String, Object> result = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
        return result;
    }

}
