package com.atguigu.crowd.imp;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.servcie.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// 在类上使用Transactional这个注解就 针对查询事务配置设置事务属性
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {


    @Autowired
    private MemberPOMapper memberPOMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {

        memberPOMapper.insertSelective(memberPO);
    }

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        MemberPOExample memberPOExample = new MemberPOExample();

        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();

        criteria.andLoginacctEqualTo(loginacct);

        List<MemberPO> list = memberPOMapper.selectByExample(memberPOExample);

        if (list.isEmpty()) {
            return null;
        }
        else
            return list.get(0);
    }

}
