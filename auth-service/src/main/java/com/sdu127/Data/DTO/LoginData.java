package com.sdu127.Data.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 登录结果
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoginData{
    /**
     * AccessToken
     */
    private String accessToken;

    /**
     * RefreshToken
     */
    private String refreshToken;

//    public static LoginData fromUser(User user, String accessToken, String refreshToken) {
//        LoginData loginData = new LoginData();
//        BeanUtils.copyProperties(user, loginData);
//        loginData.setAccessToken(accessToken);
//        loginData.setRefreshToken(refreshToken);
//        return loginData;
//    }
}
