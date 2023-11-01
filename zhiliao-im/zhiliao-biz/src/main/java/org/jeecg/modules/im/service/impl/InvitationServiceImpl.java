package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Invitation;
import org.jeecg.modules.im.entity.Invitation;
import org.jeecg.modules.im.entity.query_helper.QInvitation;
import org.jeecg.modules.im.mapper.InvitationMapper;
import org.jeecg.modules.im.mapper.InvitationMapper;
import org.jeecg.modules.im.service.InvitationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户邀请记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
@Service
public class InvitationServiceImpl extends BaseServiceImpl<InvitationMapper, Invitation> implements InvitationService {
    @Autowired
    private InvitationMapper invitationMapper;
    @Override
    public IPage<Invitation> pagination(MyPage<Invitation> page, QInvitation q) {
        return invitationMapper.pagination(page,q);
    }
}
