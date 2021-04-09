package com.lrm.web;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RankController {
    @Autowired
    UserService userService;

    /**
     * @return 按贡献值排序的用户集合 选择前十
     */
    @GetMapping("/rank")
    Result<Map<String, Object>> donationRank() {
        Map<String, Object> hashMap = new HashMap<>();
        //十个用户
        List<User> users = userService.listTopUsers(10);
        hashMap.put("users", users);
        return new Result<>(hashMap, true, null);
    }
}
