package com.lrm.web;

import com.lrm.po.User;
import com.lrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String nickname,
                        RedirectAttributes attributes)
    {
        //先检查是否已经注册过。注册过报错；没注册过注册，跳转到登录。
        User user = userService.checkUser(username, nickname);
        if(user !=null)
        {

            attributes.addFlashAttribute("message","该用户名或昵称已被注册");
            return "注册页面";
        } else {
            userService.saveUser(username, password, nickname);
            return "登录页面";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes)
    {
        //先检查用户名和密码在数据库中存在不。(不考虑是否注册过了)。存在登录；不存在报错。
        User user = userService.checkUser(username, password);
        if(user !=null)
        {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "首页页面";
        } else {
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "登录页面";
        }
    }
}
