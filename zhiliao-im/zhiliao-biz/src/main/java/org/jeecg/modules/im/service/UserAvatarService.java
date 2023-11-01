package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserAvatar;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.entity.query_helper.QUserAvatar;

/**
 * <p>
 * 用户历史头像 服务类
 * </p>
 *
 * @author junko
 * @since 2022-01-12
 */
public interface UserAvatarService extends IService<UserAvatar> {
    IPage<UserAvatar> pagination(MyPage<UserAvatar> page, QUserAvatar q);

    Result<Object> findMyAll(Integer userId);
    Result<Object> del(String id);
}
