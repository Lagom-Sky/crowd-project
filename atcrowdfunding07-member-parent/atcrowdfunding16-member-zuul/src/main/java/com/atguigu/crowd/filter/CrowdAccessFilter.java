package com.atguigu.crowd.filter;

import com.atguigu.crowd.util.AccessPassResources;
import com.atguigu.crowd.util.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public String filterType() {

        // 在目标访问前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {

        return 0;
    }

    @Override        // 线程本地化
    public boolean shouldFilter() {
        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.通过RequestContext获取当前请求的对象  // 线程本地化 (threadLocal)
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取ServletPath的值
        String servletPath = request.getServletPath();

        // 4.判断当前请求是否需要被拦截
        boolean contains = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if (contains == true) {

            // 返回false则run()不执行
            return false;
        }

        // 5.判断请求是否为静态资源
        boolean flag = AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);

        return !flag;
    }

    @Override
    public Object run() throws ZuulException {

        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.通过RequestContext获取当前请求的对象  // 线程本地化 (threadLocal)
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取当前的session1对象
        HttpSession httpSession = request.getSession();

        // 4.尝试从httpSession中获取已经登录的Session对象
        Object adminMember = httpSession.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);


        // 5.判断adminMember是否为空
        if (adminMember == null) {

            // 重定向到登录页面，因为不是一个工程只能通过重定向到登录页面
            HttpServletResponse response = requestContext.getResponse();

            // 将提示消息存入到Session域，因为已经实现了Session共享，所以可以在不同的工程中传递消息
            httpSession.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,"抱歉客户你需要先完成登录，才能访问相应的资源");

            // 重定向到登录页面member-login
            try {
                response.sendRedirect("/auth/member/to/login/page.html");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}
