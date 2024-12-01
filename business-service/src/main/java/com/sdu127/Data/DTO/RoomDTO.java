package com.sdu127.Data.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sdu127.Data.PO.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoomDTO extends Room {
    /**
     * 是否正在被使用
     */
    private Integer isUsed;

    private Integer userId;

    /**
     * 姓名
     */
    @TableField(exist = false)
    private String fullName;

    /**
     * 手机号
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 身份证号
     */
    private String idNumber;
}
