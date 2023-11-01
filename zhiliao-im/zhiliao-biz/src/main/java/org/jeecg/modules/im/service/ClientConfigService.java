package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.ClientConfig;

/**
 * <p>
 * 应用配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface ClientConfigService extends IService<ClientConfig> {

    ClientConfig get();
}
