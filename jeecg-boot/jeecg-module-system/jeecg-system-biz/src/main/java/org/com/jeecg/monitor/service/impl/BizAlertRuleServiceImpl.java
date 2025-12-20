package org.jeecg.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.monitor.entity.BizAlertRule;
import org.jeecg.monitor.mapper.BizAlertRuleMapper;
import org.jeecg.monitor.service.IBizAlertRuleService;
import org.springframework.stereotype.Service;

/**
 * @Description: 预警规则
 * @Author: JeecgBoot
 * @Date: 2025-07-29
 * @Version: V1.0
 */
@Service
public class BizAlertRuleServiceImpl extends ServiceImpl<BizAlertRuleMapper, BizAlertRule> implements IBizAlertRuleService {
}
