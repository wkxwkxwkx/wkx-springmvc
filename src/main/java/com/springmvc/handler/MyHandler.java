package com.springmvc.handler;

import java.lang.reflect.Method;

/**
 * @project: mvcsourcecode
 * @description: 自定义的url和方法的映射类
 * @author: 王凯旋
 * @date: 2022/7/30 20:50:57
 * @version: 1.0
 */
public class MyHandler {
    //路径
    private String url;
    //哪个类
    private Object controller;
    //哪个方法
    private Method method;

    public MyHandler() {
    }

    public MyHandler(String url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "MyHandler{" +
                "url='" + url + '\'' +
                ", controller=" + controller +
                ", method=" + method +
                '}';
    }
}
