package com.lrm.service;

import com.lrm.po.User;

import java.util.List;

public interface UserService {

    User checkRegister(String username, String nickname);

    User saveUser(String username, String password, String nickname);

    User checkUser(String username, String password);

    User updateUser(User user);

    User getUser(Long userId);

    User getUser(String nickname);

    Long countUser();

    List<User> listTopUsers(int size);
}
