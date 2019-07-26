package org.jeecg.modules.bjcl.product.service.impl;


import org.jeecg.modules.bjcl.product.entity.Product;
import org.jeecg.modules.bjcl.product.mapper.ProductMapper;
import org.jeecg.modules.bjcl.product.service.IProductService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 产品
 * @Author: jeecg-boot
 * @Date:   2019-07-17
 * @Version: V1.0
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
