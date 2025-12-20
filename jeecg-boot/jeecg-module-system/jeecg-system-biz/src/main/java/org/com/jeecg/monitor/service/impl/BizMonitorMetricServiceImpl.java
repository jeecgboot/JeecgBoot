package org.jeecg.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.monitor.entity.BizMonitorMetric;
import org.jeecg.monitor.mapper.BizMonitorMetricMapper;
import org.jeecg.monitor.service.IBizMonitorMetricService;
import org.springframework.stereotype.Service;

/**
 * @Description: 业务指标快照
 * @Author: JeecgBoot
 * @Date: 2025-07-29
 * @Version: V1.0
 */
@Service
public class BizMonitorMetricServiceImpl extends ServiceImpl<BizMonitorMetricMapper, BizMonitorMetric> implements IBizMonitorMetricService {
}
