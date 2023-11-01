package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Invitation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.Invitation;
import org.jeecg.modules.im.entity.query_helper.QInvitation;

/**
 * <p>
 * 用户邀请记录 服务类
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
public interface InvitationService extends IService<Invitation> {
    IPage<Invitation> pagination(MyPage<Invitation> page, QInvitation q);

}
