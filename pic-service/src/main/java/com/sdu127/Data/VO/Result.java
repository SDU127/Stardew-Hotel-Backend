package com.sdu127.Data.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int code;
    private String message;
    private Object data;

    public static Result ok(){
        return new Result(2000, null, null);
    }

    public static Result success(String message, Object data) {  //返回响应参数
        return new Result(200, message, data);
    }

    public static Result error(String message, Object data) {
        return new Result(500, message, data);
    }
}
