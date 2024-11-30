package com.sdu127.Data.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sdu127.Data.PO.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportDTO extends Report {

    /**
     * 发布者姓名
     */
    @TableField(exist = false)
    private String fullName;

    /**
     * 发布者电话
     */
    @TableField(exist = false)
    private String phone;

    /**
     * 相关图片
     */
    @TableField(exist = false)
    private List<String> images;

    /**
     * 处理人姓名
     */
    @TableField(exist = false)
    private String dealerFullName;

    /**
     * 处理人联系方式
     */
    @TableField(exist = false)
    private String dealerPhone;

    /**
     * 投诉处理列表
     */
    @TableField(exist = false)
    private List<ReportDetailDTO> details;
}
