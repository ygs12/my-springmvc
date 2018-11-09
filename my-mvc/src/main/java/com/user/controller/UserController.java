package com.user.controller;

import com.user.service.UserService;
import org.gerry.context.annotation.MyController;
import org.gerry.context.annotation.MyInjection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/7 11:06
 * @Description:
 */
@MyController
public class UserController {

    @MyInjection
    private UserService userService;


    public void test(HttpServletRequest request, HttpServletResponse response, String name, Integer id) {
        System.out.println("controller->test");
    }
}
