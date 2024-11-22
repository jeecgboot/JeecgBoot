package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ExtraFeeOption;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: extra fees options
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Repository
public interface ExtraFeeOptionMapper extends BaseMapper<ExtraFeeOption> {

    ExtraFeeOption selectByEnName(@Param("name") String optionName);
}
