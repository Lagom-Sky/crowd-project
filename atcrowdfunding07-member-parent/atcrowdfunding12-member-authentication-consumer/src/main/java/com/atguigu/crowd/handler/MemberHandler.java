package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.apache.commons.codec.language.bm.Rule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {
    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService ;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout( HttpSession session) {

        session.invalidate();

        return "redirect:http://localhost:81";
    }

    @RequestMapping("/auth/member/to/login")
    public String memberLogin(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session
    ) {
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, "服务器阻塞，请稍候重试");

            return "member-login";
        }
        MemberPO data = resultEntity.getData();

        if (data == null) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, "未查询到用户信息，请检查账号是否输入正确");

            return "member-login";
        }
        // 7.执行密码的加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String sqlUserPassword = data.getUserpswd();

        if (!bCryptPasswordEncoder.matches(userpswd, sqlUserPassword)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, "抱歉密码不正确，请检查后重试");

            return "member-login";
        }
        // 创建memberLoginVO对象去存入session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(data.getId(), data.getUsername(), data.getEmail());

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        return "redirect:http://localhost:81/auth/member/to/center/page.html";
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap){
        // 1.获取用户的输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼接Redis中存储的验证使用的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.Redis读取Key中对应的Value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);

        // 4.查询操作是否有效
        String result = resultEntity.getResult();

        if (ResultEntity.FAILED.equals(result)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null){

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXIT);

            return "member-reg";
        }
        // 5.比较验证码
        String fromCode = memberVO.getCode();

        if (!Objects.equals(fromCode, redisCode)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.CODE_INVALID);

            return "member-reg";
        }
        // 6.验证码一致清除Redis存储的验证码
        ResultEntity<String> removeResultEntity = redisRemoteService.removeRedisKeyRemote(key);

        // 可能Redis中数据删除是不成功的
//        if (ResultEntity.FAILED.equals(removeResultEntity.getResult())) {
//
//
//            return "";
//        }
        // 7.执行密码的加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String userpswd = memberVO.getUserpswd();

        String encode = bCryptPasswordEncoder.encode(userpswd);

        memberVO.setUserpswd(encode);

        // 8.执行保存
        // ①创建空的MemberPO
        MemberPO memberPO = new MemberPO();

        // ②复制属性
        BeanUtils.copyProperties(memberVO, memberPO);

        // ③调用远程的方法执行保存
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.SAVE_MEMBER_FAILED);

            return "member-reg";
        }

        return "redirect:/auth/member/to/login/page.html";
    }

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {


        // 1.发送验证码
        ResultEntity<String> resultEntity = CrowdUtil.sendCodeByShortMessage(
                phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getHost(),
                shortMessageProperties.getMethod(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin()
        );
        // 2.验证短信发送的结果
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            // 3.将生成的验证码存入Redis

            String code = resultEntity.getData();

            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

            ResultEntity<String> saveCode = redisRemoteService.setRedisKeyValueRemoteWithTimeOut(key, code, 15, TimeUnit.MINUTES);

            if (ResultEntity.SUCCESS.equals(saveCode.getResult())) {

                return ResultEntity.successWithoutData();

            }else {
                return saveCode;
            }
        }
        else {
            return resultEntity;
        }
    }
}
