package com.user.dao;

import org.gerry.context.annotation.MyDao;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/8 10:19
 * @Description:
 */
@MyDao
public class UserDaoImpl implements UserDao {

    @Override
    public void test() {
        System.out.println("获取到用户信息数据");
    }
}
