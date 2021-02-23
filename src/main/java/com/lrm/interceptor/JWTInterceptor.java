package com.lrm.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrm.util.JWTUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//基于路径判断 处理异常
//拦截用户进入其他用户的管理页
//拦截有问题的登录行为
public class JWTInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在请求头中取得token
        String token = request.getHeader("token");
        //在请求头中取得userId
        Long userId = Long.parseLong(request.getHeader("userId"));
        Map<String, Object> map = new HashMap<>();
        //异常处理
        try {
            JWTUtils.verify(token);
            DecodedJWT decodedJWT = JWTUtils.getToken(token);
            Long customUserId = decodedJWT.getClaim("userId").asLong();
            if(!userId.equals(customUserId)) {
                map.put("isSuccess", false);
                map.put("msg","无访问权限");
            } else {
                return true;
            }
        } catch (TokenExpiredException e) {
            map.put("isSuccess", false);
            map.put("msg", "用户令牌已经过期，请重新登陆");
        } catch (SignatureVerificationException e){
            map.put("isSuccess", false);
            map.put("msg", "签名错误");
        } catch (AlgorithmMismatchException e){
            map.put("isSuccess", false);
            map.put("msg", "加密算法不匹配");
        } catch (Exception e) {
            map.put("isSuccess", false);
            map.put("msg", "无效令牌");
        }
        //转化为json返回前端
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);

        return false;
    }
}
