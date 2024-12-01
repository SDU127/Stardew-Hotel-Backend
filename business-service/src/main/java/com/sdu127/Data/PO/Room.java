package com.sdu127.Data.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("room")
public class Room {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 几楼
     */
    private Integer floor;

    /**
     * 几室
     */
    private Integer number;

    /**
     * 面积
     */
    private Double area;

    /**
     * 房型
     */
    private String type;

    /**
     * 费用
     */
    private Double fee;

    /**
     * 折扣
     */
    private double discount;

    /**
     * VIP折扣
     */
    private double vipDiscount;

    public boolean checkNull() {
        return floor == null || number == null || area == null || type == null || fee == null;
    }
    
    public void modifyDiscount() {
        if (discount <= 0 || discount > 1) discount = 1;
        if (vipDiscount <= 0 || vipDiscount > 1) vipDiscount = 1;
    }
}
