package com.sdu127.Controller;

import com.sdu127.Data.VO.Result;
import com.sdu127.Service.AuthService;
import com.sdu127.Service.UserServiceIns;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 认证
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    AuthService authService;
    @Resource
    UserServiceIns userServiceIns;

    @GetMapping("/test/{id}")
    public Result test(@PathVariable("id") Integer id) {
        return Result.success(userServiceIns.getUserById(id));
    }

    /**
     * 用户名/邮箱登录
     *
     * @param nameOrMail 用户名/邮箱
     * @param password 密码
     */
    @PostMapping("/login")
    public Result login(@RequestParam String nameOrMail, String password) {
        return authService.login(nameOrMail, password);
    }

    /**
     * 通过邮箱验证码登录
     *
     * @param userMail 邮箱
     * @param verificationCode 验证码
     */
    @PostMapping("/mailVerificationCodeLogin")
    public Result mailVerificationCodeLogin(@RequestParam String userMail, @RequestParam String verificationCode) {
        return authService.mailVerificationCodeLogin(userMail, verificationCode);
    }

    /**
     * 获取邮箱验证码
     *
     * @param userMail 用户邮箱
     * @param mode 1-绑定邮箱，2-修改密码模式，3-验证码登录模式
     *
     */
    @PostMapping("/getMailVerificationCode")
    public Result getMailVerificationCode(@RequestParam String userMail,
                                          @RequestParam int mode) {
        return authService.getMailVerificationCode(userMail, mode);
    }

    /**
     * 检查用户名是否存在
     *
     * @param userName 用户名
     */
    @GetMapping("/checkUserName")
    public Result checkUserName(@RequestParam String userName) {
        return Result.message(userServiceIns.userNameExist(userName) ? "此用户名已被占用" : "此用户名可以使用");
    }

    /**
     * 通过邮箱更新用户密码
     *
     * @param userMail 用户邮箱
     * @param verificationCode 邮件验证码
     * @param newPassword 新密码
     */
    @PatchMapping("/updatePassword")
    public Result updatePassword(@RequestParam String userMail, @RequestParam String verificationCode, @RequestParam String newPassword) {
        return authService.updatePassword(userMail, verificationCode, newPassword);
    }

    /**
     * 更新Token
     *
     * @param refreshToken refreshToken
     */
    @PostMapping("/refresh")
    public Result refresh(@RequestParam String refreshToken) {
        return authService.refresh(refreshToken);
    }
}

