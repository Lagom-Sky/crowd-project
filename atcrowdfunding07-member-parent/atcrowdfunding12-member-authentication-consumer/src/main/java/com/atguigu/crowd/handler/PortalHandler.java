package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(Model model) {

        // 调用mySQLRemoteService，查询首页要显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity =  mySQLRemoteService.getPortalTypeProjectDataRemote();

        // 检查调用成功还是失败
        String result = resultEntity.getResult();

        if (ResultEntity.SUCCESS.equals(result)) {

            // 3.获取查询的结果信息
            List<PortalTypeVO> list = resultEntity.getData();

            // 4.存入模型
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_PAGE_DATA, list);

        }

        //
        // 这里显示主页的时候实际开发中是要去服务加载数据的

        return "portal";
    }
}
