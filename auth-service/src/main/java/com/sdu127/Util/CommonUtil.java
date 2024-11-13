package com.sdu127.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具
 */
@Component
public class CommonUtil {
    @Value("${code.character-set}")
    private String CHARACTER_SET;
    @Value("${code.length}")
    private Integer length;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // 邮箱格式
    private static final String ID_PATTERN = "^\\d{17}[\\dX]$"; // 身份证号码格式
    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; // 系数
    private static final char[] CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'}; // 校验码对应的数字


    /**
     * 生成一定长度的随机字符串
     * @return 随机字符串
     */
    public String generateRandomString() {
        String characters = CHARACTER_SET;
        StringBuilder randomString = new StringBuilder();

        //随机组合
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }

        return randomString.toString();
    }

    /**
     * 判断是否符合邮件格式
     */
    public boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
