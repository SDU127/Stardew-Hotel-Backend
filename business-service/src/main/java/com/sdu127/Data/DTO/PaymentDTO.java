package com.sdu127.Data.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sdu127.Data.PO.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentDTO extends Payment {
    /**
     * 付款人姓名
     */
    @TableField(exist = false)
    private String userFullName;

    /**
     * 付款人联系方式
     */
    @TableField(exist = false)
    private String userPhone;
}
