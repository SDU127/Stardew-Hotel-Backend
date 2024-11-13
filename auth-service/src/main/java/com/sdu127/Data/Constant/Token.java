package com.sdu127.Data.Constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Token类型
 */
@Getter
@AllArgsConstructor
public enum Token {

    ACCESS("ACCESS", "vDK6uz7)A_TjK([+ej&75zLTG7wdag@#", 432000),
    REFRESH("REFRESH", "YunC8M￥rJP@8R6G*(W|VD#d1sH38h8Dl", 2592000);

    private final String type;
    private final String key;
    private final int expireTime;

    public static Token get(String type) {
        for (Token token : Token.values()) {
            if (token.getType().equals(type)) {
                return token;
            }
        }
        throw new RuntimeException("Token类型异常");
    }
}
