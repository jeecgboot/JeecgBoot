package org.jeecg.modules.business.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SkuCriteria;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: SKU erpCode criteria
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Repository
public interface SkuCriteriaMapper extends BaseMapper<SkuCriteria> {

    List<SkuCriteria> listByCategoryCode(@Param("category")String categoryCode);
}
