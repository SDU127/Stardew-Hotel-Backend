package com.sdu127.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu127.Data.Constant.CurrentUser;
import com.sdu127.Data.Constant.RedisKey;
import com.sdu127.Data.Constant.ResponseData;
import com.sdu127.Data.Constant.VerificationCodeMode;
import com.sdu127.Data.Objects.UserBase;
import com.sdu127.Data.PO.User;
import com.sdu127.Data.VO.Result;
import com.sdu127.Exception.Exceptions.InfoMessage;
import com.sdu127.Mapper.CommonMapper;
import com.sdu127.Mapper.UserMapper;
import com.sdu127.Util.CommonUtil;
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
    CommonMapper commonMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    CommonUtil commonUtil;
    @Resource
    RedisUtil redisUtil;

    private static boolean needCheck = false;

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
     * 创建用户
     */
    @Transactional
    public Result addUser(String userName, String lastName, String firstName, Boolean gender, String role, String idNumber, String phone, String mail) {
        // 判断用户名是否重复
        if (userMapper.userNameExist(userName)) throw new InfoMessage(ResponseData.USER_NAME_USED);

        // 判断身份是否存在
        if (!commonMapper.roleExist(role)) throw new InfoMessage(ResponseData.ROLE_ERROR);

        // 判断名字是否传入
        if (lastName.isEmpty() || firstName.isEmpty()) throw new InfoMessage(ResponseData.NAME_ERROR);

        // 校验身份证号码
        if (needCheck && !commonUtil.isValidIdNumber(idNumber)) throw new InfoMessage(ResponseData.ID_FORMAT_ERROR);

        // 密码加密并存储，身份证后6位作为初始密码
        if (idNumber.length() < 6) throw new InfoMessage(ResponseData.ID_FORMAT_ERROR);
        String hashedPassword = BCrypt.hashpw(idNumber.substring(idNumber.length() - 6), BCrypt.gensalt());

        User user = User.builder()
                .id(null)
                .name(userName)
                .lastName(lastName)
                .firstName(firstName)
                .gender(gender)
                .password(hashedPassword)
                .mail(null)
                .role(role)
                .idNumber(idNumber)
                .phone(phone)
                .mail(mail)
                .build();

        // 插入用户
        userMapper.insert(user);
        return Result.ok();
    }

//    /**
//     * 批量删除用户
//     *
//     * @param userIds 用户集合
//     */
//    @Transactional
//    public Result deleteUser(List<Integer> userIds) {
//        for (Integer userId : userIds) {
//            houseMapper.deleteByUserId(userId);
//            parkingSpotMapper.deleteByUserId(userId);
//            paymentMapper.deleteByUserId(userId);
//            reportMapper.deleteReportImagesByUserId(userId);
//            reportMapper.deleteReportByUserId(userId);
//            noticeMapper.deleteReadByUserId(userId);
//
//            userMapper.deleteById(userId);
//        }
//        return Result.ok();
//    }

    /**
     * 修改是否需要校验身份证号码
     */
    public Result modifyIDNeedCheck(Boolean isNeedCheck) {
        needCheck = isNeedCheck;
        return Result.ok();
    }

    /**
     * 获取所有用户信息
     */
    public Result searchUser(String name, Boolean gender, String lastName, String phone, String mail, String role, String idNumber,  Integer current, Integer size) {
        Page<User> page = new Page<>(current, size);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        if (gender != null) {
            queryWrapper.eq(User::getGender, gender);
        }

        if (!role.isEmpty()) {
            queryWrapper.eq(User::getRole, role);
        }

        queryWrapper.like(User::getName, name)
                .apply("CONCAT(last_name, first_name) LIKE {0}", "%" + lastName + "%")
                .like(User::getPhone, phone)
                .like(User::getMail, mail)
                .like(User::getIdNumber, idNumber);

        IPage<User> resultPage = userMapper.selectPage(page, queryWrapper);

        return Result.success(resultPage);
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
        return userMapper.selectOne(queryWrapper);
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
