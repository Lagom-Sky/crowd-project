package com.swpu.edu.mapper;

import com.swpu.edu.entity.OrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OrderPOMapper {
    // 添加新的订单
    int insertNewOrder(OrderPO orderPO);
}