package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;

@Service
public class AdminServiceImpl implements AdminService {

    Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void updata(Admin admin) {
        // "Selective表示可选择的更新，对于null的字段不更行

        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException("用户名重复了");
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId,List<Integer> roleIdList) {

        // 根据adMinId删除旧的关联关系
        adminMapper.deleteOldRelationship(adminId);

        // 2.根据roleIdList哈adminId去保存关系

        if (roleIdList != null && roleIdList.size() != 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }


    }

    @Override
    public Admin getAdminById(Integer adminId) {

        return adminMapper.selectByPrimaryKey(adminId);
    }
    @Override
    public void saveAdmin(Admin admin) {
        // 1.密码加密
        String userPswd = bCryptPasswordEncoder.encode(admin.getUserPswd());

        admin.setUserPswd(userPswd);

        // 2.创建保存的时间

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String createTime = format.format(date);

        admin.setCreateTime(createTime);

        // 3.执行保存

        try {

            adminMapper.insert(admin);

        } catch (Exception e) {

            e.printStackTrace();
            logger.info("异常全类名" + e.getClass().getName());
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE);
            }

        }
    }

    @Override
    public List<Admin> getAll() {
      return   adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1.根据登陆的账号查询Admin对象
        AdminExample adminExample = new AdminExample();

            // 创建Criteria对象
        AdminExample.Criteria criteria = adminExample.createCriteria();

            // 封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);

            // 查询
        List<Admin>  list = adminMapper.selectByExample(adminExample);

            //判断Admin对象是不是为null
        if(list == null || list.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if(list.size() > 1)
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);

        Admin admin = list.get(0);

        if(admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 2.判断Admin对象是否为null

        // 3.如果Admin对象为空则抛出异常

        // 4.不为空则取出对象的密码
        String userPswdFromDB = admin.getUserPswd();

        // 5.将表单提交的明文密码进行加密
        String userPswdFromMd5 = CrowdUtil.md5(userPswd);

        // 6.对密码进行比较
        if(!Objects.equals(userPswdFromDB,userPswdFromMd5)){
            // 7.不一致抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        // 8.一致就返回admin对象
        return admin;
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct) {

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria  criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        return adminMapper.selectByExample(adminExample).get(0);
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 调用PagHelper的静态方法开启分页的功能
        // 这里充分体现了PageHelper的“非入侵式”设计，原本要做的查询不需要任何的修改
        PageHelper.startPage(pageNum,pageSize);

        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyWord(keyword);

        // 3.封装到PageInfo中
        return new PageInfo<>(list);
    }

    @Override
    public void remove(Integer adminId) {

        adminMapper.deleteByPrimaryKey(adminId);
    }
}

