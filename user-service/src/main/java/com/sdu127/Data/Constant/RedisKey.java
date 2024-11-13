package com.sdu127.Data.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisKey {
    LOGIN("Login"),
    MAIL_VERIFICATION_CODE("MailVerificationCode");

    private final String key;

    /**
     * 生成RedisKey
     *
     * @param keys key的参数
     */
    public String getKey(String... keys) {
        StringBuilder redisKey = new StringBuilder("EstateSystem:").append(key).append(":");
        for (String key : keys) {
            redisKey.append(key).append(":");
        }
        return redisKey.toString();
    }
}
