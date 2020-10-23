package com.swpu.edu.service;

import com.swpu.edu.entity.ProductPO;

public interface ProductService {

    int insertNewProduct(ProductPO productPO);
    ProductPO selectProductById(Integer id);
    int updateProductCount(ProductPO productPO);
}
