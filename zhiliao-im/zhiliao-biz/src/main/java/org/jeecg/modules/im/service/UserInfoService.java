package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author junko
 * @since 2021-09-21
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo findBasicByUserId(Integer userId);
    UserInfo findByUserId(Integer userId);
}
