package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.LogisticChannelChoice;
import java.util.List;

/**
 * @Description: logistic channel choice
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
public interface ILogisticChannelChoiceService extends IService<LogisticChannelChoice> {
	List<LogisticChannelChoice> fetchByShopId(List<String> shopIds);
}
