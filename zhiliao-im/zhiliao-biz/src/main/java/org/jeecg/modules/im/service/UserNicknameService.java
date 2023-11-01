package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserNickname;
import org.jeecg.modules.im.entity.UserNickname;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QUserNickname;

/**
 * <p>
 * 昵称记录，用于审核 服务类
 * </p>
 *
 * @author junko
 * @since 2022-11-22
 */
public interface UserNicknameService extends IService<UserNickname> {
    IPage<UserNickname> pagination(MyPage<UserNickname> page, QUserNickname q);

}
