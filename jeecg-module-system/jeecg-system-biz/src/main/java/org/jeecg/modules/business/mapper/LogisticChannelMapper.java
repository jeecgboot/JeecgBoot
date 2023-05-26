package org.jeecg.modules.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.LogisticChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 物流渠道
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Repository
public interface LogisticChannelMapper extends BaseMapper<LogisticChannel> {

    List<LogisticChannel> getAll();

}
