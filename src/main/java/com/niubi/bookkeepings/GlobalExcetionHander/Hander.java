package com.niubi.bookkeepings.GlobalExcetionHander;


import com.niubi.bookkeepings.Excetion.DeleteExcetion;
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
    public Result handleException(DeleteExcetion e){
        String result = String.valueOf(e).lastIndexOf(":") != -1
                ? String.valueOf(e).substring(String.valueOf(e).lastIndexOf(":") + 1)
                : String.valueOf(e);
        return Result.error(result);
    }
}
