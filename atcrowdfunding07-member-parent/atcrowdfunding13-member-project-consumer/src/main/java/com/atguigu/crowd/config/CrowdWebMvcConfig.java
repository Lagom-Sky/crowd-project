package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {


        // 浏览器访问的地址
//        String urlPath = "/auth/member/to/reg/page.html";
//
        // 目标视图的名称,将来拼接前后缀
//        String viewName = "member-reg";


        // 添加一个viewController

        registry.addViewController("/member/my/crowd").setViewName("minecrowdfunding");
        registry.addViewController("/member/need/read/declaration").setViewName("start");
        registry.addViewController("/member/to/submit/crowd/data").setViewName("start-step-1");
        registry.addViewController("/member/to/pay/back/page").setViewName("start-step-2");
        registry.addViewController("/create/confirm/page").setViewName("start-step-3");
        registry.addViewController("/to/start-step-4").setViewName("start-step-4");


    }
}
