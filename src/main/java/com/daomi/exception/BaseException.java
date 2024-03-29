package com.daomi.exception;

/**
 * 业务异常类。约定所有业务异常必须继承它
 */
public class BaseException extends RuntimeException{
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }
}
