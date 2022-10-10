package com.qcby.service;

import com.qcby.entity.User;

import java.util.List;

/**
 * @project: mvcsourcecode
 * @description: 业务层的用户方法
 * @author: 王凯旋
 * @date: 2022/7/30 12:38:51
 * @version: 1.0
 */
public interface UserService {
    List<User> findUser(String name);
}
