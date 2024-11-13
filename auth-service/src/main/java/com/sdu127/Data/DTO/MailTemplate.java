package com.sdu127.Data.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 邮件
 */
@Data
@AllArgsConstructor
public class MailTemplate {
    /**
     * 收件人
     */
    private String to;

    /**
     * 主题
     */
    private String subject;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 是否为Html
     */
    private boolean isHtml;
}