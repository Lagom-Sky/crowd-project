package com.swpu.edu.service.impl;

import com.swpu.edu.entity.UserPO;
import com.swpu.edu.mapper.UserPOMapper;
import com.swpu.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserPOMapper userPOMapper;

    @Override
    public int insertUserInfo(UserPO userPO) {
         int flag = userPOMapper.insertUserInfo(userPO);
         // 设置除零异常测试事务
         // int i = 2 / 0;
         return flag;
    }

}
