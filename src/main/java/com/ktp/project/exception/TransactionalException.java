package com.ktp.project.exception;


/**
 * 事务异常，一般用于与第三方接口对接时使用
 * Created by LinHon 2018/8/11
 */
public class TransactionalException extends RuntimeException {

    public TransactionalException(String msg) {
        super(msg);
    }

}
