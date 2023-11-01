package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-09-21
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo findByUserId(Integer userId);
}
