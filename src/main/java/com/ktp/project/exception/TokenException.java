package com.ktp.project.exception;

/**
 * token和id不匹配
 *
 * @author djcken
 * @date 2018/5/23
 */
public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
