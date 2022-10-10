package com.qcby.controller;
import com.qcby.entity.User;
import com.qcby.service.UserService;
import com.springmvc.annotation.AutoWired;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.RequestMapping;
import com.springmvc.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @project: mvcsourcecode
 * @description: user的控制层
 * @author: 王凯旋
 * @date: 2022/7/30 12:51:53
 * @version: 1.0
 */
@Controller
public class UserController {
    //持有业务逻辑层的对象
    @AutoWired
    UserService userService;

    /**
     * @description: 控制层用户查询方法
     * @author: 王凯旋
     * @date: 2022/7/30 12:57 下午
     * @param: request
     * @param: response
     * @param: name
     **/
    @RequestMapping("/user/query")
    public void findUsers(HttpServletRequest request, HttpServletResponse response,@RequestParam("name")String name){
        response.setContentType("text/html;charset=utf-8");
        try {
            List<User> users = userService.findUser(name);
            response.getWriter().print("<h1>springmvc王凯旋"+name+"</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
    }
}
