package org.jeecg.modules.smartfuel.systemInfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.smartfuel.systemInfo.entity.SmartfuelSystemInfo;
import org.jeecg.modules.smartfuel.systemInfo.mapper.SmartfuelSystemInfoMapper;
import org.jeecg.modules.smartfuel.systemInfo.service.ISmartfuelSystemInfoService;
import org.springframework.stereotype.Service;

/**
 * @Description: system_info
 * @Author: jeecg-boot
 * @Date: 2022-03-09
 * @Version: V1.0
 */
@Service
public class SmartfuelSystemInfoServiceImpl extends ServiceImpl<SmartfuelSystemInfoMapper, SmartfuelSystemInfo> implements ISmartfuelSystemInfoService {


    @Override
    public SmartfuelSystemInfo getSystemInfo() {
        return this.baseMapper.getSystemInfo();
    }

}
