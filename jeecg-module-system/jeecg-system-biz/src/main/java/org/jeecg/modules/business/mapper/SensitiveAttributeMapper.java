package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SensitiveAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: sensitive_attribute
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
@Repository
public interface SensitiveAttributeMapper extends BaseMapper<SensitiveAttribute> {
    SensitiveAttribute getHighestPriorityAttribute(@Param("orderId") String orderId);
    String getHighestPriorityAttributeId(@Param("orderId") String orderId);

    List<SensitiveAttribute> listIdAndPriority();

    String getNameByAttributes(@Param("attribute") SensitiveAttribute sensitiveAttribute);

    SensitiveAttribute getByZhName(@Param("zhName") String zhName);
}
