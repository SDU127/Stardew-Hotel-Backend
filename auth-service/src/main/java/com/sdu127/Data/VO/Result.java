package com.sdu127.Data.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private Object data;
    private String msg;

    public static Result ok(){
        return new Result(2000, null, null);
    }

    public static Result success(Object data){
        return new Result(2001, data, null);
    }

    public static Result message(String msg) {
        return new Result(2002, null, msg);
    }

    public static Result error(Integer code, String msg){
        return new Result(code, null, msg);
    }}
