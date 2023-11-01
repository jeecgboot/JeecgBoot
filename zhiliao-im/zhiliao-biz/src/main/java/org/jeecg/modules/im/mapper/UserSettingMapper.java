package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.UserSetting;

/**
 * <p>
 * 用户设置 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-04-13
 */
@Mapper
public interface UserSettingMapper extends BaseMapper<UserSetting> {

    UserSetting findByUserId(Integer userId);
}
