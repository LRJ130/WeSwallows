package com.lrm.dao;

import com.lrm.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository是负责分页查找的接口
public interface UserRepository extends JpaRepository<User, Long>
{
    //这是Spring Data默认的命名方法的规范 它已经具有了意义 相当于已经被实现了 关键字And 只要符合规范就能解析
    //注册
    User findByUsernameAndPassword(String username, String password);
    User findByNickname(String nickname);
    //登录
    User findByUsername(String username);

}
