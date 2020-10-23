package com.swpu.edu.controller;


import com.swpu.edu.entity.ProductPO;
import com.swpu.edu.service.ProductService;
import com.swpu.edu.utils.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 唐小东
 * @since 2020-10-16
 */
@RestController
@RequestMapping("/edu/product")
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * 添加商品
     * @param productPO 商品信息
     * @return
     */
    @RequestMapping("add/product")
    public Return addProduct(ProductPO productPO){
        int flag = productService.insertNewProduct(productPO);
        if (flag > 0) {
            return Return.success();
        } else {
            return Return.error();
        }
    }

}

