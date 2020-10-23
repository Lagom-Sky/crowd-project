package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<String> getAssignedAuthIdByAdminId(Integer AdminId) {

        return authMapper.selectAssignedAuthIdByAdminId(AdminId);

    }

    @Override
    public List<Auth> getAll() {

        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.getAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {

        // 获取RoleId
        List<Integer> roleIdList = map.get("roleId");

        Integer roleId = roleIdList.get(0);

        // 删除旧的关联关系数据
        authMapper.deleteOldRelationship(roleId);
        // 获取authIdList
        List<Integer> authIdList = map.get("authIdArray");

        // 判断数据是否有效
        if(authIdList == null || authIdList.size() == 0)
            return;


        // 数据有效
        authMapper.insertNewRelationship(roleId, authIdList);
    }

}
