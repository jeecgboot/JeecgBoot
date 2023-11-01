package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 意见反馈 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
    List<Feedback> findAll();
}
