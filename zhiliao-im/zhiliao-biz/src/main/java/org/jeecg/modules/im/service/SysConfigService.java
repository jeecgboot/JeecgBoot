package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.SysConfig;

/**
 * <p>
 * 系统配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface SysConfigService extends IService<SysConfig> {
    SysConfig get();
}
