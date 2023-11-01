package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.ChannelBlock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 频道封禁人员 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Mapper
public interface ChannelBlockMapper extends BaseMapper<ChannelBlock> {

}
