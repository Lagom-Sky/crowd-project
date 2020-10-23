package com.swpu.edu.controller;


import com.swpu.edu.entity.UserPO;
import com.swpu.edu.service.UserService;
import com.swpu.edu.utils.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 唐小东
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/edu/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 添加用户
     * @param userPO
     * @return
     */
    @PostMapping("save/User/Info")
    public Return saveUserInfo(UserPO userPO) {
        int flag = userService.insertUserInfo(userPO);
        if (flag > 0) {
            return Return.success();
        }
        else {
            return Return.error();
        }
    }
}

