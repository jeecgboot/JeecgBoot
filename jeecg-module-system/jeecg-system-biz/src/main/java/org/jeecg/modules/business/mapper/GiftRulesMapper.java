package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.entity.GiftRule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 添加赠品规则
 * @Author: jeecg-boot
 * @Date: 2024-09-02
 * @Version: V1.0
 */
@Repository
public interface GiftRulesMapper extends BaseMapper<GiftRule> {

    List<GiftRule> findByShop(List<String> shopCodes);
}
