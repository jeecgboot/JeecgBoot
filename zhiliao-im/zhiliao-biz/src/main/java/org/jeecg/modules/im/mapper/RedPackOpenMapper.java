package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.RedPackOpen;
import org.jeecg.modules.im.entity.query_helper.QRedPackOpen;

/**
 * <p>
 * 拆红包记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Mapper
public interface RedPackOpenMapper extends BaseMapper<RedPackOpen> {
    MyPage<RedPackOpen> pagination(@Param("pg") MyPage<RedPackOpen> pg, @Param("q") QRedPackOpen q);
}
