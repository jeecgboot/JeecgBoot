package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.MucConfig;

/**
 * <p>
 * 群组默认配置 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Mapper
public interface MucConfigMapper extends BaseMapper<MucConfig> {

    MucConfig findDefault();

    MucConfig findByMuc(Integer mucId);
}
