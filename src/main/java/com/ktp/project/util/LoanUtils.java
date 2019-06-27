package com.ktp.project.util;


import com.ktp.project.exception.BusinessException;

import org.slf4j.Logger;


/**
 * Created by LinHon 2018/8/21
 */
public class LoanUtils {


    /**
     * 不建议使用，该类要删了
     *
     * @param errorMsg
     * @param e
     * @return
     */
    @Deprecated
    public static String buildExceptionResponse(Logger log, String errorMsg, Exception e) {
        log.error(errorMsg, e);
        if (e instanceof IllegalArgumentException) {
            return ResponseUtil.createBussniessErrorJson(400, "缺少必要参数");
        }
        if (e instanceof BusinessException) {
            return ResponseUtil.createBussniessErrorJson(500, "出现错误:" + e.getMessage());
        }
        return ResponseUtil.createBussniessErrorJson(500, "出现错误");
    }

}
