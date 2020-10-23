package com.swpu.edu.service.impl;

import com.swpu.edu.entity.ProductPO;
import com.swpu.edu.mapper.ProductPOMapper;
import com.swpu.edu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductPOMapper productPOMapper;

    @Override
    public int insertNewProduct(ProductPO productPO) {
        return productPOMapper.insertNewProduct(productPO);
    }

    @Override
    public ProductPO selectProductById(Integer id) {
        return productPOMapper.selectProductById(id);
    }

    @Override
    public int updateProductCount(ProductPO productPO) {
        return productPOMapper.updateProductCount(productPO);
    }

}
