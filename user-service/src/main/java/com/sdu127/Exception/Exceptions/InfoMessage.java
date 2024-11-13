package com.sdu127.Exception.Exceptions;

import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.VO.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 错误信息返回
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoMessage extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(InfoMessage.class);

    private ResponseData responseData;

    public InfoMessage(ResponseData responseData) {
        this.responseData = responseData;
    }

    public ResponseEntity<Result> returnMessage() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Result.error(responseData.getCode(), responseData.getMessage()));
    }
}
