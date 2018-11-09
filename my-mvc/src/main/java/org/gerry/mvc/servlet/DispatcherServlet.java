package org.gerry.mvc.servlet;

import org.gerry.context.AnnotationApplicationContext;
import org.gerry.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: gerry-mvc
 * @Auther: GERRY
 * @Date: 2018/11/7 11:04
 * @Description:
 */
public class DispatcherServlet extends HttpServlet {
    private ApplicationContext context;
    public static final String CONFIG_LOCATION = "contextConfigLocation";

    public DispatcherServlet() {
        System.out.println("DispatcherServlet");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 获取servlet配置参数
        String configLocation = config.getInitParameter(CONFIG_LOCATION);
        // 构建一个MyIOC容器
        this.context = new AnnotationApplicationContext(configLocation);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
