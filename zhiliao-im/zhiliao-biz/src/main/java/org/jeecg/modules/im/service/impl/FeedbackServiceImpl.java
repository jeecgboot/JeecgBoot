package org.jeecg.modules.im.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.mapper.FeedbackMapper;
import org.jeecg.modules.im.mapper.FeedbackTypeMapper;
import org.jeecg.modules.im.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 意见反馈 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    @Autowired
    private FeedbackMapper mapper;
    @Override
    public Result<Object> findAll() {
        return success(mapper.findAll());
    }
}
