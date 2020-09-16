package com.atguigu.crowd.handler;


import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.entity.po.OrderProject;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    private Logger logger = LoggerFactory.getLogger(OrderProject.class);

    @RequestMapping("/save/address")
    public String saveAddress (
            AddressVO addressVO,
            HttpSession session
    ) {

        // 执行地址信息的保存
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);

        logger.debug("地址保存处理结果"+resultEntity.getResult());

        // 获得ReturnId
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_VO);

        Integer returnCount = orderProjectVO.getReturnCount();

        // 重定向当这个页面可以重新加载，用户的地址信息
        return "redirect:http://localhost:81/order/confirm/order/"+returnCount;
    }

    @RequestMapping("/confirm/order/{returnCountInput}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCountInput") Integer returnCount,
            HttpSession session
            ) {

        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_VO);

        orderProjectVO.setReturnCount(returnCount);

        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_VO, orderProjectVO);

        // 取出已经登录用户的id

        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        Integer memberId = memberLoginVO.getId();

        // 查询现有的收货地址
        ResultEntity<List<AddressVO>> address = mySQLRemoteService.getAddressVORemote(memberId);


        if (ResultEntity.SUCCESS.equals(address.getResult())) {

            List<AddressVO> data = address.getData();

            session.setAttribute("addressVOList", data);

            return "confirm_order";
        }else {
            return "confirm_return";
        }
    }

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirm(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session
    ){
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);

        System.out.println(resultEntity.getData());
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {

            OrderProjectVO data = resultEntity.getData();

            System.out.println(data);

            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT_VO, data);

        }

        return "confirm_return";
    }

}
