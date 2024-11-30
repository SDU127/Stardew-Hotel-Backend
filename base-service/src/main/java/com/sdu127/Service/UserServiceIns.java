package com.sdu127.Service;

import com.sdu127.Data.PO.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "user-service", path = "/user")
public interface UserServiceIns {
    // 通过用户id获取用户
    @PostMapping("/get-user/{userId}")
    User getUser(@PathVariable Integer userId);
}
