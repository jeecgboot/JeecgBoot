package org.jeecg.modules.business.mapper;

import java.util.List;
import org.jeecg.modules.business.entity.SkuPrice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: SKU价格表
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Repository
public interface SkuPriceMapper extends BaseMapper<SkuPrice> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<SkuPrice> selectByMainId(@Param("mainId") String mainId);
}
