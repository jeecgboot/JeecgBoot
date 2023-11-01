package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.entity.query_helper.QUserLog;

/**
 * <p>
 * 用户日志 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Mapper
public interface UserLogMapper extends BaseMapper<UserLog> {
    MyPage<UserLog> pagination(@Param("pg") MyPage<UserLog> pg, @Param("q") QUserLog q);
}
