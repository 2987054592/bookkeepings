package com.niubi.bookkeepings.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
    public static <T> Result<T> success(T data){
        return new Result<>(1,"ok",data);
    }
    public static <T> Result<T> success(){
        return new Result<>(1,"ok",null);
    }
    public static <T> Result<T> error(String  message){
        return new Result<>(0,message,null);
    }

}
