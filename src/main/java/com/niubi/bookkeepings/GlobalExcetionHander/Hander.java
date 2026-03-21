package com.niubi.bookkeepings.GlobalExcetionHander;


import com.niubi.bookkeepings.domain.po.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class Hander {
    @ExceptionHandler
    public Result handleException(SQLIntegrityConstraintViolationException e){
        return Result.error("当前数据已经存在，不允许重复添加");
    }
    @ExceptionHandler
    public Result handleException(Exception e) {
        String message = e.toString();
        int colonIndex = message.indexOf(": ");
        String errorMsg = (colonIndex != -1) ? message.substring(colonIndex + 2) : message;
        return Result.error("服务器异常：" + errorMsg);
    }
}
