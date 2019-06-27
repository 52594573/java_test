package com.ktp.project.util;

/**
 * @author djcken
 * @date 2018/6/30
 */
public class PageUtil {

    public static int getFirstResult(int page, int pageSize) {
        int firstResult = 0;
        if (page > 1) {
            firstResult = (page - 1) * pageSize;
        }
        return firstResult;
    }

}
