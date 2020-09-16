package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.atguigu.crowd.entity.Admin;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class test {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testConnection() throws  Exception{

        Connection connection =  dataSource.getConnection();
        System.out.println(connection);

    }
    @Test
    public void testInsertAdmin() throws  Exception{
        for(int i = 0;i<256;i++) {
            Admin admin = new Admin(null,"tom"+"_"+i,"123123"+"_"+i,"tom"+"_"+i,"tom@qq.com"+"_"+i,null);
            adminMapper.insert(admin);
        }
        /**
         * 实际开发中不适用system.out来打印，他本是是一种io操作，非常影响性能
         * 上线前去删除system out也可能会有遗漏，而且非常的麻烦
         * 而如果使用日志系统，那么通过日志的级别就可以批量的打印控制信息的打印
         */
       // System.out.println(count);
    }
    @Test
    public  void tesLog(){
        //1.获取Logger对象,传入的Class对象就是当前打印日志的这个类
        Logger logger = LoggerFactory.getLogger(test.class);
        //根据不同的日志级别来进行打印
        logger.debug("hello i am Debug level !");
        logger.debug("hello i am Debug level !");
        logger.debug("hello i am Debug level !");
        logger.debug("hello i am Debug level !");

        logger.info("hello i am info level");
        logger.info("hello i am info level");
        logger.info("hello i am info level");
        logger.info("hello i am info level");

        logger.warn("warning level");
        logger.warn("warning level");
        logger.warn("warning level");
        logger.warn("warning level");

        logger.error("error level");
        logger.error("error level");
        logger.error("error level");
        logger.error("error level");
        logger.error("error level");
    }
    @Test
    public  void testAdminService(){
        Admin admin = new Admin(null,"jerry","12345","hello","hell@qq.com",null);
        adminService.saveAdmin(admin);
    }

    @Test
    public  void testSaveRole(){
        for(int i = 0;i<66;i++)
            roleMapper.insert(new Role(null,"role"+i));
    }

    @Test
    public  void test(){
        List<Role> list = roleMapper.selectByExample(new RoleExample());
    }
}
