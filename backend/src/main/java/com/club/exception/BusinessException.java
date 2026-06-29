package com.club.exception;

// 自定义业务异常：用来抛出业务逻辑错误（如“库存不足”、“预约冲突”），让事务管理器能捕获并回滚。
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}