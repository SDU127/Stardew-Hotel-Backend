package com.sdu127.Controller;

import com.sdu127.Config.GitAutoRefreshConfig;
import com.sdu127.Config.GitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private GitConfig gitConfig;

    @Autowired
    private GitAutoRefreshConfig gitAutoRefreshConfig;

    @GetMapping(value = "show")
    public Object show(){
        return gitConfig;
    }

    @GetMapping(value = "autoShow")
    public Object autoShow(){
        return gitAutoRefreshConfig;
    }

    @PostMapping("/echo")
    public Object echo(@RequestParam String data) {
        return "Echo: " + data;
    }
}
