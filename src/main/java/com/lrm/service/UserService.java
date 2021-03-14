package com.lrm.service;

import com.lrm.po.User;

public interface UserService {

    User checkRegister(String username, String nickname);

    User saveUser(String username, String password, String nickname);

    User checkUser(String username, String password);

    User updateUser(User user);

    User getUser(Long userId);

    User getUser(String nickname);

    Long countUser();


}
