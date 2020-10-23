package com.swpu.edu.controller;


import com.swpu.edu.entity.OrderPO;
import com.swpu.edu.entity.ProductPO;
import com.swpu.edu.service.OrderService;
import com.swpu.edu.service.ProductService;
import com.swpu.edu.utils.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 唐小东
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/edu/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    /**
     * 生成订单
     * @param orderPO
     * @return
     */
    @RequestMapping("create/order")
    @Transactional
    public Return createOrder(OrderPO orderPO){
        // 购买商品数量
        int productCount = orderPO.getCount();
        // 商品id
        Integer productId = orderPO.getProductId();
        // 获得商品商品库存信息
        ProductPO productPO = productService.selectProductById(productId);
        // 判断商品库存是否足够
        if(productPO.getTotal() >= productCount){
            return Return.error().setMessage("商品库存不足"); // 库存不足直接返回
        }
        // 更新商品库存
        productPO.setTotal(productPO.getTotal() - productCount);
        // 设置商品id
        productPO.setId(productId);
        // 存入数据库
        productService.updateProductCount(productPO);
        // 生成订单
        int flag = orderService.insertNewOrder(orderPO);
        if (flag > 0) { // 添加成功
            return Return.success();
        }else {
            return Return.error(); // 添加失败
        }
    }
}

