package com.club.exception;

import com.club.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 利用 Spring 的 @RestControllerAdvice 拦截所有异常，统一返回 Result 对象。
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. 处理自定义业务异常（直接返回错误信息给前端）
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    // 2. 处理所有未捕获的异常（兜底，返回通用错误）
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常：", e); // 打印完整堆栈，方便教师排查
        return Result.error("系统繁忙，请稍后重试");
    }
}