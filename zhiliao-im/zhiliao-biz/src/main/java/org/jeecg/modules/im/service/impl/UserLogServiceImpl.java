package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.entity.query_helper.QUserLog;
import org.jeecg.modules.im.mapper.UserLogMapper;
import org.jeecg.modules.im.service.UserLogService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户日志 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class UserLogServiceImpl extends BaseServiceImpl<UserLogMapper, UserLog> implements UserLogService {
    @Autowired
    private UserLogMapper userLogMapper;
    @Override
    public IPage<UserLog> pagination(MyPage<UserLog> page, QUserLog q) {
        return userLogMapper.pagination(page,q);
    }
}
