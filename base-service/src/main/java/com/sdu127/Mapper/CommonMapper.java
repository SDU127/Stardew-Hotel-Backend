package com.sdu127.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * 基础Mapper
 */
@Mapper
public interface CommonMapper {
    /**
     * 通过请求路径获取请求权限要求
     */
    @Select("SELECT permission FROM path_permission WHERE path = #{path}")
    Optional<Integer> getPathLevel(String path);

    /**
     * 获取身份列表
     */
    @Select("SELECT role FROM role_permission")
    List<String> getRoles();

    /**
     * 获取身份拥有的权限组
     *
     * @param role 身份
     */
    @Select("SELECT permission FROM role_permission WHERE role = #{role}")
    String getRolePermission(String role);

    /**
     * 获取身份是否存在
     *
     * @param role 身份
     */
    @Select("SELECT COUNT(*) > 0 FROM role_permission WHERE role = #{role}")
    Boolean roleExist(String role);
}
