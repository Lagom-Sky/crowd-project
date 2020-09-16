package com.atguigu.crowd.handler;


import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.servcie.MemberService;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberProviderHandler {

    @Autowired
    MemberService memberService;

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO) {

        try {

            memberService.saveMember(memberPO);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {

                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCOUNT_ALREADY_IN_USE);
            }

            return ResultEntity.failed(e.getMessage());
        }

    }

    @RequestMapping("/get/get/member/po/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct){

        try {

            // 调用本地service完成查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);

            // 成功时返回查询到的数据
            return ResultEntity.successWithData(memberPO);

        } catch (Exception e) {
            e.printStackTrace();

            // 抛异常是返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    };

}
