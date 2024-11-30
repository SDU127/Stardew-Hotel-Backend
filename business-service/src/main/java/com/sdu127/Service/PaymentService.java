package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.DTO.PaymentDTO;
import com.sdu127.Data.PO.Payment;
import com.sdu127.Data.VO.Result;
import com.sdu127.Mapper.PaymentMapper;
import com.sdu127.exception.Exceptions.InfoMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class PaymentService {
    @Resource
    PaymentMapper paymentMapper;

    /**
     * 获取付款列表
     */
    public Result getPaymentList(Integer type, Integer isPaid, Integer current, Integer size) {
        Page<Payment> page = new Page<>(current, size);

        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(Payment::getContent, "");

        if (type != -1) {
            queryWrapper.eq(Payment::getType, type);
        }

        if (isPaid != -1) {
            queryWrapper.eq(Payment::getIsPaid, isPaid);
        }

        queryWrapper.orderByDesc(Payment::getTime);

        IPage<PaymentDTO> resultPage = paymentMapper.searchPaymentList(page, queryWrapper);

        return Result.success(resultPage);
    }

    /**
     * 获取我的缴费信息
     */
    public Result getMyPaymentList(Integer type, Integer isPaid, Integer current, Integer size) {
        Page<Payment> page = new Page<>(current, size);

        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Payment::getUserId, CurrentUser.getId());

        queryWrapper.like(Payment::getContent, "");

        if (type != -1) {
            queryWrapper.eq(Payment::getType, type);
        }

        if (isPaid != -1) {
            queryWrapper.eq(Payment::getIsPaid, isPaid);
        }

        queryWrapper.orderByDesc(Payment::getTime);

        IPage<PaymentDTO> resultPage = paymentMapper.searchPaymentList(page, queryWrapper);

        return Result.success(resultPage);
    }

    /**
     * 付款
     */
    @Transactional
    public Result pay(Integer paymentId) {
        Payment payment = paymentMapper.selectById(paymentId);
        if (payment == null || !Objects.equals(payment.getUserId(), CurrentUser.getId())) throw new InfoMessage(ResponseData.PAYMENT_NOT_FOUND);

        if (payment.getIsPaid() == 1) throw new InfoMessage(ResponseData.ALREADY_PAID);

        payment.setIsPaid(1);
        payment.setPayTime(LocalDateTime.now());
        paymentMapper.updateById(payment);

        return Result.ok();
    }
}
