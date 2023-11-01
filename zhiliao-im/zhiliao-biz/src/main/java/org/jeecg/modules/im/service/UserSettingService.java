package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.UserSetting;

/**
 * <p>
 * 用户设置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-04-13
 */
public interface UserSettingService extends IService<UserSetting> {
    UserSetting findByUserId(Integer userId);

    Result<Object> updateSetting(UserSetting setting);
}
