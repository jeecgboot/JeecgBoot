package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ClientCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: client category
 * @Author: jeecg-boot
 * @Date:   2023-10-19
 * @Version: V1.0
 */
public interface IClientCategoryService extends IService<ClientCategory> {

    String getClientCategoryByClientId(String clientId);

    String getIdByCode(String categoryName);
}
