package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ParcelStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: parce status
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Repository
public interface ParcelStatusMapper extends BaseMapper<ParcelStatus> {
}
