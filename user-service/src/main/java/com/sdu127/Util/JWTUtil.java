package com.sdu127.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.Constant.Token;
import com.sdu127.Exception.Exceptions.InfoMessage;
import org.springframework.stereotype.Component;

/**
 * JWT
 */
@Component
public class JWTUtil {
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
}
