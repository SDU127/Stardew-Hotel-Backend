package com.sdu127.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 基础Mapper
 */
@Mapper
public interface CommonMapper {
    /**
     * 获取身份列表
     */
    @Select("SELECT role FROM role_permission")
    List<String> getRoles();
}
