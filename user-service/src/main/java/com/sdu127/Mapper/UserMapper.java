package com.sdu127.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sdu127.Data.Objects.UserBase;
import com.sdu127.Data.PO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 更新用户邮箱
     *
     * @param userId 用户id
     * @param userMail 用户邮箱
     */
    @Update("UPDATE user SET mail = #{userMail} WHERE id = #{userId}")
    void updateMail(Integer userId, String userMail);

    /**
     * 更新密码
     *
     * @param userId 用户id
     * @param newPassword 新密码
     */
    @Update("UPDATE user SET password = #{newPassword} WHERE id = #{userId}")
    void updatePassword(Integer userId, String newPassword);

    /**
     * 更新用户名
     *
     * @param userId 用户id
     * @param userName 用户名
     */
    @Update("UPDATE user SET name = #{userName} WHERE id = #{userId}")
    void updateUserName(Integer userId, String userName);

    /**
     * 更新头像
     *
     * @param userId 用户id
     * @param avatar 头像
     */
    @Update("UPDATE user SET avatar = #{avatar} WHERE id = #{userId}")
    void updateAvatar(Integer userId, String avatar);

    /**
     * 更新手机号
     *
     * @param userId 用户id
     * @param phone 手机号
     */
    @Update("UPDATE user SET phone = #{phone} WHERE id = #{userId}")
    void updatePhone(Integer userId, String phone);

    /**
     * 获取个人信息
     *
     * @param userId 用户id
     */
    @Select("SELECT * FROM user WHERE id = #{userId}")
    UserBase getUserInfo(Integer userId);


    /**
     * 获取所有用户信息
     */
    @Select("SELECT * FROM user")
    List<UserBase> getAllUser(RowBounds rowBounds);

    /**
     * 获取用户数量
     *
     */
    @Select("SELECT COUNT(*) FROM user")
    Integer getAllUserCount();

    /**
     * 通过用户名获取用户信息
     *
     * @param userName 用户昵称
     */
    @Select("SELECT * FROM user WHERE user.name = #{userName}")
    User getUserByName(String userName);

    /**
     * 通过用户邮箱获取用户信息
     *
     * @param userMail 用户邮箱
     */
    @Select("SELECT * FROM user WHERE user.mail = #{userMail}")
    User getUserByMail(String userMail);


    /**
     * 判断用户名是否存在
     *
     * @param userName 用户名
     */
    @Select("SELECT COUNT(*) > 0 FROM user WHERE name = #{userName}")
    Boolean userNameExist(String userName);

    /**
     * 判断邮箱是否存在
     *
     * @param mail 邮箱
     */
    @Select("SELECT COUNT(*) > 0 FROM user WHERE mail = #{mail}")
    boolean mailExist(String mail);
}
