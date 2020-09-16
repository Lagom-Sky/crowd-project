package com.atguigu.crowd.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {


        // 浏览器访问的地址
        String urlPath = "/auth/member/to/reg/page.html";

        // 目标视图的名称,将来拼接前后缀
        String viewName = "member-reg";


        // 添加一个viewController
        registry.addViewController(urlPath).setViewName(viewName);

        registry.addViewController("/auth/member/to/login/page.html").setViewName("member-login");

        registry.addViewController("/auth/member/to/center/page.html").setViewName("member-center");

    }
}
