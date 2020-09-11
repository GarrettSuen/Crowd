package com.laowei.crowdfunding.exception;

/**
 * 自定义无效访问异常类
 * 作用：防止用户未进行登录，访问受保护的资源
 * @Author：Garrett
 * @Create：2020-07-20 13:04
 */
public class AccessForbiddenException extends RuntimeException{
    private static final long serialVersionUID = -6943533329784933251L;

    public AccessForbiddenException() {
        super();
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    protected AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
