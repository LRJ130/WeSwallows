package com.lrm.web;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.util.JWTUtils;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    //注意这里需要返回<User> 需要确定泛型，否则操作无效了
    @PostMapping("/register")
    public Result<User> login(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String nickname)
    {
        //先检查是否已经注册过。注册过报错；没注册过注册，注册成功跳转到登录。
            //封装成游离态的User对象 不要返回密码到前端
        User user0 = new User();
        user0.setUsername(username);
        user0.setNickname(nickname);
        User user = userService.checkRegister(username, nickname);
        if(user !=null)
        {
            //跳转到注册页面
            return new Result<>(user0, false, "该用户名或昵称已被注册过");
        } else {
            user = userService.saveUser(username, password, nickname);
                //不要返回密码到前端
            user.setPassword(null);
            //跳转到登录页面
            return new Result<>(user, true, "注册成功");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestParam String username,
                        @RequestParam String password)
    {
        //先检查用户名和密码在数据库中存在不。(不考虑是否注册过了)。存在登录；不存在报错。
        User user = userService.checkUser(username, password);

        if(user !=null)
        {
            //需要传递到前端的 包含在token内的信息 map用来存放payload
            Map<String, String> map = new HashMap<>();
            //把这些字段放在请求头里 其他东西在需要的时候可以另外返回
            map.put("userId", user.getId().toString());
            map.put("nickname", user.getNickname());
            map.put("avatar", user.getAvatar());
            map.put("isAdmin", user.getIsAdmin().toString());
            map.put("canSpeak", user.getCanSpeak().toString());
            String token = JWTUtils.getToken(map);
            //返回首页
            return new Result<>(token, true, "登录成功");
        } else {
            //返回登录页面
            return new Result<>(null, false,"用户名或密码错误");
        }
    }
}
