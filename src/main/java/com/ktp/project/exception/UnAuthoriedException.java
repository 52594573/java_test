package com.ktp.project.exception;

/**
 * 用户鉴权失败
 *
 * @author djcken
 * @date 2018/5/24
 */
public class UnAuthoriedException extends RuntimeException {

    public UnAuthoriedException(String message) {
        super(message);
    }
}
