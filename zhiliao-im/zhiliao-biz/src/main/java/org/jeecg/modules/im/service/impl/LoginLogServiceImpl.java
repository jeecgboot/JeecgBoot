package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.LoginLog;
import org.jeecg.modules.im.entity.query_helper.QLoginLog;
import org.jeecg.modules.im.mapper.LoginLogMapper;
import org.jeecg.modules.im.service.LoginLogService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;


    @Override
    public IPage<LoginLog> pagination(MyPage<LoginLog> page, QLoginLog q) {
        return loginLogMapper.pagination(page,q);
    }

    @Override
    public LoginLog findLatestByDeviceId(Integer deviceId) {
        return loginLogMapper.findLatestByDeviceNo(deviceId);
    }

    @Override
    public Long getRegisterCountOfIp(Date begin, Date end, String ip) {
        return loginLogMapper.getRegisterCountOfIp(begin.getTime(),end.getTime(),ip);
    }
    @Override
    public Long getRegisterCountOfDevice(Date begin, Date end, String deviceNo) {
        return loginLogMapper.getRegisterCountOfDevice(begin.getTime(),end.getTime(),deviceNo);
    }
}
