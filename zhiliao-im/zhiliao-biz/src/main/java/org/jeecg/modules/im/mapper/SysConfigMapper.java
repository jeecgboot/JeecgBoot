package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.SysConfig;

/**
 * <p>
 * 系统配置 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    SysConfig get();
}
