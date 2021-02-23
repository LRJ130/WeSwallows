package com.lrm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {


    private static final String TOKEN = "token!Q@W3e4r";

    /**
     * 生成token
     * @param map  //传入payload
     * @return 返回token
     */
    public static String getToken(Map<String, String> map){
        JWTCreator.Builder builder = JWT.create();
        //添加到token
        map.forEach(builder::withClaim);
        //1小时之后过期
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR_OF_DAY,1);
        //添加到token
        builder.withExpiresAt(instance.getTime());
        //添加标签 返回token
        return builder.sign(Algorithm.HMAC256(TOKEN));
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }

    /**
     * 获取token中payload
     * @param token
     * @return
     */
    public static DecodedJWT getToken(String token){
        return JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }
}
