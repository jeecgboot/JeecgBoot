package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.LoginLog;
import org.jeecg.modules.im.entity.query_helper.QLoginLog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
    MyPage<LoginLog> pagination(@Param("pg") MyPage<LoginLog> pg, @Param("q") QLoginLog q);

    LoginLog findLatestByDeviceNo(Integer deviceId);
    Long getRegisterCountOfIp(Long begin,Long end,String ip);
    Long getRegisterCountOfDevice(Long begin,Long end,String deviceNo);
}
