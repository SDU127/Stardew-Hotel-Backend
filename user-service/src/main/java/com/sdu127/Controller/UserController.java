package com.sdu127.Controller;

import com.sdu127.Data.Objects.UserBase;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.UserMapper;
import com.sdu127.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    UserMapper userMapper;


    /**
     * 绑定邮箱
     *
     * @param userMail 新邮箱
     * @param mailVerificationCode 验证码
     */
    @PostMapping("/bindMail")
    public Result bindMail(@RequestParam String userMail, @RequestParam String mailVerificationCode) {
        return userService.bindMail(userMail, mailVerificationCode);
    }

    /**
     * 更新密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @PatchMapping("/modifyPassword")
    public Result modifyPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return userService.modifyPassword(oldPassword, newPassword);
    }

    /**
     * 更新用户
     *
     * @param userBase 用户信息
     */
    @PatchMapping("/updateUser")
    public Result updateUser(@RequestBody UserBase userBase) {
        return userService.updateUser(userBase);
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo() {
        return userService.getUserInfo();
    }







    //-------------------------------------------------微服务-------------------------------------------------//

    // 测试
    @GetMapping("/{id}")
    User getUserById(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }

    // 通过用户id获取用户
    @PostMapping("/get-user/{userId}")
    User getUser(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }

    // 通过邮箱获取用户
    @GetMapping("/get-user-by-mail")
    User getUserByMail(@RequestParam String mail) {
        return userService.getUserByMail(mail);
    }

    // 通过用户名/邮箱获取用户
    @GetMapping("/get-user-by-name-or-mail")
    User getUserByNameOrMail(@RequestParam String nameOrMail) {
        return userService.getUserByNameOrMail(nameOrMail);
    }

    // 判断用户名是否存在
    @GetMapping("/user-name-exist")
    Boolean userNameExist(@RequestParam String userName) {
        return userMapper.userNameExist(userName);
    }

    // 判断邮箱是否存在
    @GetMapping("/mail-exist")
    Object mailExist(@RequestParam String mail) {
        return userMapper.mailExist(mail);
    }

    // 更新用户信息
    @PostMapping("/update-user")
    void updateUser(@RequestParam String userMail, @RequestParam String newPassword) {
        userService.updatePasswordByMail(userMail, newPassword);
    }
}
