package com.sdu127.Data.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户身份权限
 */
@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("管理员"),
    SERVER("客服"),
    CLIENT("客户");

    public final String role;
}
