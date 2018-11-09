package org.gerry.context;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.gerry.context.annotation.MyController;
import org.gerry.context.annotation.MyDao;
import org.gerry.context.annotation.MyInjection;
import org.gerry.context.annotation.MyService;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/7 11:10
 * @Description:
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private ApplicationContext context;
    private String configLocation;
    // 存储需要管理类的Class对象的集合
    List<Class<?>> classList = Collections.synchronizedList(new ArrayList<>());
    // 创建一个用于管理对象的集合(容器)
    private Map<String, Object> beansMap = new ConcurrentHashMap<>(32);

    public AbstractApplicationContext() {
    }

    public AbstractApplicationContext(ApplicationContext context, String configLocation) {
        this.context = context;
        this.configLocation = configLocation;
    }


    public void onRefresh() {
        // 1、解析xml文件,获取扫描的包路径
        String packageString = parseXML(configLocation);
        doScan(packageString);
        doIOC();
        doAutowired();
    }

    /**
     * 执行依赖注入
     */
    public void doAutowired() {
        if (beansMap.size() == 0) {
            return;
        }
        for (Map.Entry<String, Object> entry : beansMap.entrySet()) {
            Object instance = entry.getValue();
            Class<?> cls = instance.getClass();
            if (!cls.isInterface()) {
                // 获取所有类中定义字段
                Field[] fields = cls.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    try {
                        for(Field field : fields) {
                            Object injectionInstance =  null;
                            // 判断字段上面是否标注了@MyInjection注解
                            if (field.isAnnotationPresent(MyInjection.class)) {
                                // 判断按照类型注入还是名称注入
                                String value = field.getAnnotation(MyInjection.class).value();
                                if (!"".equals(value)) {
                                    // 按照名称装配
                                } else {
                                    // 按照类型装配
                                    String  typeName = field.getType().getName();
                                    injectionInstance = beansMap.get(typeName);
                                }
                            }

                            // 把对应依赖对象设置到该字段里面
                            field.setAccessible(true);
                            field.set(instance, injectionInstance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    /**
     * 执行容器的控制器反转操作
     */
    public void doIOC() {
        if (classList.size() == 0) {
            return;
        }

        try {
            for(Class<?> cls : classList) {
                Object instance = cls.newInstance();
                // 获取实例对应别名(默认为当前类的首字母小写)
                String alias = getLowerName(cls.getSimpleName());
                // 存入容器中
                beansMap.put(alias, instance);
                ////////////////// 为安装依赖对象的类型装配做准备 /////////////
                // 判断Class对象是否实现了接口
                Class<?>[] interfaces = cls.getInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    for (Class<?> clsInterface : interfaces) {
                        beansMap.put(clsInterface.getName(), instance);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            classList.clear();
        }

    }

    /**
     * 用于扫描所有标注的类的方法
     *
     * @param packagePath
     */
    private void doScan(String packagePath) {
        URL url = this.getClass().getClassLoader().getResource(packagePath.replaceAll("\\.", "/"));
        // 创建文件目录
        File file = new File(url.getFile());
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File childFile) {
                if (childFile.isDirectory()) {
                    doScan(packagePath + "." + childFile.getName());
                } else {
                    String fileName = childFile.getName();
                    if (fileName.endsWith(".class")) {
                        // 构建当前文件所在路径
                        String classPath = packagePath + "." + fileName.replaceAll("\\.class", "");
                        try {
                            // 根据路径构建Class对象
                            Class<?> cls = this.getClass().getClassLoader().loadClass(classPath);
                            // 判断是否标注@MyDao，@MyService,@MyController注解上
                            if (cls.isAnnotationPresent(MyDao.class) ||
                                    cls.isAnnotationPresent(MyService.class) ||
                                    cls.isAnnotationPresent(MyController.class)) {
                                // 保存到缓存中
                                classList.add(cls);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });

    }

    /**
     * 解决配置文件获取扫描路径的方法
     *
     * @param configLocation
     * @return
     */
    private String parseXML(String configLocation) {
        String packageString = null;
        // 创建XML文件解析对象
        SAXReader reader = new SAXReader();
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(configLocation)) {
            Document document = reader.read(in);
            // 获取到beans元素下面的bean元素对应base-package属性值
            packageString = document.getRootElement()
                    .element("context")
                    .attributeValue("base-package");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packageString;
    }

    /**
     * 把一个类的首字母变为小写
     * @param className
     * @return
     */
    private String getLowerName(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;

        return String.valueOf(chars);
    }

    @Override
    public Object getBean(String beanName) {
        return this.beansMap.get(beanName);
    }
}
