package com.atguigu.crowd.exception;

/**
 * 登陆失败是抛出的异常,自定义的异常
 */
public class LoginFailedException extends RuntimeException{

    private static final long serialVersionID = 1l;
    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    protected LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
