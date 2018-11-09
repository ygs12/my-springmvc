package org.gerry.context;

import com.user.controller.UserController;

import static com.util.ReflectUtil.getMethodVariableName;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/8 10:17
 * @Description:
 */
public class TestApplicationContext {
    public static void main(String[] args) {
        // 获取配置文件的包路径-测试
        AnnotationApplicationContext
                context = new AnnotationApplicationContext("classpath:my-mvc.xml");
        /*UserDao userDaoImpl = (UserDao) context.getBean("userDaoImpl");
        userDaoImpl.test();
        UserService userService = (UserService) context.getBean("userServiceImpl");
        userService.query();*/

        UserController controller = (UserController) context.getBean("userController");

        String[] names=getMethodVariableName("com.user.controller.UserController","test");
        for(String name:names)
        {
            System.out.println(name);
        }
    }
}
