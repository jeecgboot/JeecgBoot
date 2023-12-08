package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 商品
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {
    void updateWeightBatch(@Param("products") List<Product> product);

    String searchFirstEmptyWeightProduct(@Param("skuIds") List<String> skuIds);
}
