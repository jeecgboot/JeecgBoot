package com.vmq.service;

import com.vmq.dao.SysLogDao;
import com.vmq.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现类
 * 2018-12-24 16:10
 */
@Slf4j
@Service
public class SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Async
    public void saveAsync(SysLog sysLog) {
        sysLog.setId(System.currentTimeMillis());
        sysLogDao.save(sysLog);
    }

}
