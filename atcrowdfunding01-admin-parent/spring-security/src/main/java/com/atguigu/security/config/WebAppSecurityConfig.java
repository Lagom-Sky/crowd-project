package com.atguigu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

@EnableWebSecurity
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

//        builder
//                .inMemoryAuthentication()
//                .withUser("tom")
//                .password("12345")
//                .roles("ADMIN","学徒")
//                .and()
//                .withUser("jerry")
//                .password("12345")
//                .authorities("UPDATE","内门弟子");  // 指定当前用户的权限
        builder
                .userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        // 表示给请求授权
        security
                // 对请求进行授权
                .authorizeRequests()
                // 针对该路径进行授权
                .antMatchers("/index.jsp")
                .permitAll()

                .antMatchers("/layui/**")
                // 可以无条件访问
                .permitAll()
                .antMatchers("/level1/**")  ///level1/**这个路径要求的角色为    学徒
                .hasRole("学徒")
                .antMatchers("/level2/**")  ///level2/**这个路径要求的权限为    内门弟子
                .hasAuthority("内门弟子")
                .and()
                // 对请求进行授权
                .authorizeRequests()
                // 表示任意地请求
                .anyRequest()
                // 需要登陆以后才能进行访问
                .authenticated()
                .and()
                .formLogin()
                // 只定未登录去到的页面
                // 关于该函数的特殊说明

                // eg :
                //    index.jsp get
                //    index.jsp post
                //    index.jsp?error
                //    index.jsp?logout
                //
                // 指定登录页会影响到提交登录的地址，退出登录的地址 登录失败的地址
                .loginPage("/index.jsp") // 使用表单形式登录
                .permitAll()
                // 制定了该方法就会覆盖loginPage方法中指定的默认值

                .loginProcessingUrl("/do/login.html")//指定一个提交登录表单的地址
                .permitAll()  // 这里可以不要这句代码spring已经考虑这个问题了
                // 个性化设置修改登陆账号密码的请求参数名
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd")

                // 登陆成功需要去到的页面
                .defaultSuccessUrl("/main.html")
                .and()
                .csrf()
                .disable() // 关闭csrf
                .logout() // 开启退出的功能
                .logoutUrl("/do/logout.html")
                .logoutSuccessUrl("/index.jsp")
                .and()
                .exceptionHandling() // 指定异常的处理器
//                .accessDeniedPage("/to/no/auth/page.html")  // 访问别拒绝后来到这个页面
                // 手动实现访问被拒绝后的跳转页面
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletRequest.setAttribute("message","抱歉你无法访问这个资源********");
                        httpServletRequest.getRequestDispatcher("/WEB-INF/views/no_auth.jsp").forward(httpServletRequest,httpServletResponse);
                    }
                })
                .and()
                .rememberMe() // 开启记忆功能
                .tokenRepository(repository)
        ;

    }
}
