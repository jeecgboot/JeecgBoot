package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.query_helper.QDevice;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.LoginLogService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/device")
public class DeviceController extends BaseBackController {
    @Resource
    private DeviceService deviceService;
    @Resource
    private LoginLogService loginLogService;

    @RequestMapping("/pagination")
    public Result<Object> list(QDevice q){
        IPage<Device> page = deviceService.pagination(new MyPage<>(getPage(),getPageSize()),q);
        for (Device record : page.getRecords()) {
            record.setLoginLog(loginLogService.findLatestByDeviceId(record.getId()));
        }
        return success(page);
    }
}
