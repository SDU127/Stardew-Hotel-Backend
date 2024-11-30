package com.sdu127.Util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 通用工具
 */
@Component
public class CommonUtil {
    private static final String ID_PATTERN = "^\\d{17}[\\dX]$"; // 身份证号码格式
    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; // 系数
    private static final char[] CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'}; // 校验码对应的数字

    /**
     * 校验身份证号
     *
     * @param idNumber 身份证号
     */
    public boolean isValidIdNumber(String idNumber) {
        if (idNumber == null || idNumber.length() != 18) {
            return false; // 长度不正确
        }

        // 前17位必须是数字，第18位可以是数字或 'X'
        if (!Pattern.matches(ID_PATTERN, idNumber)) {
            return false;
        }

        // 计算校验码
        char[] idArray = idNumber.toCharArray();
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idArray[i] - '0') * WEIGHTS[i];
        }

        // 对11取模，得到应当的校验码
        int mod = sum % 11;
        char expectedCheckCode = CHECK_CODES[mod];

        // 比较最后一位的校验码是否正确
        return idArray[17] == expectedCheckCode;
    }
}
