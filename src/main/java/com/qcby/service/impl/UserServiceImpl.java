package com.qcby.service.impl;

import com.qcby.entity.User;
import com.qcby.service.UserService;
import com.springmvc.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @project: mvcsourcecode
 * @description: 用户业务层的实现层
 * @author: 王凯旋
 * @date: 2022/7/30 12:44:17
 * @version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public List<User> findUser(String name) {
        System.out.println("查询的参数是:"+name);
        List<User> users = new ArrayList();
        users.add(new User(1,"舒克","123"));
        users.add(new User(2,"贝塔","234"));
        users.add(new User(3,"坦克","345"));
        users.add(new User(4,"飞机","456"));
        return users;
    }

}
