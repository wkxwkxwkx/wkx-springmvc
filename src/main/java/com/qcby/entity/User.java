package com.qcby.entity;

/**
 * @project: mvcsourcecode
 * @description: 用户实体类
 * @author: 王凯旋
 * @date: 2022/7/30 12:39:52
 * @version: 1.0
 */
public class User {
    private Integer id;
    private String name;
    private String pass;

    public User(Integer id, String name, String pass) {
        this.id = id;
        this.name = name;
        this.pass = pass;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
