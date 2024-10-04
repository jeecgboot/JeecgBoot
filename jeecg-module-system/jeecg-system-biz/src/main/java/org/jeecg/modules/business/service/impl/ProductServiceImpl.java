package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Product;
import org.jeecg.modules.business.mapper.ProductMapper;
import org.jeecg.modules.business.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: sku product
 * @Author: jeecg-boot
 * @Date:   2024-10-03
 * @Version: V1.0
 */
@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> listByCategory(String categoryCode) {
        return productMapper.listByCategory(categoryCode);
    }

    @Override
    public String translateProductNameByValue(String field, String categoryCode, String value) {
        if(!field.equals("enName") && !field.equals("zhName"))
            return value;
        return productMapper.translateProductNameByValue(field, categoryCode, value);
    }
}
