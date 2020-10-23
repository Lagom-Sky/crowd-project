package com.swpu.edu.service.impl;

import com.swpu.edu.entity.OrderPO;
import com.swpu.edu.mapper.OrderPOMapper;
import com.swpu.edu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderPOMapper orderPOMapper;

    @Override
    public int insertNewOrder(OrderPO orderPO) {
        return orderPOMapper.insertNewOrder(orderPO);
    }

}
