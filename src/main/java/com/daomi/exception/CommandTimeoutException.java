package com.daomi.exception;

public class CommandTimeoutException extends BaseException{

    public CommandTimeoutException() {
        super();
    }

    public CommandTimeoutException(String message) {
        super(message);
    }
}
