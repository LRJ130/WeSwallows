package com.lrm.web;

import com.lrm.po.User;
import com.lrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
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
            return "/注册页面";
        } else {
            userService.saveUser(username, password, nickname);
            //要重定向 否则路径会出错
            return "redirect:/登录页面";
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
            //因为user是要传递到前端的 这个操作是为了不把密码这个字段传递到前端
            // 不会同步到数据库中
            user.setPassword(null);
            session.setAttribute("user", user);
            return "首页页面";
        } else {
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/登录页面";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.removeAttribute("user");
        return "redirect:/首页页面";
    }
}
