package org.jeecg.modules.im.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.FeedbackType;
import org.jeecg.modules.im.mapper.ExceptionLogMapper;
import org.jeecg.modules.im.mapper.FeedbackTypeMapper;
import org.jeecg.modules.im.service.FeedbackTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈类型 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Service
public class FeedbackTypeServiceImpl extends BaseServiceImpl<FeedbackTypeMapper, FeedbackType> implements FeedbackTypeService {
    @Autowired
    private FeedbackTypeMapper mapper;
    @Override
    public Result<Object> findAll() {
        return success(mapper.findAll());
    }
}