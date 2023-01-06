package org.xiaowu.behappy.common.core.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 获取当前用户信息工具类
 * @author xiaowu
 */
public class AuthContextHolder {
    //获取当前用户id
    public static Long getUserId(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }
    //获取当前用户名称
    public static String getUserName(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}
