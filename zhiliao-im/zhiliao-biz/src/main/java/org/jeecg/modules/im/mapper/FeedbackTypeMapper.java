package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.FeedbackType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 意见反馈类型 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Mapper
public interface FeedbackTypeMapper extends BaseMapper<FeedbackType> {

    List<FeedbackType> findAll();
}
