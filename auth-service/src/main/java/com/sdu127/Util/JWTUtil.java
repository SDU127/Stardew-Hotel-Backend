package com.sdu127.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.Constant.Token;
import com.sdu127.Exception.Exceptions.InfoMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT
 */
@Component
public class JWTUtil {
    @Value("${expire_time.refresh_bound}")
    public Integer REFRESH_BOUND;

    /**
     * 生成token
     */
    public static String getToken(Map<String, String> map, int expireTime, String key) {
        //获取时间，设置token过期时间
        Instant instant = Instant.now();
        Instant newInstant = instant.plusSeconds(expireTime);

        //JWT添加payload
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        //JWT过期时间 + signature
        return builder.withExpiresAt(newInstant).sign(Algorithm.HMAC256(key));
    }

    /**
     * 返回token内容
     */
    public DecodedJWT getTokenInfo(String token, String type) {
        try {
            Token tokenData = Token.get(type);
            return JWT.require(Algorithm.HMAC256(tokenData.getKey())).build().verify(token);
        } catch (Exception e) {
            throw new InfoMessage(ResponseData.TOKEN_VERIFICATION_FAILED);
        }
    }

    /**
     * 生成token
     */
    public String getTokenWithPayLoad(String id, String userName, String role, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("user_name", userName);
        map.put("role", role);
        map.put("type", type);

        Token tokenData = Token.get(type);
        return JWTUtil.getToken(map, tokenData.getExpireTime(), tokenData.getKey());
    }
}
