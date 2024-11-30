package com.sdu127.Controller;

import com.sdu127.Annotation.RequiredLogin;
import com.sdu127.Data.Constant.Role;
import com.sdu127.Data.VO.Result;
import com.sdu127.Service.PaymentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Resource
    PaymentService paymentService;

    /**
     * 获取缴费信息
     *
     */
    @RequiredLogin(roles = {Role.SERVER})
    @GetMapping("/getPaymentList")
    public Result getPaymentList(@RequestParam(required = false, defaultValue = "-1") Integer type,
                                   @RequestParam(required = false, defaultValue = "-1") Integer isPaid,
                                   @RequestParam Integer current,
                                   @RequestParam Integer size) {
        return paymentService.getPaymentList(type, isPaid, current, size);
    }

    /**
     * 获取缴费信息
     *
     */
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @GetMapping("/getMyPaymentList")
    public Result getMyPaymentList(@RequestParam(required = false, defaultValue = "-1") Integer type,
                                   @RequestParam(required = false, defaultValue = "-1") Integer isPaid,
                                   @RequestParam Integer current,
                                   @RequestParam Integer size) {
        return paymentService.getMyPaymentList(type, isPaid, current, size);
    }

    /**
     * 付款
     *
     * @param paymentId 缴费id
     */
    @RequiredLogin(roles = {Role.SERVER, Role.CLIENT})
    @PostMapping("/pay")
    public Result pay(@RequestParam Integer paymentId) {
        return paymentService.pay(paymentId);
    }
}
