package org.jeecg.modules.im.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Helps;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 帮助 服务类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
public interface HelpsService extends IService<Helps> {
    Result<Object> findAll();

}
