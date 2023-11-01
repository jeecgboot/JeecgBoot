package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.entity.query_helper.QUserAvatar;
import org.jeecg.modules.im.mapper.UserAvatarMapper;
import org.jeecg.modules.im.service.UserAvatarService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户历史头像 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-01-12
 */
@Service
public class UserAvatarServiceImpl extends BaseServiceImpl<UserAvatarMapper, UserAvatar> implements UserAvatarService {
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private UserAvatarMapper userAvatarMapper;
    @Override
    public IPage<UserAvatar> pagination(MyPage<UserAvatar> page, QUserAvatar q) {
        return userAvatarMapper.pagination(page,q);
    }
    @Override
    public Result<Object> findMyAll(Integer userId) {
        String cacheKey = String.format(ConstantCache.HISTORY_AVATAR, userId);
        List<UserAvatar> avatars = (List<UserAvatar>) redisUtil.get(cacheKey);
        if(avatars==null){
            avatars = userAvatarMapper.findAll(userId);
            redisUtil.set(cacheKey,avatars);
        }
        return success(avatars);
    }

    @Override
    public Result<Object> del(String id) {
        removeById(id);
        redisUtil.removeAll(ConstantCache.HISTORY_AVATAR.replace("%s_%s",""));
        return success();
    }
}
