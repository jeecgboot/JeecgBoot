package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.ExtraFeeOption;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: extra fees options
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
public interface IExtraFeeOptionService extends IService<ExtraFeeOption> {

    ExtraFeeOption getByName(String optionName);
}
