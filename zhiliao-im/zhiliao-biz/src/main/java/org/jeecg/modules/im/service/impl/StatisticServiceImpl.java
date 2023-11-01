package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.im.entity.Statistic;
import org.jeecg.modules.im.mapper.StatisticMapper;
import org.jeecg.modules.im.service.StatisticService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 每日数据统计 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Service
public class StatisticServiceImpl extends BaseServiceImpl<StatisticMapper, Statistic> implements StatisticService {

}
