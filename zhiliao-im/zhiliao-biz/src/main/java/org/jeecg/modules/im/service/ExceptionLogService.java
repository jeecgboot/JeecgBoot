package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ExceptionLog;
import org.jeecg.modules.im.entity.query_helper.QExceptionLog;

/**
 * <p>
 * 系统异常日志 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface ExceptionLogService extends IService<ExceptionLog> {
    IPage<ExceptionLog> pagination(MyPage<ExceptionLog> page, QExceptionLog q);
}
