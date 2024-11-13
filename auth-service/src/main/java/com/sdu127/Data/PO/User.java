package com.sdu127.Data.PO;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sdu127.Data.Objects.UserBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

/**
 * 用户表
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends UserBase {
    /**
     * 密码
     */
    private String password;
}
