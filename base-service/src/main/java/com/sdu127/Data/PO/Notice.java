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
 * 通知
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("notice")
public class Notice {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 被通知人，全体为0
     */
    private Integer recipient;

    /**
     * 通知类型
     * 0-全体，1-全体业主，2-全体客服，10-指定用户
     */
    private Integer type;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 发布时间
     */
    private LocalDateTime time;
}
