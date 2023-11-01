package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.VoteItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 群聊投票项 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Mapper
public interface VoteItemMapper extends BaseMapper<VoteItem> {

}
