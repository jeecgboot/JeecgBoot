package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.OfUser;

/**
 * <p>
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Mapper
public interface OfUserMapper extends BaseMapper<OfUser> {

    OfUser findByUsername(String username);

}
