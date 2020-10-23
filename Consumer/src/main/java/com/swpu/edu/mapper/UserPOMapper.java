package com.swpu.edu.mapper;

import com.swpu.edu.entity.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserPOMapper {
     // 添加用户数据
     int insertUserInfo(UserPO UserPO);
}