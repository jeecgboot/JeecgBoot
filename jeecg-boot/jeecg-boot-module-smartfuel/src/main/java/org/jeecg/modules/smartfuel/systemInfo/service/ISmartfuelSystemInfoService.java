package org.jeecg.modules.smartfuel.systemInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.smartfuel.systemInfo.entity.SmartfuelSystemInfo;

/**
 * @Description: system_info
 * @Author: jeecg-boot
 * @Date: 2022-03-09
 * @Version: V1.0
 */
public interface ISmartfuelSystemInfoService extends IService<SmartfuelSystemInfo> {
    
    SmartfuelSystemInfo getSystemInfo();


}
