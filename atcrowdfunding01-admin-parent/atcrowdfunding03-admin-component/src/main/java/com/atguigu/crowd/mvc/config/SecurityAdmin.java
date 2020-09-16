package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 *考虑到user对象中仅仅包括账号和密码，为了能够获得原始的Admin对象。专门创建这个类，对USER类进行扩展
 */

public class SecurityAdmin extends User {

    // 原始的Admin对象，包含Admin对象的所有属性

    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin,

                         // 传入角色权限信息的集合
                         List<GrantedAuthority> authorities) {
        // 调用父类的构造器
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        // 传入原始的Admin对象

        this.originalAdmin = originalAdmin;

        // 设置原始的Admin 对象中的密码进行擦除 // 使用父类的构造起来进行检查
        this.originalAdmin.setUserPswd(null);
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
