package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.crowd.entity.StudentInfo;
import com.atguigu.crowd.service.api.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {
    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @Autowired
    private AdminService adminService;

    @RequestMapping("/send/array.html")
    public String testReceiveArrayOne(@RequestParam("array")List<Integer> array){

        for(Integer number:array)
            System.out.println(number);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest httpServletRequest) {
        boolean judgeResult = CrowdUtil.judgeRequestType(httpServletRequest);

        logger.info("judgeResult" + judgeResult);
//        Admin admin = new Admin(null,"fsdafds","fsdafds","fsdafds","fsdafds",null);
//        adminService.saveAdmin(admin);
        List<Admin> list = adminService.getAll();
        for (Admin admin : list)
            System.out.println(admin);
        System.out.println(10 / 0);
        String a = null;
        System.out.println(a.length());
        modelMap.addAttribute("adminList", list);
        return "success";
    }
    @ResponseBody
    @RequestMapping("/send/student.html")
    public String testStudent(@RequestBody StudentInfo studentInfo,HttpServletRequest httpServletRequest){
        System.out.println(studentInfo);
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/ResultEntity.json")
    public ResultEntity<StudentInfo> testResultEntity(@RequestBody StudentInfo studentInfo,HttpServletRequest httpServletRequest) {
        boolean judgeResult = CrowdUtil.judgeRequestType(httpServletRequest);

        logger.info("judgeResult" + judgeResult);

        logger.info(studentInfo.toString());
        String a = null;
        System.out.println(a.length());
        //将查询到Student对象封装到ResultEntity里面返回
        ResultEntity<StudentInfo> resultEntity = ResultEntity.successWithData(studentInfo);

        return resultEntity;
    }
}
