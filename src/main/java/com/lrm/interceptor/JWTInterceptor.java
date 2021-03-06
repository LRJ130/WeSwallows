package com.lrm.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrm.util.JWTUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//基于路径判断 处理异常
//拦截有问题的登录行为
public class JWTInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        Map<String, Object> map = new HashMap<>();
        try {
            JWTUtils.verify(token);
            return true;
        //异常处理
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
