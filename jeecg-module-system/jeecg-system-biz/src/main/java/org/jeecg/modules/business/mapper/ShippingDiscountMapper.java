package org.jeecg.modules.business.mapper;

import java.util.List;
import org.jeecg.modules.business.entity.ShippingDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: SKU物流折扣
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Repository
public interface ShippingDiscountMapper extends BaseMapper<ShippingDiscount> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<ShippingDiscount> selectByMainId(@Param("mainId") String mainId);
}
