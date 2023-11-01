package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.LoginLog;
import org.jeecg.modules.im.entity.query_helper.QLoginLog;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface LoginLogService extends IService<LoginLog> {
    IPage<LoginLog> pagination(MyPage<LoginLog> page, QLoginLog q);
    LoginLog findLatestByDeviceId(Integer deviceId);
    //同一个ip单位时间内注册用户数量
    Long getRegisterCountOfIp(Date begin, Date end, String ip);
    //同一个设备单位时间内注册用户数量
    Long getRegisterCountOfDevice(Date begin,Date end,String deviceNo);
}
