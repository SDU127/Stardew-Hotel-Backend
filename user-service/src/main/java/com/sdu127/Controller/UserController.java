package com.sdu127.Controller;

import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.Objects.UserBase;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.UserMapper;
import com.sdu127.Service.UserService;
import com.sdu127.Annotation.RequiredLogin;
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
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
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
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @PatchMapping("/modifyPassword")
    public Result modifyPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return userService.modifyPassword(oldPassword, newPassword);
    }

    /**
     * 更新用户
     *
     * @param userBase 用户信息
     */
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @PatchMapping("/updateUser")
    public Result updateUser(@RequestBody UserBase userBase) {
        return userService.updateUser(userBase);
    }

    /**
     * 获取个人信息
     */
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @GetMapping("/getUserInfo")
    public Result getUserInfo() {
        return userService.getUserInfo();
    }




    /**
     * 创建用户
     *
     * @param name 用户名
     * @param lastName 姓
     * @param firstName 名
     * @param role 身份
     * @param idNumber 身份证号码
     */
    @RequiredLogin
    @PostMapping("/addUser")
    public Result addUser(@RequestParam String name,
                          @RequestParam String lastName,
                          @RequestParam String firstName,
                          @RequestParam Boolean gender,
                          @RequestParam String role,
                          @RequestParam String idNumber,
                          @RequestParam(required = false, defaultValue = "") String phone,
                          @RequestParam(required = false, defaultValue = "") String mail) {
        return userService.addUser(name, lastName, firstName, gender, role, idNumber, phone, mail);
    }

//    /**
//     * 批量删除用户
//     *
//     * @param userIds 用户集合
//     */
//    @DeleteMapping("/deleteUser")
//    public Result deleteUser(@RequestBody List<Integer> userIds) {
//        return userService.deleteUser(userIds);
//    }

    /**
     * 修改是否需要校验身份证号码
     *
     * @param isNeedCheck 是否需要校验
     */
    @RequiredLogin
    @PatchMapping("/modifyIDNeedCheck")
    public Result modifyIDNeedCheck(@RequestParam Boolean isNeedCheck) {
        return userService.modifyIDNeedCheck(isNeedCheck);
    }

    /**
     * 获取所有用户信息
     */
    @RequiredLogin
    @GetMapping("/searchUser")
    public Result searchUser(@RequestParam(required = false, defaultValue = "") String name,
                             @RequestParam(required = false, defaultValue = "") Boolean gender,
                             @RequestParam(required = false, defaultValue = "") String lastName,
                             @RequestParam(required = false, defaultValue = "") String phone,
                             @RequestParam(required = false, defaultValue = "") String mail,
                             @RequestParam(required = false, defaultValue = "") String role,
                             @RequestParam(required = false, defaultValue = "") String idNumber,
                             @RequestParam Integer current,
                             @RequestParam Integer size) {
        return userService.searchUser(name, gender, lastName, phone, mail, role, idNumber, current, size);
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
