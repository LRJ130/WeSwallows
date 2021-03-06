package com.lrm.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;

public class Methods {
    //得到当前UserId
    public static Long getCustomUserId(HttpServletRequest request) throws JWTVerificationException
    {
        String token = request.getHeader("token");
        DecodedJWT decodedJWT = JWTUtils.getToken(token);
        Long userId = decodedJWT.getClaim("userId").asLong();
        return userId;
    }

    //验证是否是管理页
    public static Boolean isAdmin(HttpServletRequest request) throws JWTVerificationException
    {
        String token = request.getHeader("token");
        DecodedJWT decodedJWT = JWTUtils.getToken(token);
        Boolean isAdmin = decodedJWT.getClaim("isAdmin").asBoolean();
        return isAdmin;
    }
}
