package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ExceptionLog;
import org.jeecg.modules.im.entity.query_helper.QExceptionLog;
import org.jeecg.modules.im.mapper.ExceptionLogMapper;
import org.jeecg.modules.im.service.ExceptionLogService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统异常日志 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class ExceptionLogServiceImpl extends BaseServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {
    @Autowired
    private ExceptionLogMapper exceptionLogMapper;
    @Override
    public IPage<ExceptionLog> pagination(MyPage<ExceptionLog> page, QExceptionLog q) {
        return exceptionLogMapper.pagination(page,q);
    }
}
