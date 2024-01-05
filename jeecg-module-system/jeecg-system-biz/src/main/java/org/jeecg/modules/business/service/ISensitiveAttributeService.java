package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SensitiveAttribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: sensitive_attribute
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */

public interface ISensitiveAttributeService extends IService<SensitiveAttribute> {
    SensitiveAttribute getHighestPriorityAttribute(String orderId);
    String getHighestPriorityAttributeId(String orderId);

    List<SensitiveAttribute> listIdAndPriority();
}
