package org.jeecg.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.monitor.entity.BizMonitorMetric;
import org.jeecg.monitor.mapper.BizMonitorMetricMapper;
import org.jeecg.monitor.service.IBizMonitorMetricService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务指标快照 服务实现类
 * </p>
 *
 * @author jeecg-boot
 * @since 2025-06-20
 */
@Service
public class BizMonitorMetricServiceImpl extends ServiceImpl<BizMonitorMetricMapper, BizMonitorMetric> implements IBizMonitorMetricService {

}