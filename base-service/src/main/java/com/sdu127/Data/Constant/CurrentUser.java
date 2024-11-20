package com.sdu127.Data.Constant;

/**
 * 记录当前用户信息
 */
public class CurrentUser {
    /**
     * 用户id
      */
    private static final ThreadLocal<Integer> ID = new ThreadLocal<>();

    /**
     * 用户名
      */
    private static final ThreadLocal<String> USER_NAME = new ThreadLocal<>();

    /**
     * 身份
     */
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    public static void setAll(Integer id, String userName, String role) {
        ID.set(id);
        USER_NAME.set(userName);
        ROLE.set(role);
    }

    public static Integer getId() {
        return ID.get();
    }

    public static String getUserName() {
        return USER_NAME.get();
    }

    public static String getRole() {
        return ROLE.get();
    }
}
