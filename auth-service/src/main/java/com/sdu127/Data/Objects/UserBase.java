package com.sdu127.Data.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 用户基础信息
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserBase {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户姓氏
     */
    private String lastName;

    /**
     * 用户名字
     */
    private String firstName;

    /**
     * 性别
     */
    private Boolean gender;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 身份
     */
    private String role;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 注册时间
     */
    private LocalDateTime register;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLogin;
}
