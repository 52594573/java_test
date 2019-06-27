package com.ktp.project.util;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 *
 * @author djcken
 * @date 2018/5/28
 */
public class StringUtil {

    public static int parseToInt(String s) {
        int value = 0;
        try {
            value = Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
        return value;
    }

    public static long parseToLong(String s) {
        long value = 0;
        try {
            value = Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
        return value;
    }

    public static float parseToFloat(String s) {
        float value = 0;
        try {
            value = Float.parseFloat(s);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
        return value;
    }

    public static double parseToDouble(String s) {
        double value = 0;
        try {
            value = Double.parseDouble(s);
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
        return value;
    }

    public static String getNotNullString(String str) {
        if (str == null) {
            str = "";
        }
        return str;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 去除制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 生成UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 规则 年两位+月+时+分+uid四位+四位随机数
     *
     * @param buyerId 用户id
     * @return
     */
    public static String getKtpTradeNo(int buyerId) {
        String uid = String.format("%04d", buyerId);//不足四位补0
        uid = uid.length() > 4 ? uid.substring(uid.length() - 4, uid.length()) : uid;
        String ktpTradeNo = DateUtil.format(new Date(), DateUtil.FORMAT_DATE_TIME_MINS) + uid + RandomUtil.random(1000, 9999);
        ktpTradeNo = ktpTradeNo.substring(2, ktpTradeNo.length());
        return ktpTradeNo;
    }

}
