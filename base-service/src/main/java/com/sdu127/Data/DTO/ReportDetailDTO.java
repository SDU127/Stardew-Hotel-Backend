package com.sdu127.Data.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sdu127.Data.PO.ReportDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportDetailDTO extends ReportDetail {
    /**
     * 处理人姓名
     */
    @TableField(exist = false)
    private String dealerFullName;

    /**
     * 处理人电话
     */
    @TableField(exist = false)
    private String dealerPhone;
}
