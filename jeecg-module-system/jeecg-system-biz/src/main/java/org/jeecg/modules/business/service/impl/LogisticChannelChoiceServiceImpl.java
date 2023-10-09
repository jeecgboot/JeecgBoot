package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.entity.LogisticChannelChoice;
import org.jeecg.modules.business.mapper.LogisticChannelChoiceMapper;
import org.jeecg.modules.business.service.ILogisticChannelChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: logistic channel choice
 * @Author: jeecg-boot
 * @Date:   2023-10-03
 * @Version: V1.0
 */
@Service
public class LogisticChannelChoiceServiceImpl extends ServiceImpl<LogisticChannelChoiceMapper, LogisticChannelChoice> implements ILogisticChannelChoiceService {

	@Autowired
	private LogisticChannelChoiceMapper logisticChannelChoiceMapper;

	@Override
	public List<LogisticChannelChoice> fetchByShopId(List<String> shopIds) {
		return logisticChannelChoiceMapper.fetchByShopId(shopIds);
	}
}
