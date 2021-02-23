package com.lrm.web;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.util.JWTUtils;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
        User user = userService.checkUser(username, nickname);
        if(user !=null)
        {
            //跳转到注册页面
            return new Result<>(user, false, "该用户名或昵称已被注册过");
        } else {
            user = userService.saveUser(username, password, nickname);
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
            //只需要把userId和isAdmin放在请求头里便于拦截 其他东西在需要的时候可以另外返回
            map.put("userId", user.getId().toString());
            map.put("isAdmin", user.getIsAdmin().toString());
            String token = JWTUtils.getToken(map);
            //返回首页
            return new Result<>(token, true, "登录成功");
        } else {
            //返回登录页面
            return new Result<>(null, false,"用户名或密码错误");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request)
    {
        //返回首页
        request.removeAttribute("token");
    }
}
