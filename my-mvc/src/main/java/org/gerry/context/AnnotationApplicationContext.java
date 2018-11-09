package org.gerry.context;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/7 11:12
 * @Description: 注解配置扫描类
 */
public class AnnotationApplicationContext extends AbstractApplicationContext {

    public AnnotationApplicationContext() {

    }

    public AnnotationApplicationContext(String configLocation) {
        this(null, true,configLocation);
    }

    public AnnotationApplicationContext(ApplicationContext context, boolean refresh, String configLocation) {
        super(context,resolvePath(configLocation));

        if (refresh) {
            // 调用父类的方法
            onRefresh();
        }
    }

    // 路径处理
    private static String resolvePath(String configLocation) {
        String path = configLocation;

        if (configLocation.startsWith("classpath:")) {
            path = configLocation.substring("classpath:".length());
        }

        System.out.println(path);

        return path;
    }
}
