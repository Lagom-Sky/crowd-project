package com.swpu.edu.mapper;

import com.swpu.edu.entity.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ProductPOMapper {

    int insertNewProduct(ProductPO productPO);
    ProductPO selectProductById(Integer id);
    int updateProductCount(ProductPO productPO);
}