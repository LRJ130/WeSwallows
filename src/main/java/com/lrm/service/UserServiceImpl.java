package com.lrm.service;

import com.lrm.dao.UserRepository;
import com.lrm.po.User;
import com.lrm.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class UserServiceimpl implements UserService
{
    //依赖注入 在某类中应用其他类的方法 需要调用这个类的对象 这就是依赖
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkRegister(String username, String nickname) {
        User user1 = userRepository.findByUsername(username);
        User user2 = userRepository.findByNickname(nickname);
        //不返回就是返回null
        if(user1 != null)
        {
            return user1;
        } else return user2;
    }

    @Override
    public void saveUser(String username, String password, String nickname) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(MD5Utils.code(password));
        user.setNickname(nickname);
        userRepository.save(user);
    }

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,MD5Utils.code(password));
    }

}
