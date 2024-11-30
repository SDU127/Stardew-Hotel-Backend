package com.sdu127.Data.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("report_detail")
public class ReportDetail {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 投诉id
     */
    private Integer reportId;

    /**
     * 处理人
     */
    private Integer dealer;

    /**
     * 处理方式
     */
    private String dealContent;

    /**
     * 处理时间
     */
    private LocalDateTime dealTime;

    /**
     * 评价
     */
    private Integer remark;

    /**
     * 评价内容
     */
    private String remarkContent;

    /**
     * 评价时间
     */
    private LocalDateTime remarkTime;
}
