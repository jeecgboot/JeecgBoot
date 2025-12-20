package org.jeecg.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.monitor.entity.BizMonitorMetric;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 业务指标快照
 * @Author: JeecgBoot
 * @Date: 2025-07-29
 * @Version: V1.0
 */
@Mapper
public interface BizMonitorMetricMapper extends BaseMapper<BizMonitorMetric> {
}
