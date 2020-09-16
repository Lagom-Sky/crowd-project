package com.atguigu.crowd.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;


// 表示当前类是一个配置类
@Configuration

// 启用Web环境下权限控制的功能
// 启用全局方法授权控制 ,保证权限控制的注解能够生效@PreAuthority 等
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    // 加密密码的工具列Bean

    /**
     * 在这里装配的Bean在父容器中不能用
     * @return
     */
//    @Bean
//    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
//
//        return new BCryptPasswordEncoder();
//}
//
//
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//
//        builder
//                .inMemoryAuthentication()
//                .withUser("tom")
//                .password("12345")
//                .roles("ADMIN","学徒")
//                .and()
//                .withUser("jerry")
//                .password("12345")
//                .authorities("UPDATE","内门弟子");  // 指定当前用户的权限

        // 基于数据的认证
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        // 表示给请求授权
        security
                // 对请求进行授权
                .authorizeRequests()
                // 针对该路径进行授权
                .antMatchers("/index.jsp")
                .permitAll()
                .antMatchers("/admin/to/login/page.html")
                .permitAll()
                .antMatchers("/ztree/**",
                        "/script/**",
                        "/layer/**",
                        "/jquery/**",
                        "/img/**",
                        "/fonts/**",
                        "/css/**",
                        "/crowd/**",
                        "/bootstrap/**")
                .permitAll()
                .antMatchers("/admin/get/page.html") // 针对分页的请求地址要求具备经理角色
                .access("hasRole('经理') OR hasAuthority('user:get')")
                // 表示任意地请求
                .anyRequest()
                // 需要登陆以后才能进行访问
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("exception", new Exception("你没有权限访问该页面"));
                        httpServletRequest.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .formLogin()
                .loginPage("/admin/to/login/page.html")
                .loginProcessingUrl("/security/do/login.html")
                .defaultSuccessUrl("/admin/to/main/page.html")
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd")
                .and()
                .csrf()
                .disable()// 关闭csrf;// 使用表单形式登录
                .logout()// 开启退出登录功能
                .logoutUrl("/security/do/logout.html") // 指定退出登陆的地址
                .logoutSuccessUrl("/admin/to/login/page.html"); // 指定退出成功后去打的页面


    }
}
