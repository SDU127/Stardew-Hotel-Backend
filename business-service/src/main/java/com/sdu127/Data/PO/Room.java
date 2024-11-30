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
@TableName("house")
public class Room {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 几号楼
     */
    private Integer building;

    /**
     * 几楼
     */
    private Integer floor;

    /**
     * 几室
     */
    private Integer room;

    /**
     * 面积
     */
    private Double area;

    /**
     * 房型
     */
    private String type;

    /**
     * 物业费
     */
    private Double fee;

    /**
     * 是否有人居住
     */
    private Integer isUsed;

    /**
     * 居住者
     */
    private Integer userId;
}
