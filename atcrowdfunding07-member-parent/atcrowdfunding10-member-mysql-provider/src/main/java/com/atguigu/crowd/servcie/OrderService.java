package com.atguigu.crowd.servcie;

import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAdderssVO(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrderVO(OrderVO orderVO);
}
