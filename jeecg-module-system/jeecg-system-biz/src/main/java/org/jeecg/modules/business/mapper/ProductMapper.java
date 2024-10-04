package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: sku product
 * @Author: jeecg-boot
 * @Date:   2024-10-03
 * @Version: V1.0
 */
@Repository
public interface ProductMapper extends BaseMapper<Product> {

    List<Product> listByCategory(@Param("categoryCode")String categoryCode);

    String translateProductNameByValue(@Param("field") String field, @Param("categoryCode") String categoryCode, @Param("value") String value);
}
