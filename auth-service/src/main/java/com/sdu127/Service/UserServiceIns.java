package com.sdu127.Service;

import com.sdu127.Data.PO.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user-service", path = "/user")
public interface UserServiceIns {
    // 通过用户id获取用户
    @PostMapping("/get-user/{userId}")
    User getUser(@PathVariable Integer userId);

    // 通过邮箱获取用户
    @GetMapping("/get-user-by-mail")
    User getUserByMail(@RequestParam String mail);

    // 通过用户名/邮箱获取用户
    @GetMapping("/get-user-by-name-or-mail")
    User getUserByNameOrMail(@RequestParam String nameOrMail);

    // 判断用户名是否存在
    @GetMapping("/user-name-exist")
    Boolean userNameExist(@RequestParam String userName);

    // 判断邮箱是否存在
    @GetMapping("/mail-exist")
    Object mailExist(@RequestParam String mail);

    // 更新用户信息
    @PostMapping("/update-user")
    void updateUser(@RequestParam String userMail, @RequestParam String newPassword);
}
