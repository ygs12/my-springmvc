package org.gerry.context;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/7 11:09
 * @Description:
 */
public interface ApplicationContext {
    Object getBean(String beanName);
}
