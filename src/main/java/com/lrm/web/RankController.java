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

/**
 * @author 山水夜止
 */
@RestController
public class RankController {
    @Autowired
    UserService userService;

    /**
     * @return 按贡献值排序的用户集合 选择前十
     */
    @GetMapping("/rank")
    Result<Map<String, Object>> donationRank() {
        Map<String, Object> hashMap = new HashMap<>(1);

        //返回十个贡献度最高的用户
        List<User> users = userService.listTopUsers(10);

        hashMap.put("users", users);

        return new Result<>(hashMap, true, null);
    }
}
