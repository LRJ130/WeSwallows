package com.lrm.web;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    //注意这里需要返回<User> 需要确定泛型，否则操作无效了
    @PostMapping("/register")
    public R<User> login(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String nickname)
    {
        //先检查是否已经注册过。注册过报错；没注册过注册，注册成功跳转到登录。
        User user = userService.checkUser(username, nickname);
        if(user !=null)
        {
            //跳转到注册页面
            return new R<>(user, false, "该用户名或昵称已被注册过");
        } else {
            userService.saveUser(username, password, nickname);
            //跳转到登录页面
            return new R<>(user, true, "注册成功");
        }
    }

    @PostMapping("/login")
    public R<User> login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session)
    {
        //先检查用户名和密码在数据库中存在不。(不考虑是否注册过了)。存在登录；不存在报错。
        User user = userService.checkUser(username, password);
        if(user !=null)
        {
            //因为user是要传递到前端的 这个操作是为了不把密码这个字段传递到前端 但不会同步到数据库中
            user.setPassword(null);
            session.setAttribute("user", user);
            //返回首页
            return new R<>(user, true, "登录成功");
        } else {
            //返回登录页面
            return new R<>(null, false,"用户名或密码错误");
        }
    }

    @GetMapping("/logout")
    public void logout(HttpSession session)
    {
        //返回首页
        session.removeAttribute("user");
    }
}
