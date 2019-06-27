package com.ktp.project.util;

import org.apache.http.util.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author djcken
 * @date 2017/8/28
 */
public class DateUtil {
    private static final HashMap<String, SimpleDateFormat> formats = new HashMap<>();
    public static final String FORMAT_DATE_TIME_NORMAL = "yyyyMMddHHmmssSSS";
    public static final String FORMAT_DATE_TIME_MINS = "yyyyMMddHHmmss";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_CHINESE = "yyyy年MM月dd日";

    private static SimpleDateFormat getFormat(String formatStr) {
        SimpleDateFormat format = formats.get(formatStr);
        if (format == null) {
            format = new SimpleDateFormat(formatStr);
            formats.put(formatStr, format);
        }
        return format;
    }

    public static String getFormatDate(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        return getFormatDateTime(date, format);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    public static String defaultFormat(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String defaultFormat(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static String getFormatCurrentTime(String format) {
        return getFormatDateTime(new Date(), format);
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static long getFormatTime(String dstr, String format) {
        return getFormatDate(dstr, format).getTime();
    }

    public static Date getFormatDate(String dstr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dstr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return getFormat(format).format(date);
    }

    public static Date parse(String str, String format) {
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            return getFormat(format).parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
