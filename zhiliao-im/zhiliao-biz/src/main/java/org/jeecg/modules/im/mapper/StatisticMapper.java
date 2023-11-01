package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.Statistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 每日数据统计 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Mapper
public interface StatisticMapper extends BaseMapper<Statistic> {

}
