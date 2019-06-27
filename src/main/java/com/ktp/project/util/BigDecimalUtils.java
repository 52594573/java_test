package com.ktp.project.util;

import java.math.BigDecimal;

/**
 * Created by LinHon 2018/8/22
 */
public class BigDecimalUtils {

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static double muls(double... values) {
        if (values.length < 2) {
            throw new IllegalArgumentException("至少传入两个参数");
        }

        BigDecimal b1 = new BigDecimal(Double.toString(values[0]));

        for (int i = 1; i < values.length; i++) {
            BigDecimal b2 = new BigDecimal(Double.toString(values[i]));
            b1 = b1.multiply(b2);
        }
        return b1.doubleValue();
    }

}
