package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Channel;
import org.jeecg.modules.im.entity.query_helper.QChannel;

/**
 * <p>
 * 频道 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Mapper
public interface ChannelMapper extends BaseMapper<Channel> {
    MyPage<Channel> pagination(@Param("pg") MyPage<Channel> pg, @Param("q") QChannel q);

}
