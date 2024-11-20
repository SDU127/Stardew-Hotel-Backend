package com.sdu127.Data.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseData {

    PARAM_WRONG(4000, "参数错误"),
    MAIL_FORMAT_ERROR(4001, "邮件格式错误"),
    ID_FORMAT_ERROR(4002, "身份证号码格式错误"),
    USER_NAME_USED(4003, "用户名已被占用"),
    MAIL_USED(4004, "邮箱已被注册"),
    ROLE_ERROR(4005, "身份不存在"),
    NAME_ERROR(4006, "名字错误"),
    HOUSE_EXISTS(4007, "房屋已存在"),
    PARKING_SPOT_EXISTS(4008, "车位已存在"),
    URL_WRONG(4009, "URL格式错误"),

    TOKEN_VERIFICATION_FAILED(4011, "Token验证失败"),
    PASSWORD_WRONG(4012, "密码错误"),
    VERIFICATION_CODE_WRONG(4013, "验证码错误"),

    FORBIDDEN(4030, "无权限"),
    ALREADY_PAID(4031, "已经支付过了"),
    REMARK_FORBIDDEN(4032, "无法评分"),
    ALREADY_READ(4033, "已经读过了"),

    USER_NOT_FOUND(4041, "用户不存在"),
    MAIL_NOT_FOUND(4042, "邮箱不存在"),
    USER_OR_MAIL_NOT_FOUND(4043, "用户/邮箱不存在"),
    PAYMENT_NOT_FOUND(4044, "缴费不存在"),
    PARKING_SPOT_NOT_AVAILABLE(4045, "停车位不可用"),
    HOUSE_NOT_AVAILABLE(4046, "房屋不可用"),
    REPORT_NOT_AVAILABLE(4047, "该投诉不可用"),
    NOTICE_NOT_AVAILABLE(4048, "通知不可用");



    private final Integer code;
    private final String message;
}
