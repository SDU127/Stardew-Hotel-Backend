package com.sdu127.exception.Exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 路径无权限
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PathPermissionException extends RuntimeException{
    private Integer userId;
    private String path;

    public PathPermissionException(String message, Integer userId, String path) {
        super(message);
        this.userId = userId;
        this.path = path;
    }

    public String log() {
        return "Permission Denied for user [ " + userId + " ] on path [ " + path + " ]";
    }
}
