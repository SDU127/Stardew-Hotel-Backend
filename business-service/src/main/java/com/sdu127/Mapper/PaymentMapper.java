package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.DTO.PaymentDTO;
import com.sdu127.Data.PO.Payment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 缴费Mapper
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
    /**
     * 删除用户相关付款信息
     *
     * @param userId 用户id
     */
    @Delete("DELETE FROM payment WHERE user_id = #{userId}")
    void deleteByUserId(Integer userId);

    /**
     * 搜索付款记录
     *
     */
    @Select("SELECT payment.*, CONCAT(u1.last_name, u1.first_name) AS userFullName, u1.phone AS userPhone " +
            "FROM payment " +
            "LEFT JOIN user u1 ON payment.user_id = u1.id " +
            "WHERE ${ew.sqlSegment}")
    IPage<PaymentDTO> searchPaymentList(Page<Payment> page, @Param("ew") Wrapper<Payment> queryWrapper);

    /**
     * 获取指定未支付的付款信息
     *
     * @param type 类型
     * @param userId 用户id
     * @param payTo 付款对象
     */
    @Select("SELECT * FROM payment WHERE type = #{type} AND user_id = #{userId} AND pay_to = #{payTo} AND is_paid = 0")
    PaymentDTO getUnpaidPayment(Integer type, Integer userId, Integer payTo);
}
