package com.sdu127.Data.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 缴费
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("payment")
public class Payment {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 费用类型
     * 1-房屋，2-车位，3-维修等
     */
    private Integer type;

    /**
     * 费用内容
     */
    private String content;

    /**
     * 付款人
     */
    private Integer userId;

    /**
     * 费用
     */
    private Double fee;

    /**
     * 付款目标
     */
    private Integer payTo;

    /**
     * 是否付款
     */
    private Integer isPaid;

    /**
     * 发布时间
     */
    private LocalDateTime time;

    /**
     * 截止日期
     */
    private LocalDateTime deadline;

    /**
     * 付款时间
     */
    private LocalDateTime payTime;
}
