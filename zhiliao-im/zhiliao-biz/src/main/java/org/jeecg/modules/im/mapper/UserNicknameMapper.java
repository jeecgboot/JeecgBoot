package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.UserNickname;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QUser;
import org.jeecg.modules.im.entity.query_helper.QUserNickname;

/**
 * <p>
 * 昵称记录，用于审核 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2022-11-22
 */
@Mapper
public interface UserNicknameMapper extends BaseMapper<UserNickname> {
    MyPage<UserNickname> pagination(@Param("pg") MyPage<UserNickname> pg, @Param("q") QUserNickname q);

}
