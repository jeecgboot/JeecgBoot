package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserNickname;
import org.jeecg.modules.im.entity.UserNickname;
import org.jeecg.modules.im.entity.query_helper.QUserNickname;
import org.jeecg.modules.im.mapper.UserNicknameMapper;
import org.jeecg.modules.im.mapper.UserNicknameMapper;
import org.jeecg.modules.im.service.UserNicknameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 昵称记录，用于审核 服务实现类
 * </p>
 *
 * @author junko
 * @since 2022-11-22
 */
@Service
public class UserNicknameServiceImpl extends BaseServiceImpl<UserNicknameMapper, UserNickname> implements UserNicknameService {
    @Autowired
    private UserNicknameMapper userNicknameMapper;
    @Override
    public IPage<UserNickname> pagination(MyPage<UserNickname> page, QUserNickname q) {
        return userNicknameMapper.pagination(page,q);
    }
}
