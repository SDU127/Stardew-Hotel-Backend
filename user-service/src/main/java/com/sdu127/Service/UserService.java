package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.RedisKey;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.Constant.VerificationCodeMode;
import com.sdu127.Data.Objects.UserBase;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Exception.Exceptions.InfoMessage;
import com.sdu127.Mapper.UserMapper;
import com.sdu127.Util.RedisUtil;
import jakarta.annotation.Resource;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 用户服务
 */
@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    RedisUtil redisUtil;

    /**
     * 绑定邮箱
     */
    @Transactional
    public Result bindMail(String userMail, String mailVerificationCode) {
        if (userMapper.mailExist(userMail)) throw new InfoMessage(ResponseData.MAIL_USED);

        // 校验邮件验证码
        String mode = String.valueOf(VerificationCodeMode.BIND_MAIL.getMode());
        String redisKey = RedisKey.MAIL_VERIFICATION_CODE.getKey(mode, userMail);
        if (!mailVerificationCode.equals(redisUtil.get(redisKey))) throw new InfoMessage(ResponseData.VERIFICATION_CODE_WRONG);

        redisUtil.deleteKey(redisKey);

        // 更新邮箱
        userMapper.updateMail(CurrentUser.getId(), userMail);

        return Result.ok();
    }

    /**
     * 更新密码
     */
    public Result modifyPassword(String oldPassword, String newPassword) {
        User user = getUser(CurrentUser.getId());

        // 判断密码是否正确
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) throw new InfoMessage(ResponseData.PASSWORD_WRONG);

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // 更新密码
        user.setPassword(hashedPassword);
        userMapper.updateById(user);

        return Result.ok();
    }

    /**
     * 更新用户
     */
    @Transactional
    public Result updateUser(UserBase userBase) {
        User user = getUser(userBase.getId());

        if (!Objects.equals(CurrentUser.getId(), userBase.getId()) && !Objects.equals(CurrentUser.getRole(), "管理员")) {
            throw new InfoMessage(ResponseData.FORBIDDEN);
        }

        user.update(userBase);
        userMapper.updateById(user);
        return Result.ok();
    }

    /**
     * 获取个人信息
     */
    public Result getUserInfo() {
        UserBase user = userMapper.getUserInfo(CurrentUser.getId());
        if (user == null) throw new InfoMessage(ResponseData.USER_NOT_FOUND);
        return Result.success(user);
    }

    /**
     * 用户id获取用户
     */
    public User getUser(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new InfoMessage(ResponseData.USER_NOT_FOUND);
        return user;
    }

    /**
     * 邮箱获取用户
     */
    public User getUserByMail(String mail) {
        User user = userMapper.getUserByMail(mail);
        if (user == null) throw new InfoMessage(ResponseData.MAIL_NOT_FOUND);
        return user;
    }

    /**
     * 通过用户名/邮箱获取用户
     *
     * @param nameOrMail 用户名/邮箱
     */
    public User getUserByNameOrMail(String nameOrMail) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", nameOrMail);
        queryWrapper.or().eq("mail", nameOrMail);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) throw new InfoMessage(ResponseData.USER_OR_MAIL_NOT_FOUND);
        return user;
    }

    public void updatePasswordByMail(String userMail, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("mail", userMail)
                .set("password", hashedPassword);

        userMapper.update(null, updateWrapper);
    }
}
