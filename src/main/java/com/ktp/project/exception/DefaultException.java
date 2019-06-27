package com.ktp.project.exception;

/**
 * 身份校验不通过异常
 *
 * @author djcken
 * @date 2018/5/23
 */
public class DefaultException extends RuntimeException {

    public DefaultException(String message) {
        super(message);
    }
}
