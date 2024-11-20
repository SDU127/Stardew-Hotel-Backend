package com.sdu127.exception;

import com.sdu127.Data.VO.Result;
import com.sdu127.exception.Exceptions.InfoMessage;
import com.sdu127.exception.Exceptions.PathPermissionException;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;


/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 错误消息
     */
    @ExceptionHandler(InfoMessage.class)
    private ResponseEntity<Result> handleInfoMessage(InfoMessage e) {
        return e.returnMessage();
    }

    /**
     * 路径权限异常
     */
    @ExceptionHandler(PathPermissionException.class)
    private ResponseEntity<Result> handlePathPermissionException(PathPermissionException e) {
        logger.warn(e.log());
        return response(4030, "无权限");
    }

    /**
     * 请求参数类型错误和Servlet异常
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ServletException.class, MultipartException.class})
    private ResponseEntity<Result> handleMethodException(Exception e) {
        return response(4000, e.getMessage());
    }

    /**
     * 全局异常
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Result> handleException(Exception e) {
        logger.error("Server failed: ", e);
        return response(5000, "服务器异常");
    }

    private ResponseEntity<Result> response(Integer code, String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Result.error(code, message));
    }
}
