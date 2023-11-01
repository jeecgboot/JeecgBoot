package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.MucConfig;

/**
 * <p>
 * 群组默认配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
public interface MucConfigService extends IService<MucConfig> {

    MucConfig findDefault();
    MucConfig findByMuc(Integer mucId);
}
