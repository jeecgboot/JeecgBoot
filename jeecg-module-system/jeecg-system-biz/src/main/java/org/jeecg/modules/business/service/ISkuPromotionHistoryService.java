package org.jeecg.modules.business.service;

import org.jeecg.modules.business.entity.SkuPromotionHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.vo.PromotionCouple;
import org.jeecg.modules.business.vo.PromotionDetail;

import java.util.List;

/**
 * @Description: SKU采购折扣历史
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
public interface ISkuPromotionHistoryService extends IService<SkuPromotionHistory> {

	List<PromotionCouple> selectByMainId(String mainId);
}
