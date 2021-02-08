package com.lrm.web.admin;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/search")
    public Result<Map<String, Object>> searchCustomer(String nickname)
    {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userService.getUser(nickname));
        return new Result<>(hashMap, true, "搜索完成");
    }

    //禁言/解禁
    @GetMapping("/controlSpeak/{userId}")
    public void controlSpeak(@PathVariable Long userId)
    {
        User user = userService.getUser(userId);
        user.setCanSpeak(!user.getCanSpeak());
    }
}
