package com.sdu127.Data.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("parking_spot")
public class ParkingSpot {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 位置
     */
    private String area;

    /**
     * 位置
     */
    private Integer position;

    /**
     * 0-闲置，1-租用
     */
    private Integer isRented;

    /**
     * 租用价格
     */
    private Double rentalFee;

    /**
     * 所有者id
     */
    private Integer userId;

    /**
     * 车牌号
     */
    private String carNumber;
}
