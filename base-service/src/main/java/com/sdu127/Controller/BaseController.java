package com.sdu127.Controller;

import com.sdu127.Annotation.RequiredLogin;
import com.sdu127.Data.VO.Result;
import com.sdu127.Service.BaseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base")
public class BaseController {
    @Resource
    BaseService baseService;

    /**
     * 获取身份列表
     */
    @RequiredLogin
    @GetMapping("/getRoles")
    public Result getRoles() {
        return baseService.getRoles();
    }
}
