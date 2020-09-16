package com.atguigu.crowd.test;


import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {

    private Logger logger = LoggerFactory.getLogger(MybatisTest.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Test
    public void testLoadDetailProjectVO() {

        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(20);
        logger.info(detailProjectVO.toString());
        logger.info(detailProjectVO.getDetailPicturePathList().toString());
        logger.info(detailProjectVO.getDetailReturnVOList().get(0).toString());
    }

    @Test
    public void testLoadTypeData(){

        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
        logger.info(portalTypeVOList.get(0).toString());
    }

    @Test
    public void testConnection() throws SQLException {

        System.out.println(dataSource.getConnection().toString());

        logger.info(dataSource.getConnection().toString());

    }

    @Test
    public void testT_memberMapper(){

        MemberPO memberPO = new MemberPO();
        memberPO.setRealname("66");
        memberPO.setAccttype(1);
        memberPO.setAuthstatus(1);
        memberPO.setEmail("fdsfds");
        memberPO.setCardnum("fdsfd");
        memberPO.setId(1);
        memberPO.setLoginacct("fdsfsd");
        memberPO.setUserpswd("fsdfds");
        memberPO.setUsertype(1);
        memberPOMapper.insert(memberPO);
    }

}
