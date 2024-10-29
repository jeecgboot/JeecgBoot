package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: sku product
 * @Author: jeecg-boot
 * @Date:   2024-10-03
 * @Version: V1.0
 */
public interface IProductService extends IService<Product> {

    List<Product> listByCategory(String categoryCode);

    String translateProductNameByValue(String field, String categoryCode, String value);
}
