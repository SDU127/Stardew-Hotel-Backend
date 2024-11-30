package com.sdu127.Data.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sdu127.Data.PO.ParkingSpot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParkingSpotDTO extends ParkingSpot {
    /**
     * 所有者姓
     */
    @TableField(exist = false)
    private String lastName;

    /**
     * 所有者名
     */
    @TableField(exist = false)
    private String firstName;

    /**
     * 所有者联系方式
     */
    @TableField(exist = false)
    private String phone;
}
