package com.sdu127.Data.PO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 投诉
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("report")
public class Report {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 发布者
     */
    private Integer userId;

    /**
     * 投诉类型，0-意见建议，10-维修上报
     */
    private Integer type;

    /**
     * 举报内容
     */
    private String content;

    /**
     * 处理进度，0-待接取，1-受理中，2-待评分，3-完成
     */
    private Integer dealStatus;

    /**
     * 最后处理人
     */
    private Integer lastDealer;

    /**
     * 发布时间
     */
    private LocalDateTime time;

    public Report(Integer userId, Integer type, String content) {
        this.userId = userId;
        this.type = type;
        this.content = content;
    }
}
