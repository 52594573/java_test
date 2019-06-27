package com.ktp.project.exception;

/**
 * 业务处理异常
 *
 * @author djcken
 * @date 2018/5/25
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
