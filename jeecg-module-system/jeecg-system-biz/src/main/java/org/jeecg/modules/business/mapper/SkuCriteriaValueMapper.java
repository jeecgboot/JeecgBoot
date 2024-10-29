package org.jeecg.modules.business.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.business.entity.SkuCriteriaValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: SKU criteria value
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Repository
public interface SkuCriteriaValueMapper extends BaseMapper<SkuCriteriaValue> {

    List<SkuCriteriaValue> listByCriteria(@Param("criteriaId") String criteriaId);

    String translateValueByCriteria(@Param("criteria") String criteria, @Param("field") String field, @Param("value") String value);
}
