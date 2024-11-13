package com.sdu127.Data.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerificationCodeMode {
    BIND_MAIL(1),
    UPDATE_PASSWORD(2),
    MAIL_LOGIN(3);

    private final Integer mode;
}
