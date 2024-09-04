package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.GiftRule;
import org.jeecg.modules.business.mapper.GiftRulesMapper;
import org.jeecg.modules.business.service.IGiftRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 添加赠品规则
 * @Author: jeecg-boot
 * @Date: 2024-09-02
 * @Version: V1.0
 */
@Service
@Slf4j
public class GiftRulesServiceImpl extends ServiceImpl<GiftRulesMapper, GiftRule> implements IGiftRulesService {

    @Autowired
    private GiftRulesMapper giftRulesMapper;

    public GiftRulesServiceImpl(GiftRulesMapper giftRulesMapper) {
        this.giftRulesMapper = giftRulesMapper;
    }

    @Override
    public List<GiftRule> findGiftRulesByShopCode(List<String> shopCodes) {
        return giftRulesMapper.findByShop(shopCodes);
    }
}