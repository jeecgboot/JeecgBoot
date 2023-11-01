package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.RedPack;
import org.jeecg.modules.im.entity.query_helper.QRedPack;

/**
 * <p>
 * 红包 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Mapper
public interface RedPackMapper extends BaseMapper<RedPack> {
    MyPage<RedPack> pagination(@Param("pg") MyPage<RedPack> pg, @Param("q") QRedPack q);
}
