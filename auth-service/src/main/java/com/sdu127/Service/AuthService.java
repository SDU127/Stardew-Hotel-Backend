package com.sdu127.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sdu127.Data.Constant.*;
import com.sdu127.Data.DTO.LoginData;
import com.sdu127.Data.DTO.MailTemplate;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Exception.Exceptions.InfoMessage;
import com.sdu127.Util.CommonUtil;
import com.sdu127.Util.JWTUtil;
import com.sdu127.Util.MailUtil;
import com.sdu127.Util.RedisUtil;
import jakarta.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务
 */
@Service
public class AuthService {
    @Resource
    UserServiceIns userServiceIns;
    @Resource
    CommonUtil commonUtil;
    @Resource
    JWTUtil jwtUtil;
    @Resource
    MailUtil mailUtil;
    @Resource
    RedisUtil redisUtil;

    /**
     * 用户名登录
     */
    public Result login(String nameOrMail, String password) {
        // 查询用户是否存在
        User user = userServiceIns.getUserByNameOrMail(nameOrMail);

        // 判断密码是否正确
        if (!BCrypt.checkpw(password, user.getPassword())) throw new InfoMessage(ResponseData.PASSWORD_WRONG);
        return doLogin(user);
    }

    /**
     * 通过邮箱验证码登录
     */
    public Result mailVerificationCodeLogin(String userMail, String verificationCode) {
        String mode = String.valueOf(VerificationCodeMode.MAIL_LOGIN.getMode());
        String redisKey = RedisKey.MAIL_VERIFICATION_CODE.getKey(mode, userMail);

        // 校验验证码
        if (!verificationCode.equals(redisUtil.get(redisKey))) throw new InfoMessage(ResponseData.VERIFICATION_CODE_WRONG);

        redisUtil.deleteKey(redisKey);

        // 获取用户
        User user = userServiceIns.getUserByMail(userMail);
        return doLogin(user);
    }

    /**
     * 登录
     */
    private Result doLogin(User user) {
        // 生成RefreshToken
        String refreshToken = jwtUtil.getTokenWithPayLoad(user.getId().toString(), user.getName(), user.getRole(), Token.REFRESH.getType());
        redisUtil.set("EstateSystem:RefreshToken:" + user.getId(), refreshToken, Token.REFRESH.getExpireTime());

        // 生成AccessToken
        String accessToken = jwtUtil.getTokenWithPayLoad(user.getId().toString(), user.getName(), user.getRole(), Token.ACCESS.getType());

        // 获取当前时间戳并更新登录时间
        Instant instant = Instant.now();

        // 修改Redis中的登录记录
        String redisKey = RedisKey.LOGIN.getKey() + user.getId();
        redisUtil.set(redisKey, instant.toString());

        LoginData loginData = new LoginData(accessToken, refreshToken);

        return Result.success(loginData);
    }

    /**
     * 获取邮件验证码
     */
    public Result getMailVerificationCode(String userMail, int mode) {
        // 校验邮箱格式
        if (!commonUtil.isValidEmail(userMail)) throw new InfoMessage(ResponseData.MAIL_FORMAT_ERROR);

        if (mode == 1) {
            // 判断邮箱是否被注册，用于邮箱绑定
            if (Boolean.TRUE.equals(userServiceIns.mailExist(userMail))) throw new InfoMessage(ResponseData.MAIL_USED);
        } else {
            // 判断邮箱是否存在，用于更新密码
            if (Boolean.FALSE.equals(userServiceIns.mailExist(userMail))) throw new InfoMessage(ResponseData.MAIL_NOT_FOUND);
        }

        // 生成邮件验证码
        String mailVerificationCode = commonUtil.generateRandomString();

        // 封装并异步发送邮件
        MailTemplate mailTemplate = new MailTemplate(userMail, "物业管理系统", "验证码为" + mailVerificationCode, false);
        mailUtil.sendMail(mailTemplate);

        // 存入Redis
        String redisKey = RedisKey.MAIL_VERIFICATION_CODE.getKey(String.valueOf(mode), userMail);

        redisUtil.set(redisKey, mailVerificationCode, 300);
        return Result.ok();
    }

    /**
     * 通过邮箱更新用户密码
     */
    public Result updatePassword(String userMail, String verificationCode, String newPassword) {
        String mode = String.valueOf(VerificationCodeMode.UPDATE_PASSWORD.getMode());
        String redisKey = RedisKey.MAIL_VERIFICATION_CODE.getKey(mode, userMail);

        // 校验验证码
        if (!verificationCode.equals(redisUtil.get(redisKey))) throw new InfoMessage(ResponseData.VERIFICATION_CODE_WRONG);

        redisUtil.deleteKey(redisKey);

        // 更新密码
        userServiceIns.updateUser(userMail, newPassword);
        return Result.ok();
    }

    /**
     * 刷新token
     */
    public Result refresh(String refreshToken) {
        // 获取refreshToken的payload信息
        DecodedJWT info = jwtUtil.getTokenInfo(refreshToken, Token.REFRESH.getType());

        String id = info.getClaim("id").asString();
        String userName = info.getClaim("user_name").asString();
        String role = info.getClaim("role").asString();

        // 生成新token
        String key = "EstateSystem:RefreshToken:" + id;
        if (refreshToken.equals(redisUtil.get(key))) {
            String newAccessToken = jwtUtil.getTokenWithPayLoad(id, userName, role, Token.ACCESS.getType());
            Map<String, String> map = new HashMap<>();
            map.put("AccessToken", newAccessToken);

            // 判断refreshToken时效
            long timeLeft = redisUtil.getExpire(key);
            if (timeLeft <= jwtUtil.REFRESH_BOUND) {
                String newRefreshToken = jwtUtil.getTokenWithPayLoad(id, userName, role, Token.REFRESH.getType());
                redisUtil.set(key, newRefreshToken, Token.REFRESH.getExpireTime());
                map.put("RefreshToken", newRefreshToken);
            } else {
                map.put("RefreshToken", refreshToken);
            }

            return Result.success(map);
        }

        throw new JWTVerificationException("无有效RefreshToken");
    }
}
