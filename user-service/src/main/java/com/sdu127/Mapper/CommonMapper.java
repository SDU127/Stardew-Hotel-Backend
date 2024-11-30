package com.sdu127.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 基础Mapper
 */
@Mapper
public interface CommonMapper {
    /**
     * 获取身份是否存在
     *
     * @param role 身份
     */
    @Select("SELECT COUNT(*) > 0 FROM role_permission WHERE role = #{role}")
    Boolean roleExist(String role);
}
