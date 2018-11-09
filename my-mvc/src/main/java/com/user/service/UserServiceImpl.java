package com.user.service;

import com.user.dao.UserDao;
import org.gerry.context.annotation.MyInjection;
import org.gerry.context.annotation.MyService;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/8 10:19
 * @Description:
 */
@MyService
public class UserServiceImpl implements UserService {

    @MyInjection
    private UserDao userDao;

    @Override
    public void query() {
        userDao.test();
    }
}
