package org.jeecg.modules.business.service.impl.purchase;

import org.jeecg.modules.business.entity.SkuPromotionHistory;
import org.jeecg.modules.business.mapper.SkuPromotionHistoryMapper;
import org.jeecg.modules.business.service.ISkuPromotionHistoryService;
import org.jeecg.modules.business.vo.PromotionCouple;
import org.jeecg.modules.business.vo.PromotionDetail;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: SKU采购折扣历史
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Service
public class SkuPromotionHistoryServiceImpl extends ServiceImpl<SkuPromotionHistoryMapper, SkuPromotionHistory> implements ISkuPromotionHistoryService {

	@Autowired
	private SkuPromotionHistoryMapper skuPromotionHistoryMapper;

	@Override
	public List<PromotionCouple> selectByMainId(String mainId) {
		return skuPromotionHistoryMapper.selectByMainId(mainId);
	}
}
