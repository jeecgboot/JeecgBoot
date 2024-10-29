package org.jeecg.modules.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.business.entity.GiftRule;

import java.util.List;

/**
 * @Description: 添加赠品规则
 * @Author: jeecg-boot
 * @Date:   2024-09-02
 * @Version: V1.0
 */
public interface IGiftRulesService extends IService<GiftRule> {

    List<GiftRule> findGiftRulesByShopCode(List<String> shopCodes);
}