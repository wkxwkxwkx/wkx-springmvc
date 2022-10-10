package com.springmvc.exception;

/**
 * @project: mvcsourcecode
 * @description: 自己定义的异常类
 * @author: 王凯旋
 * @date: 2022/7/30 17:34:26
 * @version: 1.0
 */
public class ContextException extends RuntimeException{
    public ContextException(String message) {
        super(message);
    }
    public ContextException(Throwable cause) {
        super(cause);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
