package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.LogisticChannelChoice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: logistic channel choice
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
@Repository
public interface LogisticChannelChoiceMapper extends BaseMapper<LogisticChannelChoice> {
    List<LogisticChannelChoice> fetchByShopId(@Param("shopIds") List<String> shopIds);
}
