package com.ktp.project.web;

import com.ktp.project.exception.BusinessException;
import com.ktp.project.exception.DefaultException;
import com.ktp.project.exception.TokenException;
import com.ktp.project.exception.UnAuthoriedException;
import com.ktp.project.util.ResponseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author djcken
 * @date 2018/5/23
 */
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    private static final int CODE_ERR_WRONG = 41;//缺少必要参数
    private static final int CODE_ERR_UNAUTHORIED = 42;//鉴权失败
    private static final int CODE_ERR_TOKEN = 45;//token和id不匹配
    private static final int CODE_ERR_BUSINESS = 140;//业务处理异常

    @ExceptionHandler(UnAuthoriedException.class)
    @ResponseBody
    public String unAuthorityException(UnAuthoriedException e) {
        return ResponseUtil.createApiErrorJson(CODE_ERR_WRONG, e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    @ResponseBody
    public String wrongTokenException(TokenException e) {
        return ResponseUtil.createApiErrorJson(CODE_ERR_TOKEN, e.getMessage());
    }

    @ExceptionHandler(DefaultException.class)
    @ResponseBody
    public String defaultException(DefaultException e) {
        return ResponseUtil.createApiErrorJson(CODE_ERR_UNAUTHORIED, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public String businessException(BusinessException e) {
        return ResponseUtil.createBussniessErrorJson(CODE_ERR_BUSINESS, e.getMessage());
    }
}
