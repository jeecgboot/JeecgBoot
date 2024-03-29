package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.Provider;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: provider
 * @Author: jeecg-boot
 * @Date:   2024-02-14
 * @Version: V1.0
 */
public interface IProviderService extends IService<Provider> {

    List<Provider> listByMabangIds(List<String> ids);
}
