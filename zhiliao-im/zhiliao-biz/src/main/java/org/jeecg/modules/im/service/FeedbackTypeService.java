package org.jeecg.modules.im.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.FeedbackType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 意见反馈类型 服务类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
public interface FeedbackTypeService extends IService<FeedbackType> {

    Result<Object> findAll();
}
