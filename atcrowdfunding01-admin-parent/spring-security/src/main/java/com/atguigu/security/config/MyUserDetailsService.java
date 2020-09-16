package com.atguigu.security.config;

import com.atguigu.security.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // 根据表单或者用户名去查询User对象，并装配角色权限等信息
    @Override
    public UserDetails loadUserByUsername(
            // 表单提交的账号或者用户名
            String userName

            ) throws UsernameNotFoundException {

        // 1.从数据库中查询到用户信息

        String sql = "select id , login_acct,user_pswd,user_name,email from t_admin where login_acct = ?";

        Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, userName);
        if (resultMap == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //3.获取用户名、密码数据
        String loginacct = resultMap.get("login_acct").toString();
        String userpswd = resultMap.get("user_pswd").toString();
        //4.创建权限列表
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ADMIN","USER");
        //5.创建用户对象


        authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        authorityList.add(new SimpleGrantedAuthority("UPDATE"));

        // Admin对象和authorityList 封装到一起并返回

        User user = new User(loginacct, userpswd, authorityList);
        return user;


    }
}
