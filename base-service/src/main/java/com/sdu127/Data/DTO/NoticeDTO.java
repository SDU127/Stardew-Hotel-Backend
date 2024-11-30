package com.sdu127.Data.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sdu127.Data.PO.Notice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NoticeDTO extends Notice {
    /**
     * 姓名
     */
    @TableField(exist = false)
    private String userFullName;

    /**
     * 是否已读
     */
    @TableField(exist = false)
    private Integer isRead;
}
