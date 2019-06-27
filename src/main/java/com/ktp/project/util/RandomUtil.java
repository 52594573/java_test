package com.ktp.project.util;

import java.util.Random;

public class RandomUtil {

    public static final char[] chars = "01234567890abcdefghigklmnopqrstuvwxwzABCDEFGHIGKLMNOPQRSTUVWXWZ".toCharArray();

    public static final int charsLen = chars.length;

    public static final Random random = new Random();

    public static String generateStr(int minLen, int maxLen) {
        if (minLen == maxLen) {
            return generateStr(minLen);
        } else {
            int min = Math.min(minLen, maxLen);
            int interval = Math.abs(maxLen - minLen);
            return generateStr(min + random.nextInt(interval));
        }
    }

    public static String generateStr(int length) {
        StringBuilder strBld = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strBld.append(chars[random.nextInt(charsLen)]);
        }
        return strBld.toString();
    }

    public static int random(int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static String generateTimeNumber() {
        return generateStr(11);
    }
}
