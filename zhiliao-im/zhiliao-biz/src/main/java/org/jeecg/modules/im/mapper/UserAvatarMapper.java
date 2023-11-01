package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserAvatar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.UserAvatar;
import org.jeecg.modules.im.entity.query_helper.QUserAvatar;

import java.util.List;

/**
 * <p>
 * 用户历史头像 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2022-01-12
 */
@Mapper
public interface UserAvatarMapper extends BaseMapper<UserAvatar> {
    MyPage<UserAvatar> pagination(@Param("pg") MyPage<UserAvatar> pg, @Param("q") QUserAvatar q);
    List<UserAvatar> findAll(Integer userId);
}
