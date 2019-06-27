package com.ktp.project.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证识别类
 *
 * @author djcken
 * @date 2018/6/11
 */
public class CardUtil {

    public static Map<String, Object> getCarInfo1(String cardCode) {
        if (cardCode.length() == 18) {
            return getCarInfo18W(cardCode);
        } else if (cardCode.length() == 15) {
            return getCarInfo15W(cardCode);
        }
        return null;
    }

    /**
     * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证
     */
    private static Map<String, Object> getCarInfo18W(String cardCode) {
        Map<String, Object> map = new HashMap<>();
        String year = cardCode.substring(6).substring(0, 4);// 得到年份
        String yue = cardCode.substring(10).substring(0, 2);// 得到月份
        String day = cardCode.substring(12).substring(0, 2);//得到日
        int sex;
        if (Integer.parseInt(cardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = 2;//女
        } else {
            sex = 1;//男
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        // String fday=format.format(date).substring(8,10);
        int age = 0;
        if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        map.put("sex", sex);
        map.put("age", age);
        map.put("birthday", year + "-" + yue + "-" + day);
        map.put("year", year);
        map.put("month", yue);
        map.put("day", day);
        return map;
    }

    /**
     * 15位身份证的验证
     *
     * @param
     * @throws Exception
     */
    private static Map<String, Object> getCarInfo15W(String cardCode) {
        Map<String, Object> map = new HashMap<>();
        String uyear = "19" + cardCode.substring(6, 8);// 年份
        String uyue = cardCode.substring(8, 10);// 月份
        String uday = cardCode.substring(10, 12);//日
        String usex = cardCode.substring(14, 15);// 用户的性别
        int sex;
        if (Integer.parseInt(usex) % 2 == 0) {
            sex = 2;//女
        } else {
            sex = 1;//男
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        // String fday=format.format(date).substring(8,10);
        int age = 0;
        if (Integer.parseInt(uyue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(uyear) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(uyear);
        }
        map.put("sex", sex);
        map.put("age", age);
        map.put("birthday", uyear + "-" + uyue + "-" + uday);
        map.put("year", uyear);
        map.put("month", uyue);
        map.put("day", uday);
        return map;
    }

    /**
     * 解析地址
     *
     * @param address
     * @return
     */
    public static Map<String, String> addressResolution(String address) {
        String regex = "^(?<province>[^省]+省|.+自治区)?(?<city>[^市]+市|.+自治州|.+地区)?(?<county>[^县]+县|.+区)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        Matcher m = Pattern.compile(regex).matcher(address);
        String province, city, county, town, village;
        Map<String, String> row = null;
        while (m.find()) {
            row = new LinkedHashMap<>();
            province = m.group("province");
            row.put("province", province == null ? "" : province.trim());
            city = m.group("city");
            row.put("city", city == null ? "" : city.trim());
            county = m.group("county");
            row.put("county", county == null ? "" : county.trim());
            town = m.group("town");
            row.put("town", town == null ? "" : town.trim());
            village = m.group("village");
            row.put("village", village == null ? "" : village.trim());
        }
        return row;
    }
}
