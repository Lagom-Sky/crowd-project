package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        // 1.根据账号名称查找Admin对象
        Admin admin = adminService.getAdminByLoginAcct(userName);

        // 2.获得adminId
        Integer adminId = admin.getId();

        // 3.根据adminId获得角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 4.根据adminId去查询权限的信息
        List<String> authNameList = authService.getAssignedAuthIdByAdminId(adminId);

        // 5.创建集合的对象去存储GrantedAuthority
        List<GrantedAuthority> authorityList = new ArrayList<>();

        // 6.遍历assignedRoleList去存入角色的信息
        for (Role role : assignedRoleList) {

            String roleName = "ROLE_" + role.getName();

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);

            authorityList.add(simpleGrantedAuthority);
        }

        // 7.遍历authNameList去存入权限的信息
        for (String authName : authNameList) {

            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);

            authorityList.add(simpleGrantedAuthority);
        }

        // 8.将数据封装一下
        SecurityAdmin securityAdmin = new SecurityAdmin(admin,authorityList);

        return securityAdmin;
    }

}
