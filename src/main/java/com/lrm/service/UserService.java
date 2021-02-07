package com.lrm.service;

import com.lrm.po.User;

public interface UserService {
    //验证该用户是否已经注册
        //注册了的不能再注册（用户名或昵称是否已经存在）
        //注册了的可以直接登录
    User checkRegister(String username, String nickname);

    User saveUser(String username, String password, String nickname);

    User checkUser(String username, String password);

    User updateUser(User user);

    User getUser(Long userId);

    User getUser(String nickname);

    Long countUser();


}
