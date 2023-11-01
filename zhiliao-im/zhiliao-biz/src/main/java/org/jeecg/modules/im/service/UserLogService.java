package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.entity.query_helper.QUserLog;

/**
 * <p>
 * 用户日志 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface UserLogService extends IService<UserLog> {
    IPage<UserLog> pagination(MyPage<UserLog> page, QUserLog q);

}
